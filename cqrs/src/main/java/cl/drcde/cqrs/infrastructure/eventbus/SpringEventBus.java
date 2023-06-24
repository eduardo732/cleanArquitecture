package cl.drcde.cqrs.infrastructure.eventbus;

import cl.drcde.cqrs.domain.shared.eventbus.Event;
import cl.drcde.cqrs.domain.shared.eventbus.EventBus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("SpringEventBus")
@Primary
public final class SpringEventBus implements EventBus {
    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventBus(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(Event event) {
        //log
        this.applicationEventPublisher.publishEvent(event);
    }
}
