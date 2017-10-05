package tech.mihmandar.ui.presentation.view;


import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;

/**
 * Created by Murat on 9/19/2017.
 */
public class TrainingMenu extends Panel implements View {

    private VerticalLayout root;

    public TrainingMenu() {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        MihmandarApplication.get().getMihmandarEventbus().register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);


//        Component content = buildContent();
//        root.addComponent(content);
//        root.setExpandRatio(content, 1);

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutEvents.LayoutClickEvent event) {
                MihmandarApplication.get().getMihmandarEventbus().post(new MihmandarEvent.CloseOpenWindowsEvent());
            }
        });

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
