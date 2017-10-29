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
public class PythonRunnerUtil implements IRunner {

    public static RunResultDto run(String fileName, String sourceCode){
         RunResultDto resultDto = new RunResultDto();

        int exitValue = 1;
        String result = "";
        File file = null;
        try {
            file = File.createTempFile("pyTest", ".py");
            FileWriter fw = new FileWriter(file);
            fw.write(sourceCode);
            fw.close();

            String absolutePath = file.getAbsolutePath();

            String line = "py -m py_compile " + absolutePath;

            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            int[] exitValues = {0,1};
            executor.setExitValues(exitValues);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler handler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(handler);

            exitValue = executor.execute(cmdLine);
            if(exitValue == 0){

                cmdLine = CommandLine.parse("py");
                cmdLine.addArgument(file.getAbsolutePath());

                ByteArrayOutputStream outputStreamRun = new ByteArrayOutputStream();
                PumpStreamHandler handlerRun = new PumpStreamHandler(outputStreamRun);
                executor.setStreamHandler(handlerRun);

                exitValue = executor.execute(cmdLine);
                result = outputStreamRun.toString();

                if(exitValue == 0){
                    resultDto.setSuccess(true);
                    resultDto.setResultMessage("Çalıştırma Başarılı");
                }else{
                    resultDto.setSuccess(false);
                    resultDto.setResultMessage("Çalıştırma Başarısız");
                }
                resultDto.setOutPut(result);

            }else{
                resultDto.setSuccess(false);
                resultDto.setResultMessage("Derleme Başarısız");
            }

        } catch (IOException e) {
            file.delete();
            resultDto.setSuccess(false);
            resultDto.setResultMessage(e.getMessage());
            e.printStackTrace();
        }
        file.delete();

        if(exitValue == 0){
            resultDto.setSuccess(true);
        }else{
            resultDto.setSuccess(false);
        }

        if(!resultDto.isSuccess() && !result.equals("")){
            resultDto.setResultMessage(result);
        }

        return resultDto;
    }
}
