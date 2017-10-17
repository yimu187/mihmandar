package tech.mihmandar.ui.presentation.window;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import tech.mihmandar.core.common.enums.EnumAccessType;
import tech.mihmandar.core.common.enums.EnumProcessType;
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.core.data.training.domain.Training;
import tech.mihmandar.core.data.training.service.TrainingService;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;

/**
 * Created by Murat on 10/8/2017.
 */
public class MihmandarAddProcessWindow extends Window {

    @Autowired
    TrainingService trainingService;

    private VerticalLayout windowLayout;
    private TextField name;
    private TextArea description;
    private OptionGroup accessType;
    private OptionGroup optionGroupProcess;

    public MihmandarAddProcessWindow() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        center();
        setCaption("Ekler");
        setWidth(40, Unit.PERCENTAGE);
        setHeight(40, Unit.PERCENTAGE);

        buildWindowLayout();

        setContent(windowLayout);
    }

    private void buildWindowLayout() {
        windowLayout = new VerticalLayout();
        windowLayout.setSizeFull();
        windowLayout.setSpacing(true);

        optionGroupProcess = new OptionGroup();
        optionGroupProcess.setCaption("İşlem Türü Seçiniz");
        optionGroupProcess.addItems(EnumProcessType.values());
        optionGroupProcess.setValue(EnumProcessType.TRAINING);
        optionGroupProcess.addStyleName("horizontal");
        windowLayout.addComponent(optionGroupProcess);
        optionGroupProcess.setItemEnabled(EnumProcessType.LAB, false);
        optionGroupProcess.setItemEnabled(EnumProcessType.EXAM, false);

        name = new TextField();
        name.setInputPrompt("Bir isim giriniz");
        name.setSizeFull();
        windowLayout.addComponent(name);

        accessType = new OptionGroup();
        accessType.setCaption("Erişim Türü Seçiniz");
        accessType.addItems(EnumAccessType.values());
        accessType.setValue(EnumAccessType.PUBLIC);
        accessType.addStyleName("horizontal");
        windowLayout.addComponent(accessType);

        description = new TextArea();
        description.setInputPrompt("Açıklama giriniz");
        description.setSizeFull();
        windowLayout.addComponent(description);

        VerticalLayout buttomLayout = new VerticalLayout();
        buttomLayout.setSizeFull();
        buttomLayout.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);

        Button btnOk = new Button("Devam");
        btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnOk.addStyleName(ValoTheme.BUTTON_SMALL);
        btnOk.addClickListener((Button.ClickListener) event1 -> {
            Training training = new Training();
            training.setName(name.getValue());
            training.setDescription(description.getValue());
            EnumSoftwareLanguages language = MihmandarApplication.get().getLanguage();
            training.setLanguage(language);
            training.setProcessType((EnumProcessType) optionGroupProcess.getValue());
            training.setAccessType((EnumAccessType)accessType.getValue());
            training = trainingService.save(training);
            String navState = "Egitim/"+training.getId();
            MihmandarApplication.get().getNavigator().navigateTo(navState);
        });
        buttonsLayout.addComponent(btnOk);

        Button btnCancel = new Button("İptal");
        btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
        btnCancel.addStyleName(ValoTheme.BUTTON_SMALL);
        btnCancel.addClickListener((Button.ClickListener) event1 -> {
            MihmandarApplication.get().removeWindow(MihmandarAddProcessWindow.this);
        });
        buttonsLayout.addComponentAsFirst(btnCancel);

        buttomLayout.addComponent(buttonsLayout);
        buttomLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);

        windowLayout.addComponent(buttomLayout);
        windowLayout.setComponentAlignment(buttomLayout, Alignment.BOTTOM_RIGHT);

        windowLayout.setExpandRatio(name, 1f);
        windowLayout.setExpandRatio(description, 3f);
        windowLayout.setExpandRatio(buttomLayout, 1f);
    }
}
