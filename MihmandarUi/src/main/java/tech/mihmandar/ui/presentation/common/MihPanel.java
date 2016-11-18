package tech.mihmandar.ui.presentation.common;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.*;

import java.lang.reflect.Method;

/**
 * Created by MURAT YILMAZ on 11/5/2016.
 */
public class MihPanel extends Panel {


    private static final long serialVersionUID = 1L;

    private VerticalLayout mainLayout;
    private VerticalLayout contentLayout;
    private HorizontalLayout headerLayout;
    private HorizontalLayout headerExtensionLayout;
    private Component headerExtensionComponent;
    private Label headerLabel = new Label("");
    private Label headerHelperLabel = new Label("");

    private Button collapseExpandButton;
    private HorizontalLayout collapseExpandLayout;
    private HorizontalLayout showHideButtonLayout;
    private Label showHideButton;

    public boolean isCollapsed = false;

    public MihPanel() {
        super();
        this.setCaption(null);
        createFlexPanelContent();
    }

    private void createFlexPanelContent() {
        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(false);
        mainLayout.setMargin(false);
        mainLayout.setImmediate(true);
        mainLayout.setWidth(100, Unit.PERCENTAGE);
//        mainLayout.addStyleName(FlexCityTheme.FLEX_100PERCENT_WIDTH);

        headerLayout = new HorizontalLayout();

//        headerLabel.addStyleName(FlexBaseTheme.FLEX_GENERAL_PANEL_TITLE_LABEL);
//        headerHelperLabel.addStyleName(FlexBaseTheme.FLEX_GENERAL_PANEL_TITLE_HELPER_LABEL);
        headerLayout.setWidth(100, Unit.PERCENTAGE);
//        headerLayout.addStyleName(FlexCityTheme.FLEX_GENERAL_PANEL_TITLE);

        headerExtensionLayout = new HorizontalLayout();
//        headerExtensionLayout.addStyleName(FlexCityTheme.FLEX_GENERAL_PANEL_INSIDE_BUTTON);

        showHideButtonLayout = createShowHideLayout();

        collapseExpandLayout = createCollapseExpandLayout();

        headerLayout.addComponent(headerLabel);
        headerLayout.addComponent(headerHelperLabel);
        headerLayout.addComponent(headerExtensionLayout);
        headerLayout.addComponent(collapseExpandLayout);
        headerLayout.addComponent(showHideButtonLayout);

        contentLayout = new VerticalLayout();
//        contentLayout.addStyleName(FlexCityTheme.FLEX_GENERAL_PANEL_BOTTOM_AREA);

        mainLayout.addComponent(headerLayout);
        mainLayout.addComponent(contentLayout);

        this.setContent(mainLayout);

//        addStyleName(FlexCityTheme.FLEX_PANEL);
        setIcon(FontAwesome.TH_LARGE);
    }

    public MihPanel(String caption) {
        this();
        this.setCaption(caption);
    }

    public MihPanel(String caption, String captionHelper) {
        this();
        this.setCaption(caption);
        this.setHelperCaption(captionHelper);
    }

    public void setHelperCaption(String captionHelper) {
        this.headerHelperLabel.setValue(captionHelper);
    }

    @Override
    public void setCaption(String caption) {
        this.headerLabel.setValue(caption);
    }

    @Override
    public void setIcon(Resource icon) {
        headerLayout.setIcon(icon);
    }

    private HorizontalLayout createShowHideLayout() {

        showHideButtonLayout = new HorizontalLayout();
//        showHideButtonLayout.addStyleName(FlexCityTheme.FLEX_GENERAL_PANEL_SHOW_HIDE);
        showHideButtonLayout.addLayoutClickListener(new ShowHideLayoutClickListener());

        showHideButton = new Label();
        showHideButton.setCaption(null);

        showHideButtonLayout.addComponent(showHideButton);

        setShowHideVisible(true);
        return showHideButtonLayout;
    }

    private HorizontalLayout createCollapseExpandLayout(){
        collapseExpandLayout = new HorizontalLayout();
//        collapseExpandLayout.addStyleName(FlexBaseTheme.FLEX_GENERAL_PANEL_SHOW_HIDE_BUTTON);

        collapseExpandButton = new Button();
        collapseExpandButton.setIcon(FontAwesome.EXPAND);
        collapseExpandButton.addClickListener(new EkraniKaplaButtonClickListener());

        collapseExpandLayout.addComponent(collapseExpandButton);

        return collapseExpandLayout;
    }

    private class EkraniKaplaButtonClickListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {

            setCollapsed(false);
//            ViewUtil.changeFullScreenComponentWithStyleName(collapseExpandButton, FlexPanel.this, "f-fullPanel");
//            ViewUtil.executeVaadinForceSync();
        }
    }

    public void addComponent(Component c) {
        contentLayout.addComponent(c);
    }

    public void removeAllComponents() {
        contentLayout.removeAllComponents();
    }

    public boolean getCollapsed() {
        return isCollapsed;
    }

    public void setCollapsed(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
        setGosterGizleButtonState(isCollapsed);
        contentLayout.setVisible(!isCollapsed);
        if(flexPanelCollapsedEventListener!=null) {
            flexPanelCollapsedEventListener.panelCollapsed(new FlexPanelCollapsedEvent(this, isCollapsed));
        }
    }

    public void setFullSizeVisible(boolean isVisible) {
        collapseExpandButton.setVisible(isVisible);
    }

    public void setShowHideVisible(boolean isVisible) {
        showHideButton.setVisible(isVisible);
        setGosterGizleButtonState(this.isCollapsed);
    }

    private void setGosterGizleButtonState(boolean isCollapsedPrm) {

        if (!isCollapsedPrm) {
            showHideButton.setDescription("Gizle");
            showHideButton.setIcon(FontAwesome.CARET_UP);
        } else {
            showHideButton.setDescription("Göster");
            showHideButton.setIcon(FontAwesome.CARET_DOWN);
        }
    }

    public void addStyleNameToPanel(String style) {
        addStyleName(style);
    }

    public void addHeaderComponent(Component component) {
        if(this.headerExtensionLayout!=null)
            headerExtensionLayout.removeComponent(component);
        this.headerExtensionLayout.addComponent(component);
        this.headerExtensionComponent = component;
        //this.headerExtensionComponent.addStyleName(FlexCityTheme.FLEX_GENERAL_PANEL_BUTTON_AREA);
    }

    private class ShowHideLayoutClickListener implements LayoutEvents.LayoutClickListener {
        @Override
        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
            if (event.getButton() != MouseEventDetails.MouseButton.LEFT) {
                return;
            }
            setCollapsed(!isCollapsed);
        }
    }

    private static final Method COLLAPSED_EVENT;

    static {
        try {
            COLLAPSED_EVENT = FlexPanelCollapsedEventListener.class.getDeclaredMethod("panelCollapsed", new Class[]{FlexPanelCollapsedEvent.class});
        } catch (final java.lang.NoSuchMethodException e) {
            throw new java.lang.RuntimeException("Internal error, buttonClicked method not found");
        }
    }

    private FlexPanelCollapsedEventListener flexPanelCollapsedEventListener;

    public interface FlexPanelCollapsedEventListener {
        public void panelCollapsed(FlexPanelCollapsedEvent event);
    }

    public class FlexPanelCollapsedEvent extends Event{
        private boolean isCollapsed;

        public FlexPanelCollapsedEvent(Panel flexPanel,boolean isCollapsedPrm) {
            super(flexPanel);
            this.isCollapsed = isCollapsedPrm;
        }

        public boolean isCollapsed() {
            return isCollapsed;
        }
    }

    public void addFlexPanelCollapsedEventListener(FlexPanelCollapsedEventListener listener) {
        flexPanelCollapsedEventListener = listener;
        addListener(FlexPanelCollapsedEvent.class, listener, COLLAPSED_EVENT);
    }

}
