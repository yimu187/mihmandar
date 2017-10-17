package tech.mihmandar.core.data.training.domain;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import tech.mihmandar.core.common.entity.BaseEntity;
import tech.mihmandar.core.data.user.domain.User;

import javax.persistence.*;

/**
 * Created by Murat on 10/8/2017.
 */
@Entity
@Audited
@Table(name = "TRAINING_STEP")
public class TrainingStep extends BaseEntity {

    @SequenceGenerator(name = "generator", sequenceName = "TRAINING_STEP_ID_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column
    private Long id;

    @Version
    private long versiyon;

    @Column(name = "STEP_NAME", length = 20)
    private String stepName;

    @Column(name = "STEP_DESCRITION", length = 100)
    private String stepDescription;

    @Column(name = "STEP_NO", length = 100)
    private String stepNo;

    @Lob
    @Column(name="CODE_EXPLAINATION")
    private String codeExplaination;


    @Lob
    @Column(name="INITIAL_CODE")
    private String initialCode;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return stepName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersiyon() {
        return versiyon;
    }

    public void setVersiyon(long versiyon) {
        this.versiyon = versiyon;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getStepNo() {
        return stepNo;
    }

    public void setStepNo(String stepNo) {
        this.stepNo = stepNo;
    }

    public String getCodeExplaination() {
        return codeExplaination;
    }

    public void setCodeExplaination(String codeExplaination) {
        this.codeExplaination = codeExplaination;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }
}
