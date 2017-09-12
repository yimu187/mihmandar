package tech.mihmandar.ui.presentation.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import tech.mihmandar.core.common.enums.EnumGender;
import tech.mihmandar.core.data.user.domain.User;
import tech.mihmandar.core.data.user.service.UserService;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;

/**
 * Created by Murat on 9/11/2017.
 */
public class NewUserView extends VerticalLayout implements View {

    @Autowired
    UserService userService;

    public NewUserView() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        setSizeFull();

        Component loginForm = buildNewUserForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildNewUserForm() {
        final VerticalLayout newUserPanel = new VerticalLayout();
        newUserPanel.setSizeUndefined();
        newUserPanel.setSpacing(true);
        Responsive.makeResponsive(newUserPanel);
        newUserPanel.addStyleName("login-panel");

        HorizontalLayout hLayout1 = new HorizontalLayout();
        hLayout1.setSpacing(true);
        newUserPanel.addComponent(hLayout1);

        final TextField tfEmail = new TextField("Email");
        tfEmail.setSizeFull();
        hLayout1.addComponent(tfEmail);

        final PasswordField passwordField = new PasswordField("Şifre");
        passwordField.setSizeFull();
        hLayout1.addComponent(passwordField);

        HorizontalLayout hLayout2 = new HorizontalLayout();
        hLayout2.setSpacing(true);
        newUserPanel.addComponent(hLayout2);

        final TextField tfAdi = new TextField("Adı");
        tfAdi.setSizeFull();
        hLayout2.addComponent(tfAdi);

        final TextField tfSoyadi = new TextField("Soyadı");
        tfSoyadi.setSizeFull();
        hLayout2.addComponent(tfSoyadi);

        HorizontalLayout hLayout3 = new HorizontalLayout();
        hLayout3.setSpacing(true);
        newUserPanel.addComponent(hLayout3);

        final TextField tfTitle = new TextField("Ünvanı");
        tfTitle.setSizeFull();
        hLayout3.addComponent(tfTitle);

        final TextField tfTelefon = new TextField("Telefon");
        tfTelefon.setSizeFull();
        hLayout3.addComponent(tfTelefon);

        HorizontalLayout hLayout4 = new HorizontalLayout();
        hLayout4.setSpacing(true);
        newUserPanel.addComponent(hLayout4);

        final ComboBox genderCombo = new ComboBox("Cinsiyet");
        genderCombo.addItem(EnumGender.FEMALE);
        genderCombo.addItem(EnumGender.MALE);
        genderCombo.addItem(EnumGender.OTHER);
        hLayout4.addComponent(genderCombo);

        final TextField tfwebsite = new TextField("Web Sitesi");
        tfwebsite.setSizeFull();
        hLayout4.addComponent(tfwebsite);

        TextArea taAdres = new TextArea();
        taAdres.setSizeFull();
        taAdres.setInputPrompt("Adres");
        newUserPanel.addComponent(taAdres);

        TextArea taBio = new TextArea();
        taBio.setSizeFull();
        taBio.setInputPrompt("Biyografi");
        newUserPanel.addComponent(taBio);

        Button btnSave = new Button("Kaydet");
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                User user = new User();
                user.setEmail(tfEmail.getValue());
                String value = passwordField.getValue();
                String password = userService.encryptPassword(value);
                user.setPassword(password);
                user.setFirstName(tfAdi.getValue());
                user.setLastName(tfSoyadi.getValue());
                user.setTitle(tfTitle.getValue());
                user.setPhone(tfTelefon.getValue());
                user.setGender((EnumGender)genderCombo.getValue());
                user.setWebsite(tfwebsite.getValue());
                user.setBio(taBio.getValue());
                user.setLocation(taAdres.getValue());

                userService.save(user);
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.UserLoginRequestedEvent(tfEmail.getValue()
                        , password, false));
            }
        });

        newUserPanel.addComponent(btnSave);
        newUserPanel.setComponentAlignment(btnSave, Alignment.BOTTOM_RIGHT);

        return newUserPanel;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        System.out.println(event.getNewView());
    }
}
