package org.harryng.demo.quarkus.util;

import org.harryng.demo.quarkus.user.entity.UserImpl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

public class SessionHolder implements Serializable {

    public static final String K_USER_ID = "userId";
    public static final String K_TOKEN_ID = "tokenId";
    public static final String K_SESSION_HOLDER = "sessionHolder";
    public static final String K_LANG = "lang";

    public static final long ANONYMOUS_ID = 0L;
    public static final String ANONYMOUS_USERNAME = "anonymous";

    private Locale locale = Locale.getDefault();
    private UserImpl user;

    private SessionHolder(UserImpl user) {
        this.user = user;
    }

    protected static UserImpl initAnoymousUser(){
        var dateNow = LocalDateTime.now();
        var anonymousUser = new UserImpl(
                ANONYMOUS_ID,
                dateNow,
                dateNow,
                "",
                ANONYMOUS_USERNAME,
                "",
                "",
                dateNow.toLocalDate(),
                ""
        );
        return anonymousUser;
    }

    public static SessionHolder createAnonymousSession(){
        return new SessionHolder(initAnoymousUser());
    }

    public static SessionHolder createInstance(UserImpl user) {
        return new SessionHolder(user);
    }

    public UserImpl getUser() {
        return user;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale){
        this.locale = locale;
    }
}