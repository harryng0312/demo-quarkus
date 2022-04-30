package org.harryng.demo.quarkus.validation.annotation;

import org.harryng.demo.quarkus.validation.validator.UserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserConstraint {
    String message() default "User validator doesn't match!";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
//    String field();
//
//    String fieldMatch();

//    @Target({ ElementType.TYPE })
//    @Retention(RetentionPolicy.RUNTIME)
//    @interface List {
//        UserConstraint[] value();
//    }
}
