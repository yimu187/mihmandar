package tech.mihmandar.ui.presentation.component;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Murat on 10/5/2017.
 */
public class HeaderComponent extends CustomComponent {
    private Label titleLabel;
    private Button image;
    private Button imageJava;
    private Button imagePython;
    private HorizontalLayout titleImageLayout;

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
        imageJava.addStyleName("icon-edit");

        imagePython = new Button();
        imagePython.setIcon(new ThemeResource("img/python.png"));
        imagePython.addStyleName("icon-edit");

        image = imageJava;

        titleImageLayout.addComponent(image);

        Component edit = buildEditButton();
        ComboBox progLanguages = new ComboBox();
        progLanguages.setNullSelectionAllowed(false);
        progLanguages.addItem("Java");
        progLanguages.addItem("Python");
        progLanguages.setValue("Java");
        progLanguages.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                titleLabel.setValue(value.toString());
                titleImageLayout.removeAllComponents();
                if(value.equals("Java")){
                    image = imageJava;
                }else{
                    image = imagePython;
                }
                titleImageLayout.addComponent(titleLabel);
                titleImageLayout.addComponent(image);
            }
        });

        HorizontalLayout tools = new HorizontalLayout(edit, progLanguages);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);
        header.setComponentAlignment(titleImageLayout, Alignment.TOP_LEFT);
        header.setComponentAlignment(tools, Alignment.TOP_RIGHT);

        setCompositionRoot(header);
    }

    private Component buildEditButton() {
        Button result = new Button();
        result.setIcon(FontAwesome.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Dashboard");
        result.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
            }
        });
        return result;
    }
}
