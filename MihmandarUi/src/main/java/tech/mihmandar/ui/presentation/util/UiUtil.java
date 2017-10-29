package tech.mihmandar.ui.presentation.util;

import com.vaadin.ui.Notification;
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.utility.dto.CompileResultDto;
import tech.mihmandar.utility.dto.RunResultDto;
import tech.mihmandar.utility.exec.JavaCompilerUtil;
import tech.mihmandar.utility.exec.PythonCompilerUtil;
import tech.mihmandar.utility.runner.JavaRunnerUtil;
import tech.mihmandar.utility.runner.PythonRunnerUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Murat on 10/17/2017.
 */
public class UiUtil {

    public static void displaySuccessNotification() {
        Notification.show("İşlem Başarılı", Notification.Type.HUMANIZED_MESSAGE);
    }

    public static void compile(String value, EnumSoftwareLanguages softLanguage) {
        CompileResultDto resultDto = getCompileResultDto(value, softLanguage);
        boolean success = resultDto.isSuccess();
        String resultMessage = resultDto.getResultMessage();
        if(success){
            Notification.show("Derleme Başarılı", Notification.Type.HUMANIZED_MESSAGE);
        }else{
            Notification.show("Derleme Hatası", resultMessage, Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private static CompileResultDto getCompileResultDto(String value, EnumSoftwareLanguages softLanguage) {
        CompileResultDto resultDto;
        switch (softLanguage){
            case Python:{
                resultDto = PythonCompilerUtil.compile(value);
                break;
            }
            case JAVA:
            default:{
                resultDto = JavaCompilerUtil.compile(value);
            }
        }
        return resultDto;
    }

    public static void displayNoSelectedRecordWarning() {
        Notification.show("Bir Kayıt Seçiniz", Notification.Type.WARNING_MESSAGE);
    }

    public static void run(String value, EnumSoftwareLanguages softLanguage) {
        RunResultDto resultDto = getRunResultDto(value, softLanguage);
        boolean success = resultDto.isSuccess();
        String resultMessage = resultDto.getResultMessage();
        if(success){
            Notification.show("Çalıştırma Başarılı", resultDto.getOutPut(), Notification.Type.HUMANIZED_MESSAGE);
        }else{
            Notification.show("Çalıştırma Hatası", resultMessage, Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private static RunResultDto getRunResultDto(String value, EnumSoftwareLanguages softLanguage) {
        RunResultDto runResultDto = new RunResultDto();
        CompileResultDto compileResultDto = getCompileResultDto(value, softLanguage);
        if(compileResultDto.isSuccess()){
            switch (softLanguage){
                case Python:{
                    DateFormat df = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                    Date today = Calendar.getInstance().getTime();
                    String dateAsStr = df.format(today);
                    runResultDto = PythonRunnerUtil.run(dateAsStr, value);
                    break;
                }
                case JAVA:
                default:{
                    int index = value.indexOf("class");
                    String substring = value.substring(index, value.length());
                    String[] split = substring.split(" ");
                    String className = split[1];
                    runResultDto = JavaRunnerUtil.run(className, value);
                }
            }
        }else{
            runResultDto.setSuccess(false);
            runResultDto.setResultMessage("Çalıştırmadan Önce Lütfen Derlendiğinden Emin Olunuz");
        }
        return runResultDto;
    }
}
