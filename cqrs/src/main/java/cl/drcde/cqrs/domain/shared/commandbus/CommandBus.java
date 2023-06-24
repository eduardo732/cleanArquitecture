package cl.drcde.cqrs.domain.shared.commandbus;

import cl.drcde.cqrs.domain.shared.commandbus.exception.CommandBusException;

public interface CommandBus {
    void handle(Command command) throws CommandBusException;
}
