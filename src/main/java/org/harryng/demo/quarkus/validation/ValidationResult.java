package org.harryng.demo.quarkus.validation;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationResult {
    public ValidationResult(String[] messages) {
        this.success = true;
        this.messages = messages;
    }

    public ValidationResult(Set<? extends ConstraintViolation<?>> violations) {
        this.success = false;
//        this.messages = violations.stream()
//                .map(cv -> {
//                    return String.join("", "{\"path\":",
//                            "\"/", cv.getPropertyPath().toString(),"\",",
//                            "\"message\":\"", cv.getMessage(), "\"");
//                }).toArray(String[]::new);
        this.mapPathMsg = violations.stream().collect(Collectors.toMap(
                cv -> String.join("", "/", cv.getPropertyPath().toString()),
                ConstraintViolation::getMessage
        ));
    }

    private String[] messages = null;
    private boolean success = false;

    private Map<String, String> mapPathMsg = null;

    public String[] getMessages() {
        if (messages == null) {
            messages = new String[0];
        }
        return messages;
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
