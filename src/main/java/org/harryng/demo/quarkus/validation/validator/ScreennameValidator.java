package org.harryng.demo.quarkus.validation.validator;

import org.harryng.demo.quarkus.util.I18nMessage;
import org.harryng.demo.quarkus.validation.annotation.ScreennameConstraint;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Singleton
public class ScreennameValidator implements ConstraintValidator<ScreennameConstraint, String> {

    @Inject
    protected I18nMessage appMessage;

    @Override
    public void initialize(ScreennameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        var valiRs = true;
        valiRs = value != null && !"".equals(value.trim());
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(appMessage.getMessage("error.screenname"))
                .addConstraintViolation();
        return valiRs;
    }
}
