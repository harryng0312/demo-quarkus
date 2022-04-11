package org.harryng.demo.quarkus.controller.cost;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.harryng.demo.quarkus.base.controller.AbstractController;
import org.harryng.demo.quarkus.cost.dto.Cost;

@ApplicationScoped
@Path("/ws/cost")
public class CostConverter extends AbstractController{

    private static final Map<String, Double> conversionRatios = new HashMap<>();

    static {
        conversionRatios.put("CHF", 0.93);
        conversionRatios.put("USD", 0.84);
        conversionRatios.put("PLN", 0.22);
        conversionRatios.put("EUR", 1.0);
    }

    @Incoming("incoming-costs") 
    @Outgoing("outgoing-costs")
    public Cost convert(Cost cost) { 
        Double conversionRatio = conversionRatios.get(cost.getCurrency().toUpperCase());
        if (conversionRatio == null) {
            return cost;
        }
        return new Cost(conversionRatio * cost.getValue(), "EUR");
    }
}