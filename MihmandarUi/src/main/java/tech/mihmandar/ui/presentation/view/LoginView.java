package tech.mihmandar.ui.presentation.view;

/**
 * Created by Murat on 6/18/2017.
 */

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import tech.mihmandar.core.common.enums.EnumYN;
import tech.mihmandar.core.data.user.domain.UserToken;
import tech.mihmandar.core.data.user.service.UserTokenService;
import tech.mihmandar.ui.presentation.common.CookieDto;
import tech.mihmandar.ui.presentation.common.CookieUtil;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;
import tech.mihmandar.utility.service.MihmandarFileConfigService;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Date;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    @Autowired
    UserTokenService userTokenService;

    private CheckBox rememberMe;
    private TextField username;
    private PasswordField password;
    private Button signin;

    public LoginView() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        setSizeFull();

        Component loginForm = buildLoginForm();
        updateFieldsByCookieInfo();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        rememberMe = new CheckBox("Beni Hatirla", true);
        horizontalLayout.addComponent(rememberMe);

        String url = MihmandarFileConfigService.getApplicationUrl() + "/#!newUser";

        HorizontalLayout hLinkYeniUye = new HorizontalLayout();
        hLinkYeniUye.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.UserLoginRequestedEvent(null
                        , null, true, false));
            }
        });
        Link linkYeniUye = new Link("Yeni Üye",
                new ExternalResource(url));
        hLinkYeniUye.addComponent(linkYeniUye);
        horizontalLayout.addComponent(hLinkYeniUye);

        HorizontalLayout hLinkYeniSifre = new HorizontalLayout();
        hLinkYeniSifre.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.UserLoginRequestedEvent(null
                        , null, true, false));
            }
        });
        Link linkYeniSifre = new Link("Şifremi Unuttum",
                new ExternalResource(url));
        hLinkYeniSifre.addComponent(linkYeniSifre);

        horizontalLayout.setComponentAlignment(rememberMe, Alignment.BOTTOM_LEFT);
        horizontalLayout.setComponentAlignment(hLinkYeniUye, Alignment.BOTTOM_RIGHT);

        loginPanel.addComponent(horizontalLayout);
        loginPanel.addComponent(hLinkYeniSifre);
        loginPanel.setComponentAlignment(hLinkYeniSifre, Alignment.BOTTOM_RIGHT);

        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        username = new TextField("Kullanıcı Adı");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password = new PasswordField("Şifre");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

       signin = new Button("Giriş");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.UserLoginRequestedEvent(username
                        .getValue(), password.getValue(), false, rememberMe.getValue()));
            }
        });

        return fields;
    }

    private void updateFieldsByCookieInfo() {
        Cookie authenticationCookie = CookieUtil.getAuthenticationCookie();
        boolean rememberMeSelected = false;
        if(authenticationCookie != null){
            String cookie = authenticationCookie.getValue();
            try {
                CookieDto cookieDto = (CookieDto) CookieUtil.deserializeFromString(cookie);
                String usrName = cookieDto.getUsername() != null ? cookieDto.getUsername() : "";
                String pass = cookieDto.getPassword() != null ? cookieDto.getPassword() : "";
                username.setValue(usrName);
                password.setValue(pass);
                rememberMeSelected = StringUtils.hasText(usrName);
                String token = cookieDto.getToken();
                UserToken userToken = userTokenService.findUserTokenByToken(token);
                Date expTime = CookieUtil.calculateExpirationDate(userToken);
                EnumYN state = userToken != null ? userToken.getState() : null;
                Date now = new Date();
                if(expTime != null && expTime.compareTo(now) > 0 && EnumYN.Y.equals(state)){
                    signin.click();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        rememberMe.setValue(rememberMeSelected);
    }

    private Component buildLabels() {
        HorizontalLayout labels = new HorizontalLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("MİHMANDARA HOŞ GELDİNİZ");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        return labels;
    }

}
