package tech.mihmandar.ui.presentation.view;

/**
 * Created by Murat on 6/18/2017.
 */

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;
import tech.mihmandar.ui.presentation.event.MihmandarEventBus;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        Notification notification = new Notification(
                "Welcome to Dashboard Demo");
        notification
                .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
        notification.show(Page.getCurrent());
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
        CheckBox checkBox = new CheckBox("Beni Hatirla", true);
        horizontalLayout.addComponent(checkBox);

        Link link = new Link("Yeni Üye",
                new ExternalResource("http://vaadin.com/"));
        horizontalLayout.addComponent(link);

        horizontalLayout.setComponentAlignment(checkBox, Alignment.BOTTOM_LEFT);
        horizontalLayout.setComponentAlignment(link, Alignment.BOTTOM_RIGHT);
        loginPanel.addComponent(horizontalLayout);

        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Kullanıcı Adı");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Şifre");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Giriş");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.UserLoginRequestedEvent(username
                        .getValue(), password.getValue()));
            }
        });
        return fields;
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
