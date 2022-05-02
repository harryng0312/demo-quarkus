package org.harryng.demo.quarkus.user;

import io.quarkus.qute.Qute;
import io.quarkus.qute.i18n.MessageBundles;
import io.quarkus.test.junit.QuarkusTest;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.harryng.demo.quarkus.util.page.Sort;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TestUser {

    static Logger logger = LoggerFactory.getLogger(TestUser.class);

    @Inject
    protected UserService userService;
    @Inject
    protected Mutiny.SessionFactory sessionFactory;

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
        logger.info("Qute:" + Qute.fmt("test {msg:error_screenname}")
                .attribute(MessageBundles.ATTRIBUTE_LOCALE, Locale.forLanguageTag("vi"))
                .render()
        );
    }

    @Test
    public void editUser2() throws Exception {
        var user = new UserImpl();
        var now = LocalDateTime.now();
//        {"id":2,"createdDate":"2022-04-11T15:21:21.082945",
        user.setId(3L);
        user.setCreatedDate(now);
//        "modifiedDate":"2022-04-11T15:21:21.082945","status":"1","username":"username 2",
        user.setModifiedDate(now);
        user.setStatus("1");
        user.setUsername("");
//        "password":"passwd2","screenName":"","dob":"2022-04-11","passwdEncryptedMethod":"plain"}
        user.setPassword("passwd2");
        user.setScreenName("");
        user.setDob(now.toLocalDate());
        user.setPasswdEncryptedMethod("plain");

        var req = "{\n" +
                "    \"id\": 3,\n" +
                "    \"createdDate\": \"2022-04-10T08:00:00\",\n" +
                "    \"modifiedDate\": \"2022-04-10T08:00:00\",\n" +
                "    \"status\": \"1\",\n" +
                "    \"username\": \"\",\n" +
                "    \"password\": \"passwd3\",\n" +
                "    \"screenName\": \"\",\n" +
                "    \"dob\": \"2022-04-10\",\n" +
                "    \"passwdEncryptedMethod\": \"plain\"\n" +
                "}";

        var res = given()
                .when()
                .header("content-type", "application/json")
                .header("Accept-Language", "en")
                .body(req)
                .put("/http/user");
        logger.info("res:" + res.body().prettyPrint());
    }

    @Test
    public void editUser() throws Exception {
        var user = new UserImpl();
        var now = LocalDateTime.now();
//        {"id":2,"createdDate":"2022-04-11T15:21:21.082945",
        user.setId(3L);
        user.setCreatedDate(now);
//        "modifiedDate":"2022-04-11T15:21:21.082945","status":"1","username":"username 2",
        user.setModifiedDate(now);
        user.setStatus("1");
        user.setUsername("");
//        "password":"passwd2","screenName":"","dob":"2022-04-11","passwdEncryptedMethod":"plain"}
        user.setPassword("passwd2");
        user.setScreenName("");
        user.setDob(now.toLocalDate());
        user.setPasswdEncryptedMethod("plain");

        var extras = new HashMap<String, Object>();
//        var runner = sessionFactory.withStatelessTransaction(Unchecked.function((sess) -> {
//            extras.putIfAbsent(BaseService.TRANS_STATELESS_SESSION, sess);
////            extras.putIfAbsent(BaseService.TRANSACTION, trans);
//            return userService.edit(SessionHolder.createAnonymousSession(), user, extras);
//        })).invoke(rs -> {
//            logger.info("result after edit: " + rs);
//        });
//        runner.await().indefinitely();

        userService.edit(SessionHolder.createAnonymousSession(), user, extras);
    }
}
