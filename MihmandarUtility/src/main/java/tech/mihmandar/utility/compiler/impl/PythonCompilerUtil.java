package tech.mihmandar.utility.compiler.impl;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import tech.mihmandar.utility.compiler.ICompile;
import tech.mihmandar.utility.dto.CompileResultDto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Murat on 10/23/2017.
 */
public class PythonCompilerUtil implements ICompile {

    public static CompileResultDto compile(String sourceCode){

        int exitValue = 0;
        String result = "";
        try {
            File file = File.createTempFile("pyTest", ".py");
            FileWriter fw = new FileWriter(file);
            fw.write(sourceCode);
            fw.close();

            String absolutePath = file.getAbsolutePath();

            String line = "py -m py_compile " + absolutePath;
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValue(1);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler handler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(handler);
            executor.getStreamHandler();

            exitValue = executor.execute(cmdLine);
            result = outputStream.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        CompileResultDto resultDto = new CompileResultDto();

        if(exitValue == 0){
            resultDto.setSuccess(true);
        }else{
            resultDto.setSuccess(false);
        }
        resultDto.setResultMessage(result);

        return resultDto;
    }
}
