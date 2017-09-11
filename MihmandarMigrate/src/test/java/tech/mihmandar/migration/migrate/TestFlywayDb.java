package tech.mihmandar.migration.migrate;

import org.flywaydb.core.Flyway;
import org.junit.Test;
import tech.mihmandar.utility.service.MihmandarFileConfigService;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Murat on 9/11/2017.
 */
public class TestFlywayDb {

    @Test
    public void testFlywayDb() throws IOException {

        String dbType = MihmandarFileConfigService.getFileConfig("db.Type");
        String url = MihmandarFileConfigService.getFileConfig("jdbc.url");
        String username = MihmandarFileConfigService.getFileConfig("jdbc.username");
        String password = MihmandarFileConfigService.getFileConfig("jdbc.password");
        String auditUsername = MihmandarFileConfigService.getFileConfig("jdbc.audit.username");
        Flyway flyway = new Flyway();
        HashMap<String, String> map = new HashMap<>();
        map.put("SEMA_ADI", username);
        map.put("AUDIT_SEMA_ADI", auditUsername);
        flyway.setPlaceholders(map);
        flyway.setLocations("classpath:tech/mihmandar/migrate/migration/" + dbType,"classpath:tech/mihmandar/migrate/migration");
        flyway.setEncoding("UTF-8");
        flyway.setDataSource(url, username, password, "select now()");
        flyway.repair();
        flyway.setValidateOnMigrate(false);
        flyway.setBaselineOnMigrate(true);
        flyway.migrate();
    }
}
