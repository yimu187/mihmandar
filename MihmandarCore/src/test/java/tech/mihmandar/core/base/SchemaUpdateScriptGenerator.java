package tech.mihmandar.core.base;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.tool.hbm2ddl.SchemaUpdateScript;
import org.junit.Test;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import tech.mihmandar.core.common.entity.BaseEntity;
import tech.mihmandar.utility.service.MihmandarFileConfigService;

import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Murat on 9/11/2017.
 */
public class SchemaUpdateScriptGenerator extends BaseITCase {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void generateScript() throws SQLException, NoSuchFieldException, IOException {

        String hibenateDialect = MihmandarFileConfigService.getFileConfig("hibernate.dialect");
        String jdbcUrl = MihmandarFileConfigService.getFileConfig("jdbc.url");
        String jdbcUsername = MihmandarFileConfigService.getFileConfig("jdbc.username");
        String jdbcPassword = MihmandarFileConfigService.getFileConfig("jdbc.password");
        String auditUsername = MihmandarFileConfigService.getFileConfig("jdbc.audit.username");

        Properties dialectProps = new Properties();
        dialectProps.put("hibernate.dialect", hibenateDialect);

        Connection con = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

        Configuration configuration = ((LocalSessionFactoryBean) applicationContext.getBean("&sessionFactory")).getConfiguration();

        DatabaseMetadata dbmd = new DatabaseMetadata(con, Dialect.getDialect(dialectProps), configuration);
        List<SchemaUpdateScript> schemaUpdateScripts = configuration.generateSchemaUpdateScriptList(Dialect.getDialect(dialectProps), dbmd);

        List<String> tableNameList = new ArrayList<String>();
        StringBuffer sb = new StringBuffer("");
        for (SchemaUpdateScript s : schemaUpdateScripts) {
            String script = s.getScript();
            //https://hibernate.atlassian.net/browse/HHH-8008 -- Sequence uretmkek için yapılır
            //Audit tabloları dışında create table işlemi yapılmış ise
            if(script.startsWith("create table") && !script.contains(auditUsername)){
                char endIndexDelimter = "(".toCharArray()[0];
                int endIndex = script.indexOf(endIndexDelimter);
                String tableName = script.substring(0, endIndex).replaceAll("create table "+jdbcUsername+".","");
                tableNameList.add(tableName.toUpperCase(Locale.ENGLISH).trim());
            }
            script = script.replace(auditUsername,"${AUDIT_SEMA_ADI}");
            script = script.replace(jdbcUsername,"${SEMA_ADI}");
            script = script.toUpperCase(Locale.ENGLISH) + ";" +System.lineSeparator();
            sb.append(script);
        }

        //https://hibernate.atlassian.net/browse/HHH-8008 -- Sequence uretmkek için yapılır
        Reflections reflections = new Reflections("tech.mihmanda.core.data");

        Set<Class<? extends BaseEntity>> allClasses = reflections.getSubTypesOf(BaseEntity.class);
        String sequenceName="";
        for (String tableName : tableNameList) {
            sb.append(System.lineSeparator());
            for (Class<? extends BaseEntity> c : allClasses) {
                Table tableAnnotation = c.getAnnotation(Table.class);
                String tableAnnotationName = tableAnnotation != null ? tableAnnotation.name() : "";
                if(tableName.equals(tableAnnotationName)){
                    //SbsKisi SbsKurum gibi turemenin olduğu sınıflarda sequence turetilen sınıfta olduğu için
                    //Sequence ddl işlemi yapılmaz
                    if(!hasIdField(c)){
                        continue;
                    }
                    SequenceGenerator sequenceGeneratorAnnotaionIdField = c.getDeclaredField("id").getAnnotation(SequenceGenerator.class);
                    sequenceName = sequenceGeneratorAnnotaionIdField.sequenceName();
                    sb.append("CREATE SEQUENCE " + sequenceName + System.lineSeparator());
                    break;
                }
            }
        }

        File file = File.createTempFile("SchemaUpdateScriptGeneratorOutput_", ".sql");
        System.out.println("*******************************************************************\n");
        System.out.println("***************  SchemaUpdateScriptGeneratorOutput  ***************\n");
        System.out.println("*******************************************************************\n");
        System.out.println("File Name : " + file.getAbsolutePath());
        System.out.println("*******************************************************************\n");
        System.out.println("*******************************************************************\n");
        System.out.println("*******************************************************************\n");
        FileWriter fw = new FileWriter(file);
        fw.write(sb.toString());
        fw.close();
    }

    private boolean hasIdField(Class<? extends BaseEntity> c) {
        boolean contains = false;
        Field[] fields = c.getFields();
        for (Field field : fields) {
            contains = "id".equals(field.getName());
            if(contains){
                break;
            }
        }

        return contains;
    }

}
