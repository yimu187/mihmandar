package tech.mihmandar.ui.presentation.view;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import tech.mihmandar.ui.presentation.common.DashboardNavigator;

/**
 * Created by Murat on 8/31/2017.
 */
public class MainView  extends HorizontalLayout{

    public MainView() {
        setSizeFull();
        addStyleName("mainview");

        addComponent(new MihmandarMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new DashboardNavigator(content);
    }

}
