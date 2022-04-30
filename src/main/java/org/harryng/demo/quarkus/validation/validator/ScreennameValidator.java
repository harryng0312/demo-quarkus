package org.harryng.demo.quarkus.validation.validator;

import org.harryng.demo.quarkus.validation.annotation.ScreennameConstraint;

import javax.inject.Singleton;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Singleton
public class ScreennameValidator implements ConstraintValidator<ScreennameConstraint, String> {

    @Override
    public void initialize(ScreennameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        var valiRs = true;
        valiRs = value != null && !"".equals(value.trim());
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Screenname customized does not match!")
                .addConstraintViolation();
        return valiRs;
    }
}
