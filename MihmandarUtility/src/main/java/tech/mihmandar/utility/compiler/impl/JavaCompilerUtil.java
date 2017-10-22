package tech.mihmandar.utility.compiler.impl;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import tech.mihmandar.utility.compiler.ICompile;
import tech.mihmandar.utility.dto.CompileResultDto;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Murat on 10/7/2017.
 */
public class JavaCompilerUtil implements ICompile {

    public static CompileResultDto compile(String sourceCode){
        CompileResultDto resultDto = new CompileResultDto();
        if(!StringUtils.hasText(sourceCode)){
            resultDto.setSuccess(false);
            resultDto.setResultMessage("Lütfen editör içeriğini boş bırakmayınız");
            return resultDto;
        }

        List<Diagnostic<? extends JavaFileObject>> diagnostics = Collections.emptyList();
        try {
            Random random = new Random();
            long i = random.nextInt(1000);
            String next = String.valueOf(i);
            String sourceFilePath = System.getProperty("java.io.tmpdir") + File.separator + "file" + next + ".java";
            File file = new File(sourceFilePath);
            file.createNewFile();
            FileUtils.writeByteArrayToFile(file, sourceCode.getBytes());

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<JavaFileObject>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(sourceFilePath));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticsCollector, null, null, compilationUnits);
            boolean success = task.call();
            if (!success) {
                diagnostics = diagnosticsCollector.getDiagnostics();
            }
            fileManager.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String resultMessage = "";
        boolean success;
        if(diagnostics.isEmpty()){
            resultMessage  = "Derleme Başarılı";
            success = true;
        }else{
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                resultMessage += " " + diagnostic.getMessage(null);
            }
            success = false;
        }
        resultDto.setResultMessage(resultMessage);
        resultDto.setSuccess(success);
        return resultDto;
    }
}
