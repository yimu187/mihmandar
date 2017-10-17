package tech.mihmandar.ui.presentation.view;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

public enum ViewType {
    MIHMANDAR_HOME("Derle", MihmandarView.class, getCompileIcon(), true, "Derle"),
    MIHMANDAR_TRAINING("Egitim", MihmandarTrainingView.class, getPencilIcon(), true, "Eğitim")

    ;

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private final String name;

    ViewType(final String viewName,
                     final Class<? extends View> viewClass, final Resource icon,
                     final boolean stateful, String name) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.name = name;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public String getName() {
        return name;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    private static Resource getPencilIcon() {
        return new ThemeResource("img/student-glasses.png");
    }

    private static Resource getCompileIcon() {
        return new ThemeResource("img/compile.png");
    }

    public static ViewType getByViewName(final String viewName) {
        ViewType result = null;
        for (ViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
