package org.harryng.demo.quarkus.user;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.harryng.demo.quarkus.util.page.Sort;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TestUser {

    static Logger logger = LoggerFactory.getLogger(TestUser.class);

    @Test
    public void sort() {
        List<String> content = new ArrayList<>();
        PageInfo pageInfo = new PageInfo(0, 5, 0, Sort.by(Sort.Direction.ASC, "id"));
        Page p = new Page<>(content, pageInfo, 10);
        logger.info("unsorted: " + (Sort.unsorted() == null));
    }

    @Test
    public void getUser() {
        var body = given()
                .when().get("/user/get-username-sync?id=1")
                .body().prettyPrint();
        logger.info("test Username:" + body);
    }
}
