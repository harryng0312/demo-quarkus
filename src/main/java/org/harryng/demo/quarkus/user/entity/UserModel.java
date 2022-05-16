package org.harryng.demo.quarkus.user.entity;

import org.harryng.demo.quarkus.base.entity.AbstractStatedEntity;
import org.harryng.demo.quarkus.validation.annotation.ScreennameConstraint;

import java.time.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class UserModel extends AbstractStatedEntity<Long> {
    private String username = "";
    private String password = "";
    private String screenName = "";
    private LocalDate dob = LocalDate.MIN;
    private String passwdEncryptedMethod = "plain";

    public UserModel() {
        super();
    }

    public UserModel(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String status,
                     String username, String password, String screenName, LocalDate dob, String passwdEncryptedMethod) {
        super(id, createdDate, modifiedDate, status);
        this.username = username;
        this.password = password;
        this.screenName = screenName;
        this.dob = dob;
        this.passwdEncryptedMethod = passwdEncryptedMethod;
    }

    @Basic
    @Column(name = "username")
    @NotBlank(message = "{msg:error_username_notblank}")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password_")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "screenname")
//    @ScreennameConstraint
    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Basic
    @Column(name = "dob")
    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Basic
    @Column(name = "passwd_encrypted_method")
    public String getPasswdEncryptedMethod() {
        return passwdEncryptedMethod;
    }

    public void setPasswdEncryptedMethod(String passwdEncryptedMethod) {
        this.passwdEncryptedMethod = passwdEncryptedMethod;
    }
}
