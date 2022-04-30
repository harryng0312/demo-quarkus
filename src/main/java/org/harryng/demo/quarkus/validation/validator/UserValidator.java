package org.harryng.demo.quarkus.validation.validator;

import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.validation.annotation.UserConstraint;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Singleton
public class UserValidator implements ConstraintValidator<UserConstraint, UserImpl> {

    @Inject
    protected UserService userService;

    @Override
    public void initialize(UserConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserImpl value, ConstraintValidatorContext context) {
        var valiRs = true;
        valiRs = valiRs && value.getScreenName()!=null && !"".equals(value.getScreenName().trim());
        return valiRs;
    }
}
