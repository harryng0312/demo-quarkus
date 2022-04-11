package org.harryng.demo.quarkus.controller.cost;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.harryng.demo.quarkus.base.controller.AbstractController;
import org.harryng.demo.quarkus.cost.dto.Cost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
@Path("/ws/collected-costs")
public class CostCollector extends AbstractController {
    static Logger logger = LoggerFactory.getLogger(CostCollector.class);
    private double sum = 0.0;

    @GET
    public synchronized double getCosts() {
        return sum;
    }

    @Incoming("collector")
    // @Outgoing("out-collector")
    public synchronized double collect(Cost cost) {
        logger.info("info: " + cost.getValue() + " " + cost.getCurrency());
        if ("EUR".equals(cost.getCurrency())) {
            sum += cost.getValue();
        }
        return sum;
    }
}
