package org.harryng.demo.quarkus.i18n;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

@MessageBundle("msg")
public interface I18nMessage {

    @Message("Screen name is fault")
    String error_screenname();

    @Message("Username is not blank")
    String error_username_notblank();
}
