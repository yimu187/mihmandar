package tech.mihmandar.ui.presentation.common;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import tech.mihmandar.core.common.dto.UserDto;
import tech.mihmandar.core.common.enums.EnumYN;
import tech.mihmandar.core.data.user.domain.User;
import tech.mihmandar.core.data.user.domain.UserToken;
import tech.mihmandar.core.data.user.service.UserService;
import tech.mihmandar.core.data.user.service.UserTokenService;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;
import tech.mihmandar.ui.presentation.event.MihmandarEventBus;
import tech.mihmandar.ui.presentation.view.LoginView;
import tech.mihmandar.ui.presentation.view.MainView;
import tech.mihmandar.ui.presentation.view.NewUserView;
import tech.mihmandar.utility.enums.ErrorEnums;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * Created by MURAT YILMAZ on 11/5/2016.
 */
@Theme("mytheme")
@Title("Mihmandar")
@Component
public class MihmandarApplication extends UI {

    @Autowired
    UserService userService;

    @Autowired
    UserTokenService userTokenService;

    @Autowired
    private ApplicationContext applicationContext;

    private final MihmandarEventBus mihmandarEventbus = new MihmandarEventBus();


    public static MihmandarApplication get() {
        return (MihmandarApplication) getCurrent();
    }

    protected void init(VaadinRequest request) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        ServletContext servletContext = httpServletRequest.getSession().getServletContext();
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String yearAsInt = String.valueOf(year);
        getPage().setTitle("Mihmandar | " + yearAsInt);
        setTheme("mytheme");

        MihmandarEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent(false, null);

        Page.getCurrent().addBrowserWindowResizeListener(
                new Page.BrowserWindowResizeListener() {
                    public void browserWindowResized(
                            final Page.BrowserWindowResizeEvent event) {
                        MihmandarEventBus.post(new MihmandarEvent.BrowserResizeEvent());
                    }
                });
    }

    private void updateContent(boolean loginSucceded, String username) {
        Cookie authenticationCookie = CookieUtil.getAuthenticationCookie();
        Date expTime = null;
        EnumYN state = null;
        UserToken userToken = null;
        if(authenticationCookie != null){
            String cookie = authenticationCookie.getValue();
            try {
                CookieDto cookieDto = (CookieDto) CookieUtil.deserializeFromString(cookie);
                String token = cookieDto.getToken();
                userToken = userTokenService.findUserTokenByToken(token);
                expTime = CookieUtil.calculateExpirationDate(userToken);
                state = userToken != null ? userToken.getState() : null;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Date now = new Date();
        if(expTime != null && expTime.compareTo(now) > 0 && EnumYN.Y.equals(state)){
            if(userToken != null){
                User user = userTokenService.findUserByTokenId(userToken.getId());
                UserDto userDto = userService.convertUserDtoByUser(user);
                VaadinSession.getCurrent().setAttribute(UserDto.class.getName(), userDto);

            }
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        }else if(loginSucceded){
            if(StringUtils.hasText(username)){
                User user = userService.findUserByUsername(username);
                UserDto userDto = userService.convertUserDtoByUser(user);
                VaadinSession.getCurrent().setAttribute(UserDto.class.getName(), userDto);
            }
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        }
        else{
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public MihmandarEventBus getMihmandarEventbus() {
        return mihmandarEventbus;
    }

    @Subscribe
    public void userLoginRequested(final MihmandarEvent.UserLoginRequestedEvent event) {
        String userName = event.getUserName();
        String password = event.getPassword();
        boolean newUser = event.isNewUser();
        boolean rememberMe = event.isRememberMe();
        if(!newUser){
            User user = userService.findUserByUsername(userName);
            if(user == null){
                Notification.show("Kullanıcı bilgileri hatalı", Notification.Type.TRAY_NOTIFICATION);
            }else{
                boolean matches = userService.checkPassword(password, user.getPassword());
                if(matches){
                    UserDto userDto = userService.convertUserDtoByUser(user);
                    VaadinSession.getCurrent().setAttribute(UserDto.class.getName(), userDto);
                    if(rememberMe){
                        saveAuthenticationCookie(userName, password);
                    }else{
                        CookieUtil.clearAuthenticationCookie();
                    }
                    updateContent(true, userName);
                }else{

                    Notification notification = new Notification(
                            ErrorEnums.HATALI_SIFRE.toString());
                    notification
                            .setDescription("<span>Girilen kullanıcı adı ve şifre uyuşmamaktadır. Yeni Üye ile kayıt olabilirsiniz veya şifre mi unuttum ile şifrenizi sıfırlayabilirsiniz</span>");
                    notification.setHtmlContentAllowed(true);
                    notification.setStyleName("tray dark small closable login-help");
                    notification.setPosition(Position.BOTTOM_CENTER);
                    notification.setDelayMsec(1000);
                    notification.show(Page.getCurrent());
                }
            }
        }else{
            setContent(new NewUserView());
        }
    }


    private void saveAuthenticationCookie(String userName, String password) {
        String token = UUID.randomUUID().toString();
        EnumYN state = EnumYN.Y;
        Long expMinutes = 60L;

        userTokenService.saveUserTokenByUserNameTokenStateAndExpMinutes(userName, token, state, expMinutes);
        CookieUtil.saveAuthenticationCookieToBrowser(userName, password, token, state);
    }



    @Subscribe
    public void userLoggedOut(final MihmandarEvent.UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();

        Cookie authenticationCookie = CookieUtil.getAuthenticationCookie();
        CookieDto cookieDto = null;
        if(authenticationCookie != null){
            String cookie = authenticationCookie.getValue();
            try {
                cookieDto = (CookieDto) CookieUtil.deserializeFromString(cookie);
                String token = cookieDto.getToken();
                if(token != null){
                    UserToken userToken = userTokenService.findUserTokenByToken(token);
                    userToken.setState(EnumYN.N);
                    userTokenService.save(userToken);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        String result = null;
        try {
            if(cookieDto == null){
                cookieDto = new CookieDto();
            }
            result = CookieUtil.serializeToString(cookieDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cookie cookieBrowser = new Cookie(CookieUtil.AUTHENTICATION_COOKIE_NAME, result);
        cookieBrowser.setMaxAge(60 * 60 * 24 * 365); // Store cookie for 1 year
        cookieBrowser.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(cookieBrowser);
    }

    @Subscribe
    public void closeOpenWindows(final MihmandarEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }
}
