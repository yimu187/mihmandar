package tech.mihmandar.ui.presentation.view;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;
import org.vaadin.aceeditor.client.AceMarker;
import org.vaadin.aceeditor.client.AceRange;
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.ui.presentation.common.MihPanel;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.component.HeaderComponent;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;
import tech.mihmandar.ui.presentation.util.UiUtil;
import tech.mihmandar.ui.presentation.window.MihmandarAddProcessWindow;

@SuppressWarnings("serial")
public final class MihmandarView extends CustomComponent implements View{

    private HorizontalLayout content;
    private VerticalLayout contentV;
    private final VerticalLayout mainLayout;
    private HeaderComponent headerComponent;
    private AceEditor aceEditor;
    private Label description;
    private MihPanel panelEditor;

    public MihmandarView() {
        setSizeFull();
        MihmandarApplication.get().getMihmandarEventbus().register(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        setCompositionRoot(mainLayout);
        Responsive.makeResponsive(mainLayout);
        headerComponent = new HeaderComponent();

        Button addNewTraining = new Button();
        addNewTraining.setIcon(FontAwesome.EDIT);
        addNewTraining.addStyleName("icon-edit");
        addNewTraining.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        addNewTraining.setDescription("Edit Dashboard");
        addNewTraining.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                addWindow();
            }
        });

        headerComponent.addComponentToTools(addNewTraining);
        mainLayout.addComponent(headerComponent);

        buildContentByWidth();

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        mainLayout.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.CloseOpenWindowsEvent());
            }
        });


    }

    private void addWindow() {
        MihmandarAddProcessWindow window = new MihmandarAddProcessWindow();
        MihmandarApplication.get().addWindow(window);
    }

    private void buildContentByWidth() {
        int browserWindowWidth = MihmandarApplication.get().getPage().getBrowserWindowWidth();

        mainLayout.removeAllComponents();
        if(browserWindowWidth >= 900){
            mainLayout.addComponent(headerComponent);
            content = new HorizontalLayout();
            content = (HorizontalLayout) buildContent(content);
            mainLayout.addComponent(content);
            mainLayout.setExpandRatio(headerComponent, 1);
            mainLayout.setExpandRatio(content, 15);
        }else{
            mainLayout.removeAllComponents();
            contentV = new VerticalLayout();
            contentV = (VerticalLayout) buildContent(contentV);
            mainLayout.addComponent(contentV);
            mainLayout.setExpandRatio(headerComponent, 1);
            mainLayout.setExpandRatio(contentV, 1);

        }
    }

    private AbstractOrderedLayout buildContent(AbstractOrderedLayout content) {
        content.setSizeFull();
        content.addStyleName("m-view-content");
        content.setSpacing(true);
        Responsive.makeResponsive(content);

        description = new Label();
        description.setContentMode(ContentMode.HTML);
        description.setHeight(100, Unit.PERCENTAGE);
        description.setSizeFull();
        description.setValue("<strong>Test derlemesi için aşağıdaki kodu  yan taraftaki editörü kullanabilirsiniz:<br> class HelloWorldApp {<br>&nbsp public static void main(String[] args) {<br>&nbsp&nbsp System.out.println(\"Hello World!\"); // Display the string.<br>&nbsp } <br> }<strong>");
        description.setSizeFull();
        description.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        MihPanel notesPanel = new MihPanel("Açıklama");
        notesPanel.addStyleName("notes");
        notesPanel.addComponent(description);
        notesPanel.setHeight(100, Unit.PERCENTAGE);
        VerticalLayout notesVerticalLayout = new VerticalLayout();
        notesVerticalLayout.setSizeFull();
        notesVerticalLayout.addComponent(notesPanel);
        content.addComponent(notesVerticalLayout);

        aceEditor = new AceEditor();
        aceEditor.setHeight(100, Unit.PERCENTAGE);
        AceMode aceMode = AceMode.java;
        aceEditor.setMode(aceMode);
        aceEditor.setSizeFull();
        aceEditor.setTheme(AceTheme.terminal);
        aceEditor.setHighlightActiveLine(true);
        aceEditor.setHighlightSelectedWord(true);
        EnumSoftwareLanguages language = MihmandarApplication.get().getLanguage();
        language = language != null ? language : EnumSoftwareLanguages.JAVA;
        panelEditor = new MihPanel("Editör(" + language.toString() + ")");
        panelEditor.addStyleName("editor");
        panelEditor.setHeight(100, Unit.PERCENTAGE);

        VerticalLayout editorVerticalLayout = new VerticalLayout();
        editorVerticalLayout.setSizeFull();
        editorVerticalLayout.addComponent(panelEditor);
        content.addComponent(editorVerticalLayout);

        panelEditor.addComponent(aceEditor);

        panelEditor.addMenuToHeader("Derle", FontAwesome.PAINT_BRUSH, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String value = aceEditor.getValue();
                EnumSoftwareLanguages softLanguage = MihmandarApplication.get().getLanguage();
                UiUtil.compile(value, softLanguage);
                aceEditor.addMarker(new AceRange(9,1,9,5), "ace_highlight-marker", AceMarker.Type.line,  true, AceMarker.OnTextChange.ADJUST);
            }
        });

        panelEditor.addMenuToHeader("Çalıştır", FontAwesome.LONG_ARROW_LEFT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String value = aceEditor.getValue();
                EnumSoftwareLanguages softLanguage = MihmandarApplication.get().getLanguage();
                UiUtil.run(value, softLanguage);
            }
        });

        content.setExpandRatio(notesVerticalLayout, 1f);
        content.setExpandRatio(editorVerticalLayout, 1f);

        return content;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    @Subscribe
    public void resize(final MihmandarEvent.BrowserResizeEvent event) {
        buildContentByWidth();
        boolean headerComVisible = event.getHeight() > 900;
        headerComponent.setVisible(headerComVisible);
    }

    @Subscribe
    public void softwareLanguageChanged(final MihmandarEvent.SoftwareLanguageChagedEvent event) {
        EnumSoftwareLanguages softwareLanguage = event.getSoftwareLanguage();
        panelEditor.setCaption("Editör(" + softwareLanguage.toString() + ")");
        switch (softwareLanguage){
            case Python:{
                description.setValue("<strong>Test derlemesi için aşağıdaki kodu  yan taraftaki editörü kullanabilirsiniz:<br> &nbsp print(\"Hello World.\") <strong>");
                aceEditor.setMode(AceMode.python);

                break;
            }
            case JAVA:
            default:{
                aceEditor.setMode(AceMode.java);
                description.setValue("<strong>Test derlemesi için aşağıdaki kodu  yan taraftaki editörü kullanabilirsiniz:<br> class HelloWorldApp {<br>&nbsp public static void main(String[] args) {<br>&nbsp&nbsp System.out.println(\"Hello World!\"); // Display the string.<br>&nbsp } <br> }<strong>");
                break;
            }
        }
    }
}
