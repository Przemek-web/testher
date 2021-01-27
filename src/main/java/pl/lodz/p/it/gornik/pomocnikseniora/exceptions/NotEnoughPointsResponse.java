package pl.lodz.p.it.gornik.pomocnikseniora.exceptions;

public class NotEnoughPointsResponse {


    private String message;

    public NotEnoughPointsResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
