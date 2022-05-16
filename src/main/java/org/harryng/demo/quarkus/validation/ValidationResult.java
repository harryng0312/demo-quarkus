package org.harryng.demo.quarkus.validation;

import io.quarkus.qute.Qute;
import io.quarkus.qute.i18n.MessageBundles;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.validation.ConstraintViolation;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationResult {
    public ValidationResult(String[] messages) {
        this.success = true;
    }

    private ValidationResult(Set<? extends ConstraintViolation<?>> violations, Locale locale) {
        this.success = false;
        this.mapPathMsg = violations.stream().collect(Collectors.toMap(
                cv -> String.join("", "/", cv.getPropertyPath().toString()),
                cv -> Qute.fmt(cv.getMessage())
                        .attribute(MessageBundles.ATTRIBUTE_LOCALE, locale).render(),
                (s0, s1) -> s0
        ));
    }

    private boolean success = false;

    private Map<String, String> mapPathMsg = null;

    public static ValidationResult getInstance(Set<? extends ConstraintViolation<?>> violations, Locale locale) {
        return new ValidationResult(violations, locale);
    }

    public boolean isSuccess() {
        success = this.mapPathMsg.isEmpty();
        return success;
    }

    public String getMessagesInJson() {
        var json = new JsonObject();
        json.put("failures", new JsonArray());
        var messagesJson = json.getJsonArray("failures");
        this.mapPathMsg.forEach((key, value) -> {
            var result = new JsonObject();
            result.put("path", key);
            result.put("message", value);
            messagesJson.add(result);
        });
        return json.toString();
    }
}
