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
import tech.mihmandar.ui.presentation.view.ViewType;

import java.util.Collection;
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
    private TreeTable treeTable;
    private HierarchicalContainer container;
    private Long trainingId;
    private RichTextArea notes;

    private TabSheet tabSheet;
    private TabSheet.Tab adimlarTab;
    private TabSheet.Tab yapilacaklarTab;
    private TabSheet.Tab editorTab;

    private EnumProcessType processType;

    public MihmandarTrainingAddComponent(EnumProcessType processType) {
        this.processType = processType;
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);

        setSizeFull();
        MihmandarApplication.get().getMihmandarEventbus().register(this);

        Responsive.makeResponsive(mainLayout);

        headerComponent = new HeaderComponent();
        headerComponent.setSizeFull();
        headerComponent.setLanguageComboEnabled(false);
        mainLayout.addComponent(headerComponent);

        MihPanel fieldsPanel = createFieldsPanel(processType);
        mainLayout.addComponent(fieldsPanel);

        tabSheet = new TabSheet();
        tabSheet.setWidth(100, Unit.PERCENTAGE);
        tabSheet.setHeight(800, Unit.PIXELS);
        tabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                Component selectedTab = tabSheet.getSelectedTab();
                if(selectedTab != null && adimlarTab != null && !selectedTab.equals(adimlarTab.getComponent())){
                    Container.Hierarchical containerDataSource = treeTable.getContainerDataSource();
                    Collection<?> itemIds = containerDataSource.getItemIds();
                    TrainingStep selectedStep = null;
                    for (Object itemId : itemIds) {
                        CheckBox cb = (CheckBox) containerDataSource.getContainerProperty(itemId, "cb").getValue();
                        Boolean value = cb.getValue();
                        if(Boolean.TRUE.equals(value)){
                            selectedStep = (TrainingStep)itemId;
                        }
                    }
                    if(selectedStep == null){
                        tabSheet.setSelectedTab(adimlarTab);
                        UiUtil.displayNoSelectedRecordWarning();
                    }
                }
            }
        });
        mainLayout.addComponent(tabSheet);

        MihPanel treeTablePanel = createTreeTablePanel();
        adimlarTab = tabSheet.addTab(treeTablePanel, "Adım Sırası", FontAwesome.STEP_FORWARD);

        MihPanel descPanel = createDescPanel();
        yapilacaklarTab = tabSheet.addTab(descPanel, "Yapılacakların Tarifi", FontAwesome.INFO);

        MihPanel editorPanel = createEditorPanel();
        editorTab = tabSheet.addTab(editorPanel, "Editör", FontAwesome.PAINT_BRUSH);

        setCompositionRoot(mainLayout);
    }

    private MihPanel createFieldsPanel(EnumProcessType processType) {
        MihPanel fieldsPanel = new MihPanel(processType.toString() + " Detayları");
        fieldsPanel.setCollapsed(true);
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
        fieldsPanel.addMenuToHeader("Sil", FontAwesome.REMOVE, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                if(trainingId != null){
                    Training training = trainingService.findById(trainingId);
                    trainingService.delete(training);
                    MihmandarApplication.get().getNavigator().navigateTo(ViewType.MIHMANDAR_TRAINING.getName());
                }
            }
        });
        fieldsPanel.setSizeFull();

        trainingFieldsLayout = new VerticalLayout();
        trainingFieldsLayout.setSpacing(true);
        trainingFieldsLayout.setSizeFull();
        fieldsPanel.addComponent(trainingFieldsLayout);

        accessType = new OptionGroup();
        accessType.setImmediate(true);
        accessType.addItems(EnumAccessType.values());
        accessType.setDescription("Erişim Türü");
        accessType.addStyleName("horizontal");
        trainingFieldsLayout.addComponent(accessType);

        name = new TextField();
        name.setInputPrompt("İsim giriniz...");
        name.setImmediate(true);
        name.setSizeFull();
        trainingFieldsLayout.addComponent(name);

        description = new TextArea();
        description.setCaption("Açıklama");
        description.setImmediate(true);
        description.setInputPrompt("Açıklama");
        description.setSizeFull();
        trainingFieldsLayout.addComponent(description);

        trainingFieldsLayout.setExpandRatio(description, 5f);
        trainingFieldsLayout.setExpandRatio(name, 2f);
        trainingFieldsLayout.setExpandRatio(accessType, 1f);

        return fieldsPanel;
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

    private MihPanel createEditorPanel() {
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
                EnumSoftwareLanguages softLanguage = MihmandarApplication.get().getLanguage();
                UiUtil.compile(value, softLanguage);
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
        return panelEditor;
    }

    private MihPanel createDescPanel() {
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
        return descPanel;
    }

    private MihPanel createTreeTablePanel() {
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
        return treeTablePanel;
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