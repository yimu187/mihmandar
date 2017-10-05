package tech.mihmandar.ui.presentation.view;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.io.FileUtils;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;
import tech.mihmandar.ui.presentation.common.MihPanel;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.component.HeaderComponent;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public final class MihmandarView extends Panel implements View{

    private HorizontalLayout content;
    private final VerticalLayout mainLayout;
    private AceEditor aceEditor;

    public MihmandarView() {
        setSizeFull();
        MihmandarApplication.get().getMihmandarEventbus().register(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        setContent(mainLayout);
        Responsive.makeResponsive(mainLayout);
        HeaderComponent headerComponent = new HeaderComponent();
        mainLayout.addComponent(headerComponent);

        Component content = buildContent();
        mainLayout.addComponent(content);
        mainLayout.setExpandRatio(headerComponent, 1);
        mainLayout.setExpandRatio(content, 15);

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        mainLayout.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.CloseOpenWindowsEvent());
            }
        });
    }


    private Component buildContent() {
        content = new HorizontalLayout();
        content.setSizeFull();
        content.setSpacing(true);
        Responsive.makeResponsive(content);

        TextArea notes = new TextArea();
        notes.setHeight(600, Unit.PIXELS);
        notes.setSizeFull();
        notes.setValue("Test derlemesi için aşağıdaki kodu  yan taraftaki editörü kullanabilirsiniz:\n class HelloWorldApp {\n\t public static void main(String[] args) {\n\t\t System.out.println(\"Hello World!\"); // Display the string.\n\t } \n }");
        notes.setSizeFull();
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        MihPanel notesPanel = new MihPanel("Notes");
        notesPanel.addComponent(notes);
        notesPanel.setHeight(650, Unit.PIXELS);
        content.addComponent(notesPanel);

        aceEditor = new AceEditor();
        aceEditor.setHeight(100, Unit.PERCENTAGE);
        AceMode aceMode = AceMode.java;
        aceEditor.setMode(aceMode);
        aceEditor.setSizeFull();
        aceEditor.setTheme(AceTheme.terminal);
        MihPanel panelEditor = new MihPanel("Editör");
        panelEditor.setHeight(650, Unit.PIXELS);
        panelEditor.addComponent(aceEditor);
        panelEditor.addMenuToHeader("Derle", FontAwesome.PAINT_BRUSH, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                doCompile();
            }
        });
        content.addComponent(panelEditor);
        content.setExpandRatio(notesPanel, 1f);
        content.setExpandRatio(panelEditor, 1f);

        return content;
    }

    private void doCompile() {
        String value = aceEditor.getValue();

        List<Diagnostic<? extends JavaFileObject>> diagnostics = Collections.emptyList();
        try {
            Random random = new Random();
            long i = random.nextInt(1000);
            String next = String.valueOf(i);
            String sourceFilePath = System.getProperty("java.io.tmpdir") + File.separator + "file" + next + ".java";
            File file = new File(sourceFilePath);
            file.createNewFile();
            FileUtils.writeByteArrayToFile(file, value.getBytes());

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<JavaFileObject>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(sourceFilePath));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticsCollector, null, null, compilationUnits);
            boolean success = task.call();
            if (!success) {
                diagnostics = diagnosticsCollector.getDiagnostics();
            }
            fileManager.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(diagnostics.isEmpty()){
            Notification.show("Derleme Başarılı", Notification.Type.HUMANIZED_MESSAGE);
        }else{
            String errors  = "";
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                errors += " " + diagnostic.getMessage(null);
            }
            Notification.show("Derleme Hatası" + errors, Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
