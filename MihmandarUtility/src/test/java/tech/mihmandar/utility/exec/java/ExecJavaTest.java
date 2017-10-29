package tech.mihmandar.utility.exec.java;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Murat on 10/29/2017.
 */
public class ExecJavaTest {

    @Test
    public void compileTest() throws IOException {
        int exitValue = getCompileResult();
        Assert.assertTrue(exitValue == 0 || exitValue == 1);
    }

    @Test
    public void runTest() throws IOException {

        int exitValue = getCompileResult();
        Assert.assertTrue(exitValue == 0 || exitValue == 1);

        String result = getRunResult();
        Assert.assertTrue(result.equals("Hello World!"));
    }

    private String getRunResult() throws IOException {
        CommandLine cmdLine = CommandLine.parse("java");
        cmdLine.addArgument("-cp");
        cmdLine.addArgument("c:\\Temp");
        cmdLine.addArgument("HelloWorldApp");

        DefaultExecutor executor = new DefaultExecutor();
        int[] exitValues = {0, 1};
        executor.setExitValues(exitValues);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler handler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(handler);
        executor.execute(cmdLine);
        String result = outputStream.toString();

        return result;
    }

    private int getCompileResult() throws IOException {
        CommandLine cmdLine = CommandLine.parse("javac");
        cmdLine.addArgument(" c:\\Temp\\HelloWorldApp.java");
        DefaultExecutor executor = new DefaultExecutor();
        int[] exitValues = {0, 1};
        executor.setExitValues(exitValues);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler handler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(handler);

        return executor.execute(cmdLine);
    }
}
