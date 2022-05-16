package org.harryng.demo.quarkus.validation.validator;

import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.MessageBundles;
import io.smallrye.mutiny.Uni;
import io.vertx.core.MultiMap;
import org.harryng.demo.quarkus.base.service.BaseService;
import org.harryng.demo.quarkus.i18n.I18nMessage;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.user.service.UserServiceImpl;
import org.harryng.demo.quarkus.util.I18nMessageBundle;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.validation.ValidationPayloads;
import org.harryng.demo.quarkus.validation.annotation.EditUserContraint;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

@ApplicationScoped
public class EditUserValidator implements ConstraintValidator<EditUserContraint, UserImpl> {
    static Logger logger = LoggerFactory.getLogger(EditUserValidator.class);
    @Inject
    protected I18nMessageBundle appMessage;

//    @Inject
//    protected I18nMessage i18nMessage;

    @Override
    public void initialize(EditUserContraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserImpl value, ConstraintValidatorContext context) {
        var valiRs = true;
//        logger.info("validate user");
        context.disableDefaultConstraintViolation();
        valiRs = value.getUsername() != null && !"".equals(value.getScreenName());
        var validatorContextMap = context.unwrap(HibernateConstraintValidatorContext.class)
                .getConstraintValidatorPayload(ValidationPayloads.class);

        var sessionHolder = validatorContextMap.get(SessionHolder.class);
        var extras = (Map<String, Object>) validatorContextMap.get(Map.class);
        var userService = validatorContextMap.get(UserService.class);
        var headers = (MultiMap) extras.get(BaseService.HTTP_HEADERS);
        if ("".equals(value.getScreenName())) {
            context.buildConstraintViolationWithTemplate(
                            MessageBundles.get(I18nMessage.class,
                                    Localized.Literal.of(sessionHolder.getLocale().getLanguage())).errorScreenname())
                    .addConstraintViolation();
        }
        UserImpl user = null;
        try {
            user = userService.getByUsername(sessionHolder, value.getUsername(), extras)
                    .await().indefinitely();
            valiRs = valiRs && (user == null);
            if (user != null) {
                context.buildConstraintViolationWithTemplate(
                                MessageBundles.get(I18nMessage.class,
                                        Localized.Literal.of(sessionHolder.getLocale().getLanguage())).errorUserIsExisted())
                        .addConstraintViolation();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return valiRs;
    }
}
