package tech.mihmandar.utility.dto;

/**
 * Created by Murat on 10/7/2017.
 */
public class CompileResultDto {
    private boolean success;
    private String resultMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
