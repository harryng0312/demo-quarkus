package org.harryng.demo.quarkus.user;

import io.quarkus.panache.common.Sort;
import io.quarkus.qute.Qute;
import io.quarkus.qute.i18n.MessageBundles;
import io.quarkus.test.junit.QuarkusTest;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.PagedResult;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;

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
        PageInfo pageInfo = new PageInfo(0, 5, Sort.ascending("id_"));
        PagedResult p = new PagedResult<>(content, pageInfo, 10);
        logger.info("unsorted: " + (Sort.empty() == null));
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
        var req = "{\n" +
                "    \"id\": 3,\n" +
                "    \"createdDate\": \"2022-04-10T08:00:00\",\n" +
                "    \"modifiedDate\": \"2022-04-10T08:00:00\",\n" +
                "    \"status\": \"1\",\n" +
                "    \"username\": \"username 2\",\n" +
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
        user.setId(2L);
        user.setCreatedDate(now);
//        "modifiedDate":"2022-04-11T15:21:21.082945","status":"1","username":"username 2",
        user.setModifiedDate(now);
        user.setStatus("1");
        user.setUsername("username 2");
//        "password":"passwd2","screenName":"","dob":"2022-04-11","passwdEncryptedMethod":"plain"}
        user.setPassword("passwd2");
        user.setScreenName("screenname3");
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

        int result = userService.edit(SessionHolder.createAnonymousSession(), user, extras).await().indefinitely();
        logger.info("edit result:" + result);
    }

    @Test
    public void findUserByUsername() throws Exception {
        var res = given()
                .header("X-Correlation-ID", UUID.randomUUID().toString())
                .when()
                .header("content-type", "application/json")
                .header("Accept-Language", "en")
                .get("/http/user/username/username 7")
                .body().prettyPrint();
//        var user = userService.getByUsername(SessionHolder.createAnonymousSession(), "username 3",
//                        new HashMap<>())
//                .await().indefinitely();
//        logger.info("user:" + user);
    }
}
