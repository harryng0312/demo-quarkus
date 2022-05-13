package org.harryng.demo.quarkus.i18n;

import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

@Localized("en")
public interface I18nMessageEn extends I18nMessage {

    @Override
    @Message("Screen name is fault in en")
    String errorScreenname();
}
