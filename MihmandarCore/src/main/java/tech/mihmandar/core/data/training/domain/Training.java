package tech.mihmandar.core.data.training.domain;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import tech.mihmandar.core.common.entity.BaseEntity;
import tech.mihmandar.core.common.enums.EnumAccessType;
import tech.mihmandar.core.common.enums.EnumProcessType;
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.core.data.user.domain.User;

import javax.persistence.*;

/**
 * Created by Murat on 10/8/2017.
 */
@Entity
@Audited
@Table(name = "TRAINING")
public class Training extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "generator", sequenceName = "TRAINING_ID_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column
    private Long id;

    @Version
    private long versiyon;

    @Index(name = "IX_TRAINING_USER")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER")
    @ForeignKey(name = "FK_TRAINING_USER")
    private User user;

    @Column(name = "NAME", length = 20)
    private String name;

    @Column(name = "DESCRIPTOION", length = 100)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, name = "LANGUAGE")
    private EnumSoftwareLanguages language;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, name = "PROCESS_TYPE")
    private EnumProcessType processType;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, name = "ACCESS_TYPE")
    private EnumAccessType accessType;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Long getId() {
        return id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EnumProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(EnumProcessType processType) {
        this.processType = processType;
    }

    public EnumSoftwareLanguages getLanguage() {
        return language;
    }

    public void setLanguage(EnumSoftwareLanguages language) {
        this.language = language;
    }

    public void setAccessType(EnumAccessType accessType) {
        this.accessType = accessType;
    }

    public EnumAccessType getAccessType() {
        return accessType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
