package tech.mihmandar.core.data.user.domain;

import org.hibernate.envers.Audited;
import tech.mihmandar.core.common.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by Murat on 9/11/2017.
 */
@Entity
@Audited
@Table(name = "USER")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "generator", sequenceName = "USER_ID_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column
    private Long id;

    @Version
    private long versiyon;

    @Column(name = "ROLE", length = 20)
    private String role;

    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Column(name = "LAST_NAME", length = 50)
    private String lastName;

    @Column(name = "TITLE", length = 50)
    private String title;

    @Column(name = "GENDER", length = 10)
    private boolean gender;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "LOCATION", length = 4000)
    private String location;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Column(name = "WEBSITE", length = 20)
    private String website;

    @Column(name = "BIO", length = 4000)
    private String bio;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
