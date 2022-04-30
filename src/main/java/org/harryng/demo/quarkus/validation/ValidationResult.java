package org.harryng.demo.quarkus.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationResult {
    public ValidationResult(String[] messages) {
        this.success = true;
        this.messages = messages;
    }

    public ValidationResult(Set<? extends ConstraintViolation<?>> violations) {
        this.success = false;
        var mess = violations.stream()
                .map(cv -> cv.getMessage())
                .collect(Collectors.toList());
        this.messages = mess.toArray(new String[mess.size()]);
    }

    private String[] messages = null;
    private boolean success = false;

    public String[] getMessages() {
        if(messages==null){
            messages = new String[0];
        }
        return messages;
    }

    public boolean isSuccess() {
        return success;
    }
}
