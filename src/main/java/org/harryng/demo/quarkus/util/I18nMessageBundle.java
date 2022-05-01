package org.harryng.demo.quarkus.util;

import javax.inject.Singleton;

import java.util.Locale;
import java.util.ResourceBundle;

@Singleton
public class I18nMessageBundle {

    public String getMessage(String key, Locale locale) {
        return ResourceBundle.getBundle("messages/msg", locale).getString(key);
    }

    public String getMessage(String key) {
        return ResourceBundle.getBundle("messages/msg").getString(key);
    }
}
