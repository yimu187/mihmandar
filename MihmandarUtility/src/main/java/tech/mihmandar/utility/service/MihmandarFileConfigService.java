package tech.mihmandar.utility.service;

import org.springframework.stereotype.Service;
import tech.mihmandar.utility.enums.ErrorEnums;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Murat on 9/11/2017.
 */
@Service
public class MihmandarFileConfigService {

    public static final String APPLICATION_DYNAMIC_PROPERTIES = "application-env-shared.properties";
    public static final String APPLICATION_CONFIG_PATH = System.getProperty("mihmandarConfigPath"); // "C:/JAVA/FlexCityConfig";

    public static String getFileConfig(String key) {
        Properties properties = new Properties();
        String filePath = APPLICATION_CONFIG_PATH + "/config/default/" + APPLICATION_DYNAMIC_PROPERTIES;
        properties = getProperties(properties, filePath);
        return (String)properties.get(key) ;
    }

    private static Properties getProperties(Properties properties, String filePath) {
        try {
            properties.load(new FileInputStream(filePath));
        } catch (FileNotFoundException e1) {
            throw new RuntimeException(ErrorEnums.DOSYA_BULUNMADI.toString() + " " + filePath);
        } catch (IOException e1) {
            throw new RuntimeException(ErrorEnums.DOSYA_OKUNURKEN_HATA_OLUSTU.toString());
        }

        return properties;
    }

}
