package tech.mihmandar.ui.presentation.component;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;
import sun.plugin.util.UIUtil;
import tech.mihmandar.core.common.enums.EnumAccessType;
import tech.mihmandar.core.common.enums.EnumProcessType;
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.core.data.training.domain.TraininStepRelation;
import tech.mihmandar.core.data.training.domain.Training;
import tech.mihmandar.core.data.training.domain.TrainingStep;
import tech.mihmandar.core.data.training.service.TraininStepRelationService;
import tech.mihmandar.core.data.training.service.TrainingService;
import tech.mihmandar.core.data.training.service.TrainingStepService;
import tech.mihmandar.ui.presentation.common.MihPanel;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;
import tech.mihmandar.ui.presentation.event.MihmandarEvent;
import tech.mihmandar.ui.presentation.util.UiUtil;
import tech.mihmandar.utility.compiler.JavaCompilerUtil;
import tech.mihmandar.utility.dto.CompileResultDto;

import java.util.List;

/**
 * Created by Murat on 10/17/2017.
 */
public class MihmandarTrainingAddComponent extends CustomComponent {

    @Autowired
    TrainingService trainingService;

    @Autowired
    TraininStepRelationService traininStepRelationService;

    @Autowired
    TrainingStepService trainingStepService;

    private VerticalLayout mainLayout;
    private HeaderComponent headerComponent;
    private VerticalLayout trainingFieldsLayout;
    private TextField name;
    private TextArea description;
    private OptionGroup accessType;
    private AceEditor aceEditor;
    private HorizontalLayout content;
    private VerticalLayout contentV;
    private TreeTable treeTable;
    private HierarchicalContainer container;
    private Long trainingId;
    private RichTextArea notes;

    private EnumProcessType processType;

    public MihmandarTrainingAddComponent(EnumProcessType processType) {
        this.processType = processType;
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setSizeFull();

        setSizeFull();
        MihmandarApplication.get().getMihmandarEventbus().register(this);

        Responsive.makeResponsive(mainLayout);

        int browserWindowWidth = MihmandarApplication.get().getPage().getBrowserWindowWidth();
        buildContentByWidth(browserWindowWidth);

        setCompositionRoot(mainLayout);
    }

    private void buildContentByWidth(int browserWindowWidth) {

        headerComponent = new HeaderComponent();
        headerComponent.setSizeFull();
        headerComponent.setLanguageComboEnabled(false);

        MihPanel fieldsPanel = new MihPanel("Detaylar");
        fieldsPanel.addMenuToHeader("Kaydet", FontAwesome.SAVE, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                if(trainingId != null){
                    Training training = trainingService.findById(trainingId);
                    training.setAccessType((EnumAccessType) accessType.getValue());
                    training.setName(name.getValue());
                    training.setDescription(description.getValue());
                    trainingService.save(training);
                    UiUtil.displaySuccessNotification();
                }
            }
        });
        fieldsPanel.setSizeFull();

        mainLayout.removeAllComponents();
        if(browserWindowWidth >= 700){
            mainLayout.addComponent(headerComponent);

            generateTrainingFieldsLayout(false);
            fieldsPanel.addComponent(trainingFieldsLayout);
            mainLayout.addComponent(fieldsPanel);

            content = new HorizontalLayout();
            content = (HorizontalLayout) buildContent(content);
            mainLayout.addComponent(content);

            mainLayout.setExpandRatio(headerComponent, 1);
            mainLayout.setExpandRatio(fieldsPanel, 3f);
            mainLayout.setExpandRatio(content, 10f);
        }else{
            mainLayout.addComponent(headerComponent);

            generateTrainingFieldsLayout(true);
            fieldsPanel.addComponent(trainingFieldsLayout);
            mainLayout.addComponent(fieldsPanel);

            contentV = new VerticalLayout();
            contentV = (VerticalLayout) buildContent(contentV);
            mainLayout.addComponent(contentV);

            mainLayout.setExpandRatio(headerComponent, 1);
            mainLayout.setExpandRatio(fieldsPanel, 3f);
            mainLayout.setExpandRatio(contentV, 10);
        }
    }

    private void generateTrainingFieldsLayout(boolean smallSize) {

        trainingFieldsLayout = new VerticalLayout();
        trainingFieldsLayout.setSpacing(true);
        trainingFieldsLayout.setSizeFull();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.setSpacing(true);
        trainingFieldsLayout.addComponent(horizontalLayout);

        if(accessType == null){
            accessType = new OptionGroup();
        }
        accessType.setImmediate(true);
        accessType.addItems(EnumAccessType.values());
        accessType.setCaption("Erişim Türü");
        accessType.addStyleName("horizontal");
        FormLayout optGrupLayout = new FormLayout();
        optGrupLayout.addComponent(accessType);
        if(!smallSize){
            horizontalLayout.addComponent(optGrupLayout);
        }else{
            trainingFieldsLayout.addComponent(optGrupLayout);
        }

        if(name == null){
            name = new TextField("Adı");
        }
        name.setImmediate(true);
        name.setSizeFull();
        FormLayout nameLayout = new FormLayout();
        nameLayout.addComponent(name);
        if(!smallSize){
            horizontalLayout.addComponent(nameLayout);
        }else{
            trainingFieldsLayout.addComponent(nameLayout);
        }

        if(description == null){
            description = new TextArea();
        }
        description.setImmediate(true);
        description.setInputPrompt("Açıklama");
        description.setSizeFull();
        trainingFieldsLayout.addComponent(description);
    }

    private AbstractOrderedLayout buildContent(AbstractOrderedLayout content) {
        content.setSizeFull();
        content.setSpacing(true);

        MihPanel treeTablePanel = new MihPanel("Adımlar");
        treeTable = new TreeTable();

        treeTable.setSelectable(false);
        treeTable.setMultiSelect(false);
        treeTable.setImmediate(true);
        treeTable.setNullSelectionAllowed(false);
        treeTable.setNullSelectionItemId(false);
        treeTable.setColumnReorderingAllowed(false);
        treeTable.setColumnCollapsingAllowed(false);
        treeTable.setSortEnabled(false);
        treeTable.setSizeFull();
        container = createContainer();
        treeTable.setColumnHeader("cb", "");
        treeTable.setColumnHeader("sira", "SIRA");
        treeTable.setColumnHeader("adi", "ADI");
        treeTable.setColumnExpandRatio("cb", 1f);
        treeTable.setColumnExpandRatio("sira", 2f);
        treeTable.setColumnExpandRatio("adi", 5f);
        treeTable.setContainerDataSource(container);
        fillTreeTable();

        treeTablePanel.addComponent(treeTable);
        treeTablePanel.setHeight(100, Unit.PERCENTAGE);
        treeTablePanel.addMenuToHeader("Adım Ekle", FontAwesome.PLUS, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Window adımEkleWindow = new Window("Adım Ekle");

                adımEkleWindow.center();
                adımEkleWindow.setWidth(40, Unit.PERCENTAGE);
                adımEkleWindow.setHeight(40, Unit.PERCENTAGE);

                VerticalLayout layout = new VerticalLayout();
                layout.setSizeFull();
                layout.setSpacing(true);

                ComboBox ustAdim = new ComboBox("Üst Adım");
                ustAdim.setSizeFull();
                if(trainingId != null){
                    List<TrainingStep> trainingSteps = traininStepRelationService.findAllTraininStepRelationByTrainingId(trainingId);
                    ustAdim.addItems(trainingSteps);
                }
                layout.addComponent(ustAdim);

                TextField adimSirasi = new TextField("Adım Sırası");
                adimSirasi.addValidator(new NullValidator("Lütfen Adım Adı giriniz",false));
                adimSirasi.setInputPrompt("Adım sırası giriniz");
                adimSirasi.setSizeFull();
                layout.addComponent(adimSirasi);

                TextField adimAdi = new TextField("Adım Adı");
                adimAdi.addValidator(new NullValidator("Lütfen Adım Adı giriniz",false));
                adimAdi.setInputPrompt("Adım adı giriniz");
                adimAdi.setSizeFull();
                layout.addComponent(adimAdi);

                VerticalLayout buttomLayout = new VerticalLayout();
                buttomLayout.setSizeFull();
                buttomLayout.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
                HorizontalLayout buttonsLayout = new HorizontalLayout();
                buttonsLayout.setSpacing(true);

                Button btnOk = new Button("Devam");
                btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
                btnOk.addStyleName(ValoTheme.BUTTON_SMALL);
                btnOk.addClickListener((Button.ClickListener) event1 -> {
                    TrainingStep trainingStep = new TrainingStep();
                    trainingStep.setStepNo(adimSirasi.getValue());
                    trainingStep.setStepName(adimAdi.getValue());

                    trainingStep = trainingStepService.save(trainingStep);

                    Training training = trainingService.findById(trainingId);

                    TraininStepRelation relation = new TraininStepRelation();
                    relation.setTraining(training);
                    relation.setTrainingStep(trainingStep);

                    traininStepRelationService.save(relation);
                    fillTreeTable();
                    UiUtil.displaySuccessNotification();
                    MihmandarApplication.get().removeWindow(adımEkleWindow);
                });
                buttonsLayout.addComponent(btnOk);

                Button btnCancel = new Button("İptal");
                btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
                btnCancel.addStyleName(ValoTheme.BUTTON_SMALL);
                btnCancel.addClickListener((Button.ClickListener) event1 -> {
                    MihmandarApplication.get().removeWindow(adımEkleWindow);
                });
                buttonsLayout.addComponentAsFirst(btnCancel);

                buttomLayout.addComponent(buttonsLayout);
                buttomLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);

                layout.addComponent(buttomLayout);
                layout.setComponentAlignment(buttomLayout, Alignment.BOTTOM_RIGHT);

                adımEkleWindow.setContent(layout);

                MihmandarApplication.get().addWindow(adımEkleWindow);

            }
        });
        content.addComponent(treeTablePanel);

        MihPanel descPanel = new MihPanel("Yapılacakların Tarifi");
        descPanel.setHeight(100, Unit.PERCENTAGE);
        notes = new RichTextArea();
        notes.setValue("");
        notes.setSizeFull();
        descPanel.addComponent(notes);
        descPanel.addMenuToHeader("Kaydet", FontAwesome.SAVE, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String value = notes.getValue();
                TrainingStep trainingStep = getTrainingStep();
                if(trainingStep != null){
                    TrainingStep step = trainingStepService.findById(trainingStep.getId());
                    step.setCodeExplaination(value);
                    trainingStepService.save(step);
                    UiUtil.displaySuccessNotification();
                }
            }
        });
        content.addComponent(descPanel);


        aceEditor = new AceEditor();
        AceMode aceMode = AceMode.java;
        aceEditor.setMode(aceMode);
        aceEditor.setSizeFull();
        aceEditor.setTheme(AceTheme.terminal);
        MihPanel panelEditor = new MihPanel("Editör");
        panelEditor.setHeight(100, Unit.PERCENTAGE);
        panelEditor.addStyleName("editor");
        panelEditor.addComponent(aceEditor);
        panelEditor.addMenuToHeader("Derle", FontAwesome.PAINT_BRUSH, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String value = aceEditor.getValue();
                UiUtil.doCompile(value);
            }
        });
        panelEditor.addMenuToHeader("Kaydet", FontAwesome.SAVE, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String value = aceEditor.getValue();
                TrainingStep trainingStep = getTrainingStep();
                if(trainingStep != null){
                    TrainingStep step = trainingStepService.findById(trainingStep.getId());
                    step.setInitialCode(value);
                    trainingStepService.save(step);
                    UiUtil.displaySuccessNotification();
                }
            }
        });
        content.addComponent(panelEditor);

        content.setExpandRatio(descPanel, 2f);
        content.setExpandRatio(panelEditor, 2f);
        content.setExpandRatio(treeTablePanel, 1f);

        return content;
    }

    private TrainingStep getTrainingStep() {
        Container.Hierarchical containerDataSource = treeTable.getContainerDataSource();
        TrainingStep step = null;
        for (Object o : containerDataSource.getItemIds()) {
            CheckBox cb = (CheckBox) containerDataSource.getContainerProperty(o, "cb").getValue();
            Boolean value = cb.getValue();
            if(Boolean.TRUE.equals(value)){
                step = (TrainingStep)cb.getData();
                break;
            }
        }

        return step;
    }

    private void fillTreeTable() {
        treeTable.removeAllItems();
        HierarchicalContainer container = createContainer();
        if(trainingId != null){
            List<TrainingStep> stepList = traininStepRelationService.findAllTraininStepRelationByTrainingId(trainingId);
            for (TrainingStep trainingStep : stepList) {
                container.addItem(trainingStep);

                CheckBox cb = new CheckBox();
                cb.setData(trainingStep);
                cb.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        Object value = event.getProperty().getValue();
                        Object dataTrainingStep = cb.getData();
                        updateCheckBoxes(value, dataTrainingStep);
                    }
                });

                container.getContainerProperty(trainingStep, "cb").setValue(cb);
                container.getContainerProperty(trainingStep, "sira").setValue(trainingStep.getStepNo());
                container.getContainerProperty(trainingStep, "adi").setValue(trainingStep.getStepName());
            }
        }

        treeTable.setContainerDataSource(container);
    }

    private void updateCheckBoxes(Object value, Object dataTrainingStep) {
        Container.Hierarchical containerDataSource = treeTable.getContainerDataSource();
        if(Boolean.TRUE.equals(value)){
            for (Object o : containerDataSource.getItemIds()) {
                if(dataTrainingStep.equals(o)){
                    continue;
                }

                CheckBox cbInContainer = (CheckBox) container.getContainerProperty(o, "cb").getValue();
                cbInContainer.setValue(Boolean.FALSE);
            }

            TrainingStep step = (TrainingStep) dataTrainingStep;
            step = trainingStepService.findById(step.getId());

            String codeExplaination = StringUtils.hasText(step.getCodeExplaination()) ? step.getCodeExplaination() : "";
            notes.setValue(codeExplaination);

            String initialCode = StringUtils.hasText(step.getInitialCode()) ? step.getInitialCode() : "";
            aceEditor.setValue(initialCode);
        }
    }

    private HierarchicalContainer createContainer() {
        container = new HierarchicalContainer();
        container.addContainerProperty("cb", CheckBox.class, null);
        container.addContainerProperty("sira", String.class, null);
        container.addContainerProperty("adi", String.class, null);
        return container;
    }

    @Subscribe
    public void resize(final MihmandarEvent.BrowserResizeEvent event) {
        int width = event.getWidth();
        buildContentByWidth(width);
    }

    public void fillWithTraingId(String trainingIdParam) {
        long traininIdParamAsLong = Long.parseLong(trainingIdParam);
        Training training = trainingService.findById(traininIdParamAsLong);
        EnumSoftwareLanguages language = MihmandarApplication.get().getLanguage();
        if(training != null || (training != null && training.getLanguage().equals(language))){
            accessType.setValue(training.getAccessType());
            name.setValue(training.getName());
            description.setValue(training.getDescription());
            trainingId = training.getId();
        }else{
            accessType.setValue(null);
            name.setValue("");
            description.setValue("");
            trainingId = null;
        }
        fillTreeTable();
    }
}
