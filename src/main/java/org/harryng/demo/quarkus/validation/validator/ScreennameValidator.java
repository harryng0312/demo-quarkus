package org.harryng.demo.quarkus.validation.validator;

import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.MessageBundles;
import io.vertx.core.MultiMap;
import org.harryng.demo.quarkus.i18n.I18nMessage;
import org.harryng.demo.quarkus.util.I18nMessageBundle;
import org.harryng.demo.quarkus.validation.annotation.ScreennameConstraint;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

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
        var headers = context.unwrap(HibernateConstraintValidatorContext.class)
                .getConstraintValidatorPayload(MultiMap.class);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        MessageBundles.get(I18nMessage.class,
                                Localized.Literal.of(headers.get("Accept-Language"))).errorScreenname())
                .addConstraintViolation();
        return valiRs;
    }
}
