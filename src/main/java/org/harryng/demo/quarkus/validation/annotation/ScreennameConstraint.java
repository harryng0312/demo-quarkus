package org.harryng.demo.quarkus.validation.annotation;

import io.quarkus.qute.i18n.MessageBundles;
import org.harryng.demo.quarkus.i18n.I18nMessage;
import org.harryng.demo.quarkus.validation.validator.ScreennameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = ScreennameValidator.class)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ScreennameConstraint {
//    String message() default "User screenname validator doesn't match!";
    String message() default "{msg:error_screenname}";
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
