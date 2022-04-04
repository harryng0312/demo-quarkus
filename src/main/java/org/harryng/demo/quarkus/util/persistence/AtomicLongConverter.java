package org.harryng.demo.quarkus.util.persistence;

import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.AttributeConverter;

public class AtomicLongConverter implements AttributeConverter<AtomicLong, Long> {
    public Long convertToDatabaseColumn(AtomicLong attribute) {
        if (attribute != null) {
            return attribute.get();
        }
        return 0L;
    }

    public AtomicLong convertToEntityAttribute(Long dbData) {
        if (dbData != null) {
            return new AtomicLong(dbData);
        }
        return new AtomicLong(0L);
    }
}