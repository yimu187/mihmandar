package tech.mihmandar.core.data.training.domain;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import tech.mihmandar.core.common.entity.BaseEntity;
import tech.mihmandar.core.data.training.service.TrainingService;
import tech.mihmandar.core.data.user.domain.User;

import javax.persistence.*;

/**
 * Created by Murat on 10/9/2017.
 */
@Entity
@Audited
@Table(name = "TRAINING_STEP_RELATION")
public class TraininStepRelation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "generator", sequenceName = "TRAINING_STEP_RELATION_ID_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column
    private Long id;

    @Version
    private long versiyon;

    @Index(name = "IX_TRAINING_RELATION_TRAINING")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAINING")
    @ForeignKey(name = "FK_TRAINING_RELATION_TRAINING")
    private Training training;


    @Index(name = "IX_TRAINING_STEP_RELATION")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAINING_STEP")
    @ForeignKey(name = "FK_TRAINING_STEP_RELATION")
    private TrainingStep trainingStep;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : null;
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

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public TrainingStep getTrainingStep() {
        return trainingStep;
    }

    public void setTrainingStep(TrainingStep trainingStep) {
        this.trainingStep = trainingStep;
    }
}
