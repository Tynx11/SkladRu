package ru.kpfu.itis.web.error;

public class ProductAlreadyExistException extends RuntimeException {
    public ProductAlreadyExistException() {
        super();
    }

    public ProductAlreadyExistException(String message) {
        super(message);
    }

    public ProductAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
