package pl.lodz.p.it.gornik.pomocnikseniora.dtos.response;

public class EtagResponse {
    private String message;

    public EtagResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
