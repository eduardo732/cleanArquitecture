package cl.drcde.cqrs.domain.shared.commandbus;

import cl.drcde.cqrs.domain.shared.commandbus.exception.CommandBusException;

public interface CommandHandler<T extends Command> {
    void handle(T command) throws CommandBusException;
}
