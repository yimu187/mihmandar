package tech.mihmandar.ui.presentation.util;

import com.vaadin.ui.Notification;
import tech.mihmandar.utility.compiler.JavaCompilerUtil;
import tech.mihmandar.utility.dto.CompileResultDto;

/**
 * Created by Murat on 10/17/2017.
 */
public class UiUtil {

    public static void displaySuccessNotification() {
        Notification.show("İşlem Başarılı", Notification.Type.HUMANIZED_MESSAGE);
    }

    public static void doCompile(String value) {
        CompileResultDto resultDto = JavaCompilerUtil.compile(value);
        boolean success = resultDto.isSuccess();
        String resultMessage = resultDto.getResultMessage();
        if(success){
            Notification.show("Derleme Başarılı", Notification.Type.HUMANIZED_MESSAGE);
        }else{
            Notification.show("Derleme Hatası", resultMessage, Notification.Type.HUMANIZED_MESSAGE);
        }
    }
}
