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
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.ui.presentation.common.MihPanel;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.component.HeaderComponent;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;
import tech.mihmandar.ui.presentation.util.UiUtil;
import tech.mihmandar.ui.presentation.window.MihmandarAddProcessWindow;
import tech.mihmandar.utility.compiler.JavaCompilerUtil;
import tech.mihmandar.utility.dto.CompileResultDto;

@SuppressWarnings("serial")
public final class MihmandarView extends Panel implements View{

    private HorizontalLayout content;
    private VerticalLayout contentV;
    private final VerticalLayout mainLayout;
    private HeaderComponent headerComponent;
    private AceEditor aceEditor;
    private Label description;

    public MihmandarView() {
        setSizeFull();
        MihmandarApplication.get().getMihmandarEventbus().register(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        setContent(mainLayout);
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

        int browserWindowWidth = MihmandarApplication.get().getPage().getBrowserWindowWidth();
        buildContentByWidth(browserWindowWidth);

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

    private void buildContentByWidth(int browserWindowWidth) {
        mainLayout.removeAllComponents();
        if(browserWindowWidth >= 700){
            mainLayout.addComponent(headerComponent);
            content = new HorizontalLayout();
            content = (HorizontalLayout) buildContent(content);
            mainLayout.addComponent(content);
            mainLayout.setExpandRatio(headerComponent, 1);
            mainLayout.setExpandRatio(content, 15);
        }else{
            mainLayout.removeAllComponents();
            mainLayout.addComponent(headerComponent);
            contentV = new VerticalLayout();
            contentV = (VerticalLayout) buildContent(contentV);
            mainLayout.addComponent(contentV);
            mainLayout.setExpandRatio(headerComponent, 1);
            mainLayout.setExpandRatio(contentV, 15);

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
        content.addComponent(notesPanel);

        aceEditor = new AceEditor();
        aceEditor.setHeight(100, Unit.PERCENTAGE);
        AceMode aceMode = AceMode.java;
        aceEditor.setMode(aceMode);
        aceEditor.setSizeFull();
        aceEditor.setTheme(AceTheme.terminal);
        MihPanel panelEditor = new MihPanel("Editör");
        panelEditor.addStyleName("editor");
        panelEditor.setHeight(100, Unit.PERCENTAGE);
        panelEditor.addComponent(aceEditor);
        panelEditor.addMenuToHeader("Derle", FontAwesome.PAINT_BRUSH, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String value = aceEditor.getValue();
                UiUtil.doCompile(value);
            }
        });
        content.addComponent(panelEditor);
        content.setExpandRatio(notesPanel, 1f);
        content.setExpandRatio(panelEditor, 1f);

        return content;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    @Subscribe
    public void resize(final MihmandarEvent.BrowserResizeEvent event) {
        int width = event.getWidth();
        buildContentByWidth(width);
    }

    @Subscribe
    public void softwareLanguageChanged(final MihmandarEvent.SoftwareLanguageChagedEvent event) {
        EnumSoftwareLanguages softwareLanguage = event.getSoftwareLanguage();
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
