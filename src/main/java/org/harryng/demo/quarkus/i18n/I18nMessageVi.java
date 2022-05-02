package org.harryng.demo.quarkus.i18n;

import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.Message;

@Localized("vi")
public interface I18nMessageVi extends I18nMessage {

    @Override
    @Message("Tên hiển thị chưa đúng")
    String error_screenname();

    @Message("Tên tài khoản không được để trống")
    String error_username_notblank();
}
