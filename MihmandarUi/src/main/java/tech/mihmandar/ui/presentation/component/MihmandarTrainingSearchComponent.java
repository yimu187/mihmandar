package tech.mihmandar.ui.presentation.component;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.vaadin.alump.lazylayouts.LazyComponentProvider;
import org.vaadin.alump.lazylayouts.LazyComponentRequestEvent;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;
import tech.mihmandar.core.common.dto.UserDto;
import tech.mihmandar.core.common.enums.EnumProcessType;
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.core.data.training.domain.Training;
import tech.mihmandar.core.data.training.service.TrainingService;
import tech.mihmandar.ui.presentation.common.MihPanel;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Murat on 10/17/2017.
 */
public class MihmandarTrainingSearchComponent extends CustomComponent {

    @Autowired
    TrainingService trainingService;

    private EnumProcessType processType;
    private Panel panel;
    private VerticalLayout mainLayout;
    private LazyVerticalLayout lazyLayout;
    private TextField searchText;
    private final int DEFAULT_PANEL_SIZE = 5;
    private final int INTERVAL = 2;
    private List<MihPanel> panelList = new ArrayList<>();

    public MihmandarTrainingSearchComponent(EnumProcessType processType) {
        this.processType = processType;
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        MihmandarApplication.get().getMihmandarEventbus().register(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);

        panel = new Panel();
        panel.setSizeFull();
        mainLayout.addComponent(panel);

        lazyLayout = new LazyVerticalLayout();
        lazyLayout.setSpacing(true);

        setSizeFull();

        buildLazyLayout();

        Responsive.makeResponsive(mainLayout);

        setCompositionRoot(mainLayout);
    }

    private void buildLazyLayout() {

        panelList.clear();

        searchText = new TextField();
        searchText.setSizeFull();
        searchText.setInputPrompt("Arama bilgisi giriniz...");
        searchText.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                fillLazyLayout();
            }
        });

        fillLazyLayout();

        panel.setContent(lazyLayout);
    }

    private void fillLazyLayout() {
        lazyLayout.removeAllComponents();

        lazyLayout.addComponent(searchText);

        UserDto userDto = MihmandarApplication.get().getUserDto();
        Long userId = userDto.getUserId();
        String searchTextValue = searchText.getValue();
        List<Training> trainingList = trainingService.findAllAccessibleTrainingByUserIdSearchTextStartIndexAndSize(userId, searchTextValue, 0, DEFAULT_PANEL_SIZE);

        addTrainingsToLazyLayout(trainingList);

        lazyLayout.enableLazyLoading(new LazyComponentProvider() {
            @Override
            public void onLazyComponentRequest(LazyComponentRequestEvent lazyComponentRequestEvent) {
                int totatlSize = panelList.size();
                String searchTextValue = searchText.getValue();
                List<Training> trainingList = trainingService.findAllAccessibleTrainingByUserIdSearchTextStartIndexAndSize(userId, searchTextValue, totatlSize, INTERVAL);
                if(!trainingList.isEmpty()){
                    addTrainingsToLazyLayout(trainingList);
                }else{
                    lazyComponentRequestEvent.getComponentContainer().disableLazyLoading();
                }
            }
        });
    }

    private void addTrainingsToLazyLayout(List<Training> trainingList) {
        for (Training training : trainingList) {
            MihPanel panel = generateTrainingPanel(training);
            panel.setData(training);
            lazyLayout.addComponent(panel);
            panelList.add(panel);
        }
    }

    private MihPanel generateTrainingPanel(Training training) {
        MihPanel panel = new MihPanel();
        panel.setSizeFull();

        EnumSoftwareLanguages language = training.getLanguage();
        String languageAsStr = language != null ? language.toString() : "Java";
        panel.setCaption(languageAsStr);
        VerticalLayout panelLayout = new VerticalLayout();
        panelLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if(event.isDoubleClick()){
                    Notification.show("Eğitime Başlanacak Emin misiniz ?", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        panelLayout.setSizeFull();
        panelLayout.setSpacing(true);

        Label name = new Label(training.getName());
        name.addStyleName(ValoTheme.LABEL_BOLD);

        panelLayout.addComponent(name);

        Label descLabel = new Label(training.getDescription());
        panelLayout.addComponent(descLabel);

        int browserWindowWidth = MihmandarApplication.get().getPage().getBrowserWindowWidth();
        if(browserWindowWidth > 900){
            name.addStyleName(ValoTheme.LABEL_H1);
            descLabel.addStyleName(ValoTheme.LABEL_H2);
        }else{
            name.addStyleName(ValoTheme.LABEL_H3);
            descLabel.addStyleName(ValoTheme.LABEL_H4);
        }

        UserDto userDto = MihmandarApplication.get().getUserDto();
        if(training.getUser() != null && training.getUser().getId().equals(userDto.getUserId())){
            panel.addMenuToHeader("Güncelle", FontAwesome.PENCIL, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    String navState = "Egitim/"+training.getId();
                    MihmandarApplication.get().getNavigator().navigateTo(navState);
                }
            });
        }

        panel.addMenuToHeader("Başla", FontAwesome.ARROW_RIGHT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Notification.show("Eğitime Başlanacak Emin misiniz ?", Notification.Type.HUMANIZED_MESSAGE);
            }
        });
        panel.addComponent(panelLayout);

        return panel;
    }

    @Subscribe
    public void resize(final MihmandarEvent.BrowserResizeEvent event) {
        buildLazyLayout();
    }

}
