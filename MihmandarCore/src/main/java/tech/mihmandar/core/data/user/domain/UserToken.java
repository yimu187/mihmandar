package tech.mihmandar.core.data.user.domain;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import tech.mihmandar.core.common.entity.BaseEntity;
import tech.mihmandar.core.common.enums.EnumYN;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Murat on 9/14/2017.
 */
@Entity
@Audited
@Table(name = "USER_TOKEN")
public class UserToken extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "generator", sequenceName = "USER_TOKEN_ID_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column
    private Long id;

    @Version
    private long versiyon;

    @Index(name = "IX_USER_TOKEN_USER")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER")
    @ForeignKey(name = "FK_USER_TOKEN_USER")
    private User user;

    @Column(name = "TOKEN", length = 50)
    private String token;

    @Column(name = "TOKEN_REQEST_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenRequestTime;

    @Column
    private Long expirationMinutes;

    /**
     * User loggout esnasında Hayır yapılır
     */
    @Enumerated(EnumType.STRING)
    @Column
    private EnumYN state;

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersiyon() {
        return versiyon;
    }

    public void setVersiyon(long versiyon) {
        this.versiyon = versiyon;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpirationMinutes() {
        return expirationMinutes;
    }

    public void setExpirationMinutes(Long expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }

    public EnumYN getState() {
        return state;
    }

    public void setState(EnumYN state) {
        this.state = state;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return token;
    }

    public Date getTokenRequestTime() {
        return tokenRequestTime;
    }

    public void setTokenRequestTime(Date tokenRequestTime) {
        this.tokenRequestTime = tokenRequestTime;
    }
}
