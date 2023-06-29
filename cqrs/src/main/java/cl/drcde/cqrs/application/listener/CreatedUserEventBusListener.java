package cl.drcde.cqrs.application.listener;

import cl.drcde.cqrs.domain.events.CreatedUserEvent;
import cl.drcde.cqrs.domain.shared.commandbus.CommandBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class CreatedUserEventBusListener {
    private final CommandBus commandBus;

    public CreatedUserEventBusListener(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @EventListener
    public void onCreatedUserEvent(CreatedUserEvent event) throws  Exception {
        log.debug("CreatedUserEventBusListener::onCreatedUserEvent event: {}", event);
        //TODO: Se debe crear un handle a un comando, como para registrar el historial del usuario creado.
    }
}
