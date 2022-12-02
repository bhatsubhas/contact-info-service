package me.subhas.contactinfo.web.error;

public class ErrorResponse {
    private String errorMessage;
    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
