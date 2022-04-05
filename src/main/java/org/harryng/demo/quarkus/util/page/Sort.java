package org.harryng.demo.quarkus.util.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sort implements Serializable {
    public static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.ASC;
    public static final Sort UNSORTED = Sort.by(new String[0]);
    public List<Order> orders = Collections.emptyList();

    public Sort(Direction direction, String... properties){
        this(direction, Arrays.asList(properties));
    }

    public Sort(Direction direction, List<String> properties){
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }

        this.orders = new ArrayList<>(properties.size());

        for (String property : properties) {
            this.orders.add(new Sort.Order(direction, property));
        }
    }

    public static Sort by(String... properties) {
        return properties.length == 0 ? Sort.unsorted() : new Sort(DEFAULT_DIRECTION, properties);
    }

    public static Sort by(Direction direction, String... properties) {
        return properties.length == 0 ? Sort.unsorted() : new Sort(direction, properties);
    }

    public List<Order> getOrders(){
        return orders;
    }

    public static Sort unsorted() {
        return UNSORTED;
    }

    public static enum Direction {
        ASC,
        DESC
    }

    public static class Order implements Serializable {
        private Direction direction = Direction.ASC;
        private String property = "";

        public Order(Direction direction, String property){
            this.direction = direction;
            this.property = property;
        }
    }
}
