package org.harryng.demo.quarkus.user.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_")
//@EditUserContraint(message = "[Screenname]{msg:error_screenname}", groups = {EditUserContraint.class})
public class UserImpl extends UserModel {

    public UserImpl() {
        super();
    }

    public UserImpl(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String status,
                    String username, String password, String screenName, LocalDate dob, String passwdEncryptedMethod) {
        super(id, createdDate, modifiedDate, status, username, password, screenName, dob, passwdEncryptedMethod);
    }
}
