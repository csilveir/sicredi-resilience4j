package br.com.coffeeandit.resilience4j.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
