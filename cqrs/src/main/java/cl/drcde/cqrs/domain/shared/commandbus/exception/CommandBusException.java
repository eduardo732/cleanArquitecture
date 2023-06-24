package cl.drcde.cqrs.domain.shared.commandbus.exception;

public final class CommandBusException extends Exception {
    public CommandBusException(String message) { super(message); }
}

