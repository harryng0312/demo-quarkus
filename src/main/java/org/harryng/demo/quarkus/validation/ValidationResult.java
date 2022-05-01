package org.harryng.demo.quarkus.validation;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Stream;

public class ValidationResult {
    public ValidationResult(String[] messages) {
        this.success = true;
        this.messages = messages;
    }

    public ValidationResult(Set<? extends ConstraintViolation<?>> violations) {
        this.success = false;
        this.messages = violations.stream()
                .map(cv -> {
                    return String.join("", "/", cv.getPropertyPath().toString()
                            ,":", cv.getMessage());
                }).toArray(String[]::new);
    }

    private String[] messages = null;
    private boolean success = false;

    public String[] getMessages() {
        if (messages == null) {
            messages = new String[0];
        }
        return messages;
    }

    public boolean isSuccess() {
        success = this.getMessages().length == 0;
        return success;
    }

    public String getMessagesInJson(){
        var json = new JsonObject();
        json.put("messages", new JsonArray());
        var messagesJson = json.getJsonArray("messages");
        Stream.of(this.messages).forEach(msg -> {

            messagesJson.add(msg);
        });
        return json.toString();
    }
}
