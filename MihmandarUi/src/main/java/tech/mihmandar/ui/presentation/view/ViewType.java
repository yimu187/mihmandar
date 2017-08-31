package tech.mihmandar.ui.presentation.view;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum ViewType {
    MIHMANDAR_HOME("dashboard", MihmandarView.class, FontAwesome.HOME, true)
//    , SALES("sales", SalesView.class, FontAwesome.BAR_CHART_O, false)
//    , TRANSACTIONS("transactions", TransactionsView.class, FontAwesome.TABLE, false)
//    , REPORTS("reports", ReportsView.class, FontAwesome.FILE_TEXT_O, true)
//    , SCHEDULE("schedule", ScheduleView.class, FontAwesome.CALENDAR_O, false)
    ;

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    ViewType(final String viewName,
                     final Class<? extends View> viewClass, final Resource icon,
                     final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
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
