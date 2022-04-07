package org.harryng.demo.quarkus.user.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@XmlRootElement
//@XmlAccessorType(XmlAccessType.PROPERTY)
public class UserImpl extends UserModel {

    public UserImpl(){super();}

    public UserImpl(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String status,
                    String username, String password, String screenName, LocalDate dob, String passwdEncryptedMethod) {
        super(id, createdDate, modifiedDate, status, username, password, screenName, dob, passwdEncryptedMethod);
    }
}
