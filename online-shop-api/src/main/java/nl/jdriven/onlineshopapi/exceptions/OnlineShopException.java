package nl.jdriven.onlineshopapi.exceptions;

public class OnlineShopException extends RuntimeException {

    public OnlineShopException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
