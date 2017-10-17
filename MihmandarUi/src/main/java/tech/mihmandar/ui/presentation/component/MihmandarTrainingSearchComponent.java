package tech.mihmandar.ui.presentation.component;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import tech.mihmandar.core.common.enums.EnumProcessType;
import tech.mihmandar.core.data.training.service.TrainingService;
import tech.mihmandar.ui.presentation.common.MihPanel;

/**
 * Created by Murat on 10/17/2017.
 */
public class MihmandarTrainingSearchComponent extends CustomComponent {

    @Autowired
    TrainingService trainingService;

    private VerticalLayout mainLayout;
    private EnumProcessType processType;
    private MihPanel panel;

    public MihmandarTrainingSearchComponent(EnumProcessType processType) {
        this.processType = processType;
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);

        panel = new MihPanel("Eğitimler");
        panel.setSizeFull();
        mainLayout.addComponent(panel);

        setSizeFull();

        setCompositionRoot(mainLayout);


    }
}
