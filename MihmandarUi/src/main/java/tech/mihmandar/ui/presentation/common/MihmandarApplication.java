package tech.mihmandar.ui.presentation.common;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.stereotype.Component;
import tech.mihmandar.core.base.dto.UserDto;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;
import tech.mihmandar.ui.presentation.event.MihmandarEventBus;
import tech.mihmandar.ui.presentation.view.LoginView;
import tech.mihmandar.ui.presentation.view.MainView;

import java.util.Calendar;


/**
 * Created by MURAT YILMAZ on 11/5/2016.
 */
@Theme("mytheme")
@Title("Mihmandar")
@Component
public class MihmandarApplication extends UI {

    private Navigator navigator;

    private final MihmandarEventBus mihmandarEventbus = new MihmandarEventBus();

    public static MihmandarApplication get() {
        return (MihmandarApplication) getCurrent();
    }


    protected void init(VaadinRequest request) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String yearAsInt = String.valueOf(year);
        getPage().setTitle("Mihmandar | " + yearAsInt);
        setTheme("mytheme");

        MihmandarEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(
                new Page.BrowserWindowResizeListener() {
                    public void browserWindowResized(
                            final Page.BrowserWindowResizeEvent event) {
                        MihmandarEventBus.post(new MihmandarEvent.BrowserResizeEvent());
                    }
                });
//
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        horizontalLayout.setSizeFull();
//        horizontalLayout.addStyleName("backColorGrey");
//        MihPanel panel = new MihPanel();
//        panel.setCaption("Yazılım Dili Seçim");
//        panel.addStyleName("backColorGrey");
//        panel.setSizeFull();
//        Label label = new Label();
//        label.setValue("Test Background color");

//        horizontalLayout.addComponent(label);
//        content.addComponent(panel);
//        content.setComponentAlignment(panel, Alignment.BOTTOM_CENTER);



//        Responsive.makeResponsive(this);
//        getPage().setTitle("Mihmandar");
//
//        final VerticalLayout mainLayout = new VerticalLayout();
//        mainLayout.setMargin(true);
//        setContent(mainLayout);
//
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        horizontalLayout.setSizeFull();
//        mainLayout.addComponent(horizontalLayout);
//
//        MihPanel panel1 = new MihPanel();
//        panel1.setCaption("İşlem");
//        panel1.setHeight(600, Unit.PIXELS);
//        horizontalLayout.addComponent(panel1);
//
//        MihPanel panel2 = new MihPanel();
//        panel2.setCaption("Editör");
//        panel2.setSizeFull();
//        panel2.setHeight(600, Unit.PIXELS);
//        horizontalLayout.addComponent(panel2);
//
//        horizontalLayout.setExpandRatio(panel1, 1f);
//        horizontalLayout.setExpandRatio(panel2, 1f);
//
//        setContent(mainLayout);
//
//        final AceEditor aceEditor = new AceEditor();
//        AceMode aceMode = AceMode.java;
//        aceEditor.setMode(aceMode);
//        aceEditor.setSizeFull();
//        aceEditor.setTheme(AceTheme.terminal);
//        aceEditor.setHeight(500, Unit.PIXELS);
//        panel2.addComponent(aceEditor);
//
//        panel2.setCaption("Ace Mode("+ aceMode+ ")");
//
//        Button btn = new Button("Derle");
//        btn.addClickListener(new Button.ClickListener() {
//            public void buttonClick(Button.ClickEvent event) {
//                String value = aceEditor.getValue();
//
//                List<Diagnostic<? extends JavaFileObject>> diagnostics = Collections.emptyList();
//                try {
//                    Random random = new Random();
//                    long i = random.nextInt(1000);
//                    String next = String.valueOf(i);
//                    String sourceFilePath = System.getProperty("java.io.tmpdir") + File.separator + "file" + next + ".java";
//                    File file = new File(sourceFilePath);
//                    file.createNewFile();
//                    FileUtils.writeByteArrayToFile(file, value.getBytes());
//
//                    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//
//                    DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<JavaFileObject>();
//                    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
//                    Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(sourceFilePath));
//                    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticsCollector, null, null, compilationUnits);
//                    boolean success = task.call();
//                    if (!success) {
//                        diagnostics = diagnosticsCollector.getDiagnostics();
//                    }
//                    fileManager.close();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if(diagnostics.isEmpty()){
//                    Notification.show("Derleme Başarılı", Notification.Type.HUMANIZED_MESSAGE);
//                }else{
//                    String errors  = "";
//                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
//                        errors += " " + diagnostic.getMessage(null);
//                    }
//                    Notification.show("Derleme Hatası" + errors, Notification.Type.HUMANIZED_MESSAGE);
//                }
//
//            }
//        });
//        mainLayout.addComponent(btn);
//        mainLayout.setComponentAlignment(btn, Alignment.BOTTOM_RIGHT);
    }

    private void updateContent() {
        UserDto user = (UserDto) VaadinSession.getCurrent().getAttribute(
                UserDto.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    public MihmandarEventBus getMihmandarEventbus() {
        return mihmandarEventbus;
    }

    @Subscribe
    public void userLoginRequested(final MihmandarEvent.UserLoginRequestedEvent event) {
        UserDto user = new UserDto();
        user.setFirstName("MURAT");
        user.setLastName("YILMAZ");
        user.setRole("admin");
        String email = user.getFirstName().toLowerCase() + "."
                + user.getLastName().toLowerCase() + "@"
                + "yilmaztech" + ".com";
        user.setEmail(email.replaceAll(" ", ""));
        user.setLocation("İstanbul");
        user.setBio("Bu bir biyografidir");
        VaadinSession.getCurrent().setAttribute(UserDto.class.getName(), user);
        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final MihmandarEvent.UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final MihmandarEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }
}
