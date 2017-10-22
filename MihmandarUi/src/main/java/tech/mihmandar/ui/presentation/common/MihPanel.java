package tech.mihmandar.ui.presentation.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by MURAT YILMAZ on 11/5/2016.
 */
public class MihPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private VerticalLayout panelLayout;
    private VerticalLayout content;
    private HorizontalLayout toolbar;
    private Label captionLabel;
    private MenuBar tools;
    private MenuBar.MenuItem max;
    private MenuBar.MenuItem showHide;

    public MihPanel(String captionValue) {

        setSizeFull();

        panelLayout = new VerticalLayout();
        panelLayout.setSizeFull();

        content = new VerticalLayout();
        content.addStyleName("panel-content");
        content.setSizeFull();

        toolbar = new HorizontalLayout();
        toolbar.addStyleName("panel-toolbar");
        toolbar.setWidth("100%");
        captionLabel = new Label(captionValue);

        captionLabel.addStyleName(ValoTheme.LABEL_H4);
        captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
        captionLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

        max = tools.addItem("", FontAwesome.EXPAND, (MenuBar.Command) selectedItem -> {
            if (!panelLayout.getStyleName().contains("full")) {
                selectedItem.setIcon(FontAwesome.COMPRESS);
                toggleMaximized(panelLayout, true);
            } else {
                panelLayout.removeStyleName("full");
                selectedItem.setIcon(FontAwesome.EXPAND);
                toggleMaximized(panelLayout, false);
            }
        });

        max.setStyleName("icon-only");
        max.setDescription("Tam Ekran Yap / Küçült");
        showHide = tools.addItem("", FontAwesome.CARET_DOWN, (MenuBar.Command) selectedItem -> {
            doPanelShowHideAction();
        });

        showHide.setDescription("Göster / Gizle");
        toolbar.addComponents(captionLabel, tools);
        toolbar.setExpandRatio(captionLabel, 1);
        toolbar.setComponentAlignment(captionLabel, Alignment.MIDDLE_LEFT);


        panelLayout.addComponent(toolbar);
        panelLayout.addComponent(content);

        panelLayout.setExpandRatio(toolbar, 1f);
        panelLayout.setExpandRatio(content, 15f);

        this.setContent(panelLayout);
    }

    public MihPanel() {
        this("");
    }

    @Override
    public void setCaption(String caption) {
        captionLabel.setValue(caption);
    }

    public void doPanelShowHideAction(){
        boolean visible = content.isVisible();
        panelLayout.removeAllComponents();
        setCollapsed(visible);
    }

    public void addMenuToHeader(String description, Resource icon, MenuBar.Command command){
        MenuBar.MenuItem menuItem = tools.addItemBefore("", icon, command, max);
        menuItem.setStyleName("icon-only");
        menuItem.setDescription(description);
    }

    public void addComponent(Component component){
        content.addComponent(component);
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("full");
        } else {
            panel.removeStyleName("full");
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);
        if(content != null){
            content.setHeight(height, unit);
        }
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        if(content != null){
            content.setHeight(height);
        }
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);
        if(content != null){
            content.setWidth(width, unit);
        }
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        if(content != null){
            content.setWidth(width);
        }
    }

    public void setCollapsed(boolean collapsed){
        if(collapsed){
            content.setVisible(false);
            showHide.setIcon(FontAwesome.CARET_UP);

            panelLayout.addComponent(toolbar);
        }else{
            content.setVisible(true);
            showHide.setIcon(FontAwesome.CARET_DOWN);

            panelLayout.addComponent(toolbar);
            panelLayout.addComponent(content);

            panelLayout.setExpandRatio(toolbar, 1f);
            panelLayout.setExpandRatio(content, 15f);
        }
    }
}
