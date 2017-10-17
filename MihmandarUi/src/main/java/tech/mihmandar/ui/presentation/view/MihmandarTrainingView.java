package tech.mihmandar.ui.presentation.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import tech.mihmandar.core.common.enums.EnumProcessType;
import tech.mihmandar.core.data.training.service.TrainingService;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.component.MihmandarTrainingAddComponent;
import tech.mihmandar.ui.presentation.component.MihmandarTrainingSearchComponent;

@SuppressWarnings("serial")
public final class MihmandarTrainingView extends CustomComponent implements View{

    @Autowired
    TrainingService trainingService;

    private VerticalLayout mainLayout;
    private MihmandarTrainingAddComponent addTrainingComponent;
    private MihmandarTrainingSearchComponent trainingSearchComponent;

    public MihmandarTrainingView() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        addTrainingComponent = new MihmandarTrainingAddComponent(EnumProcessType.TRAINING);
        addTrainingComponent.setSizeFull();

        trainingSearchComponent = new MihmandarTrainingSearchComponent(EnumProcessType.TRAINING);
        trainingSearchComponent.setSizeFull();

        setSizeFull();

        MihmandarApplication.get().getMihmandarEventbus().register(this);

        Responsive.makeResponsive(mainLayout);

        setCompositionRoot(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String parameters = event.getParameters();
        String trainingId = parameters;
        mainLayout.removeAllComponents();
        if(StringUtils.hasText(trainingId)){
            addTrainingComponent.fillWithTraingId(trainingId);
            mainLayout.addComponent(addTrainingComponent);
        }else{
            mainLayout.addComponent(new Label("Arama sayfası"));
        }


    }

}
