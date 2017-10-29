package tech.mihmandar.utility.runner;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import tech.mihmandar.utility.dto.RunResultDto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Murat on 10/28/2017.
 */
public class JavaRunnerUtil implements IRunner {

    public static RunResultDto run(String fileName, String sourceCode){
        RunResultDto runResultDto = new RunResultDto();
        File file = null;
        try {
            file = File.createTempFile(fileName, ".java");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            fw.write(sourceCode);
            fw.close();

            String absolutePath = file.getAbsolutePath();

            String command = "javac  " + absolutePath;

            CommandLine cmdLine = CommandLine.parse(command);
            DefaultExecutor executor = new DefaultExecutor();
            int[] exitValues = {0,1};
            executor.setExitValues(exitValues);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler handler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(handler);

            int exitValue = executor.execute(cmdLine);
            if(exitValue == 0){
                cmdLine = CommandLine.parse("java");
                cmdLine.addArgument("-cp");
                cmdLine.addArgument(file.getParent());
                cmdLine.addArgument(fileName);

                ByteArrayOutputStream outputStreamRun = new ByteArrayOutputStream();
                PumpStreamHandler handlerRun = new PumpStreamHandler(outputStreamRun);
                executor.setStreamHandler(handlerRun);

                exitValue = executor.execute(cmdLine);
                String result = outputStreamRun.toString();

                if(exitValue == 0){
                    runResultDto.setSuccess(true);
                    runResultDto.setResultMessage("Çalıştırma Başarılı");
                }else{
                    runResultDto.setSuccess(false);
                    runResultDto.setResultMessage("Çalıştırma Başarısız");
                }
                runResultDto.setOutPut(result);
            }else{
                runResultDto.setSuccess(false);
                runResultDto.setResultMessage("Derleme Başarısız");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return runResultDto;
    }
}
