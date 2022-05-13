package org.harryng.demo.quarkus.validation.annotation;

import org.harryng.demo.quarkus.validation.validator.EditUserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Constraint(validatedBy = EditUserValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EditUserContraint {
    String message() default "Default message user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
