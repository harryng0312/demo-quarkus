package org.harryng.demo.quarkus.config;

import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ResourceBundle;

@ApplicationScoped
public class CommonConfig {

    @Inject
    public Mutiny.SessionFactory sessionFactory;

//    @Produces
//    @Default
//    public Uni<Mutiny.Session> getTransactionSession(){
//        return sessionFactory.openSession();
//    }

//    @Produces
    public ValidatorFactory getValidatorFactory(){
        ResourceBundleLocator resourceBundleLocator = (locale) -> {
            return ResourceBundle.getBundle("messages.msg", locale);
        };
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .messageInterpolator(new ResourceBundleMessageInterpolator(resourceBundleLocator))
                .buildValidatorFactory();
    }

//    @Produces
    public Validator validatorFactory () {
        return getValidatorFactory().getValidator();
    }

}
