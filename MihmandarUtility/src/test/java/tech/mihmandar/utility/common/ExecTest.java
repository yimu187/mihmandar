package tech.mihmandar.utility.common;

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
public class ExecTest {

    @Test
    public void CommandTest() throws IOException {
//        String commnand = "java c:\\Temp";
        CommandLine cmdLine = CommandLine.parse("java");
        cmdLine.addArgument(" -version");
        DefaultExecutor executor = new DefaultExecutor();
        int[] exitValues = {0, 1};
        executor.setExitValues(exitValues);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler handler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(handler);

        int exitValue = executor.execute(cmdLine);
        Assert.assertTrue(exitValue == 0 || exitValue == 1);
    }
}
