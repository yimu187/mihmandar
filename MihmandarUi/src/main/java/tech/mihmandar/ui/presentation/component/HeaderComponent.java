package tech.mihmandar.ui.presentation.component;

import com.vaadin.data.Property;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;

/**
 * Created by Murat on 10/5/2017.
 */
public class HeaderComponent extends CustomComponent {
    private Label titleLabel;
    private Button image;
    private Button imageJava;
    private Button imagePython;
    private HorizontalLayout titleImageLayout;
    private HorizontalLayout tools;
    private ComboBox progLanguages;

    public HeaderComponent() {
        HorizontalLayout header = new HorizontalLayout();
        header.setSizeFull();
        header.setSpacing(true);
        header.addStyleName("header");

        titleImageLayout = new HorizontalLayout();
        titleImageLayout.setSpacing(true);
        header.addComponent(titleImageLayout);

        titleLabel = new Label("Java");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName("header-title-label");
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        titleImageLayout.addComponent(titleLabel);

        imageJava = new Button();
        imageJava.setIcon(new ThemeResource("img/java.png"));
        imageJava.addStyleName(ValoTheme.BUTTON_ICON_ONLY);

        imagePython = new Button();
        imagePython.setIcon(new ThemeResource("img/python.png"));
        imagePython.addStyleName(ValoTheme.BUTTON_ICON_ONLY);

        image = imageJava;

        titleImageLayout.addComponent(image);

        progLanguages = new ComboBox();
        progLanguages.setNullSelectionAllowed(false);
        EnumSoftwareLanguages[] values = EnumSoftwareLanguages.values();
        progLanguages.addItems(values);
        EnumSoftwareLanguages selectedLanguage = MihmandarApplication.get().getLanguage();
        EnumSoftwareLanguages language = selectedLanguage != null ? selectedLanguage : EnumSoftwareLanguages.JAVA;
        progLanguages.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                EnumSoftwareLanguages value = (EnumSoftwareLanguages)event.getProperty().getValue();
                titleLabel.setValue(value.toString());
                titleImageLayout.removeAllComponents();
                if(EnumSoftwareLanguages.JAVA.equals(value)){
                    image = imageJava;
                }else{
                    image = imagePython;
                }
                titleImageLayout.addComponent(titleLabel);
                titleImageLayout.addComponent(image);
                MihmandarApplication.get().setLanguage(value);
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.SoftwareLanguageChagedEvent(value));
            }
        });
        progLanguages.setValue(language);

        tools = new HorizontalLayout(progLanguages);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);
        header.setComponentAlignment(titleImageLayout, Alignment.TOP_LEFT);
        header.setComponentAlignment(tools, Alignment.TOP_RIGHT);

        setCompositionRoot(header);
    }

    public void addComponentToTools(Component component){
        tools.addComponentAsFirst(component);
    }

    public void setLanguageComboEnabled(boolean enabled){
        progLanguages.setEnabled(enabled);
    }
}
