package cl.drcde.cqrs.domain.shared.exception;

public final class DomainException extends RuntimeException{
    public DomainException(String message) { super(message); }
    public DomainException(String message, Throwable cause) { super(message, cause); }
}
