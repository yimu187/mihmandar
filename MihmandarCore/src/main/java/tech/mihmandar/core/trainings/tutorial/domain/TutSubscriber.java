package tech.mihmandar.core.trainings.tutorial.domain;

import org.hibernate.envers.Audited;
import tech.mihmandar.core.common.entity.BaseEntity;
import tech.mihmandar.core.trainings.tutorial.enums.EnumTutAccountType;

import javax.persistence.*;

/**
 * Created by MURAT YILMAZ on 10/31/2016.
 */
@Entity
@Audited
@Table(name = "TUT_SUBSCRIBER")
public class TutSubscriber extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "generator", sequenceName = "TUT_SUBSCRIBER_ID_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column
    private Long id;

    @Version
    private long versiyon;

    @Column(name = "USERNAME", length =  50)
    private String username;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "PASSWORD", length = 200)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE", length = 30)
    private EnumTutAccountType accountType;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EnumTutAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(EnumTutAccountType accountType) {
        this.accountType = accountType;
    }

    public String toString() {
        return username;
    }
}
