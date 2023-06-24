package cl.drcde.cqrs.domain.shared.querybus.exception;

public final class QueryBusException extends Exception {
    public QueryBusException(String message) { super(message); }
}

