package org.harryng.demo.quarkus.util;

import io.quarkus.qute.TemplateExtension;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

import java.util.Locale;
import java.util.ResourceBundle;

@Singleton
public class I18nMessage {

    public String getMessage(String key, Locale locale){
        return ResourceBundle.getBundle("messages/msg", locale).getString(key);
    }

    public String getMessage(String key){
        return ResourceBundle.getBundle("messages/msg").getString(key);
    }
}
