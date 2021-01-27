package pl.lodz.p.it.gornik.pomocnikseniora.exceptions;

public class CustomOptimisticLockException extends RuntimeException {

    public CustomOptimisticLockException(String message) {
        super(message);
    }
}
