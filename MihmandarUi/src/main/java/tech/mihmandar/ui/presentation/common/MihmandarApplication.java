package tech.mihmandar.ui.presentation.common;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import tech.mihmandar.core.common.dto.UserDto;
import tech.mihmandar.core.data.user.domain.User;
import tech.mihmandar.core.data.user.service.UserService;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;
import tech.mihmandar.ui.presentation.event.MihmandarEventBus;
import tech.mihmandar.ui.presentation.view.LoginView;
import tech.mihmandar.ui.presentation.view.MainView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;


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

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(
                new Page.BrowserWindowResizeListener() {
                    public void browserWindowResized(
                            final Page.BrowserWindowResizeEvent event) {
                        MihmandarEventBus.post(new MihmandarEvent.BrowserResizeEvent());
                    }
                });
    }

    private void updateContent() {
        UserDto user = (UserDto) VaadinSession.getCurrent().getAttribute(
                UserDto.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
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
        User user = userService.findUserByUsername(userName);
        if(user == null){
            Notification.show("Kullanıcı bilgileri hatalı", Notification.Type.TRAY_NOTIFICATION);
        }else{
            UserDto userDto = new UserDto();
            userDto.setFirstName("MURAT");
            userDto.setLastName("YILMAZ");
            userDto.setRole("admin");
            String email = userDto.getFirstName().toLowerCase() + "."
                    + userDto.getLastName().toLowerCase() + "@"
                    + "yilmaztech" + ".com";
            userDto.setEmail(email.replaceAll(" ", ""));
            userDto.setLocation("İstanbul");
            userDto.setBio("Bu bir biyografidir");
            VaadinSession.getCurrent().setAttribute(UserDto.class.getName(), userDto);
            updateContent();
        }
    }

    @Subscribe
    public void userLoggedOut(final MihmandarEvent.UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final MihmandarEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }
}
