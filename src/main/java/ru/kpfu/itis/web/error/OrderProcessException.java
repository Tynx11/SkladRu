package ru.kpfu.itis.web.error;

public class OrderProcessException extends RuntimeException {

    public OrderProcessException() {
        super();
    }

    public OrderProcessException(String message) {
        super(message);
    }

    public OrderProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderProcessException(Throwable cause) {
        super(cause);
    }
}
