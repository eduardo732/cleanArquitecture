package cl.drcde.cqrs.infrastructure.eventbus;

import cl.drcde.cqrs.domain.shared.eventbus.Event;
import cl.drcde.cqrs.domain.shared.eventbus.EventBus;
import org.springframework.stereotype.Component;

@Component("ConsoleEventBus")
public final class ConsoleEventBus implements EventBus {
    @Override
    public void publish(Event event) {
        System.out.printf("%s %s %s%n", event.getClass().getName(), event.getId().toString(), event.getDate());
    }
}
