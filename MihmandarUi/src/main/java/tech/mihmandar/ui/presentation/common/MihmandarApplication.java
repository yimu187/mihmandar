package tech.mihmandar.ui.presentation.common;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Created by MURAT YILMAZ on 11/5/2016.
 */
@Component
public class MihmandarApplication extends UI {

    private Navigator navigator;

    protected void init(VaadinRequest request) {

        Responsive.makeResponsive(this);
        getPage().setTitle("Mihmandar");

        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setMargin(true);
        setContent(mainLayout);

        MihPanel panel = new MihPanel("Editör");
        panel.setSizeFull();

        setContent(mainLayout);

        final AceEditor aceEditor = new AceEditor();
        AceMode aceMode = AceMode.java;
        aceEditor.setMode(aceMode);
        aceEditor.setSizeFull();
        aceEditor.setTheme(AceTheme.terminal);
        aceEditor.setHeight(500, Unit.PIXELS);
        panel.setContent(aceEditor);
        mainLayout.addComponent(panel);

        panel.setCaption(panel.getCaption() + "Ace Mode("+ aceMode+ ")");

        Button btn = new Button("Derle");
        btn.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
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
        });
        mainLayout.addComponent(btn);
        mainLayout.setComponentAlignment(btn, Alignment.BOTTOM_RIGHT);
    }

//        navigator = new Navigator(this, this);
//
//        navigator.addView("",EMasaView.class); //default view - ilk açılan view tanımı
//        navigator.addView(EMasaView.NAME,EMasaView.class);
//        navigator.addView(NavigatorViewImpl.NAME,NavigatorViewImpl.class);

}
