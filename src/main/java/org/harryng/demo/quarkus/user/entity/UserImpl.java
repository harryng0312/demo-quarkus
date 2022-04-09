package org.harryng.demo.quarkus.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="user_")
public class UserImpl extends UserModel {

    public UserImpl(){super();}

    public UserImpl(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String status,
                    String username, String password, String screenName, LocalDate dob, String passwdEncryptedMethod) {
        super(id, createdDate, modifiedDate, status, username, password, screenName, dob, passwdEncryptedMethod);
    }
}
