package org.harryng.demo.quarkus.validation.validator;

import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.MessageBundles;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import org.harryng.demo.quarkus.i18n.I18nMessage;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.I18nMessageBundle;
import org.harryng.demo.quarkus.validation.annotation.EditUserContraint;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class EditUserValidator implements ConstraintValidator<EditUserContraint, UserImpl> {

    @Inject
    protected I18nMessageBundle appMessage;

//    @Inject
//    protected UserService userService;

//    @Inject
//    protected I18nMessage i18nMessage;

    @Override
    public void initialize(EditUserContraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserImpl value, ConstraintValidatorContext context) {
        var valiRs = true;
        valiRs = value != null && !"".equals(value.getScreenName());
        var headers = context.unwrap(HibernateConstraintValidatorContext.class)
                        .getConstraintValidatorPayload(MultiMap.class);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                MessageBundles.get(I18nMessage.class,
                        Localized.Literal.of(headers.get("Accept-Language"))).error_screenname())
                .addConstraintViolation();
        return valiRs;
    }
}
