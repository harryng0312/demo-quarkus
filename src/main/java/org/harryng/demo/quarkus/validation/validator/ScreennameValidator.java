package org.harryng.demo.quarkus.validation.validator;

import org.harryng.demo.quarkus.util.I18nMessageBundle;
import org.harryng.demo.quarkus.validation.annotation.ScreennameConstraint;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Singleton
public class ScreennameValidator implements ConstraintValidator<ScreennameConstraint, String> {

    @Inject
    protected I18nMessageBundle appMessage;

    @Override
    public void initialize(ScreennameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        var valiRs = true;
        valiRs = value != null && !"".equals(value.trim());
//        context.disableDefaultConstraintViolation();
//        context.buildConstraintViolationWithTemplate(
//                MessageBundles.get(I18nMessage.class, Localized.Literal.of("vi")).error_screenname())
//                .addConstraintViolation();
        return valiRs;
    }
}
