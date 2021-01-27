package pl.lodz.p.it.gornik.pomocnikseniora.exceptions;

public class ActivationCodeResponse {


    private String message;

    public ActivationCodeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
