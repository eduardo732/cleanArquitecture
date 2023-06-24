package cl.drcde.cqrs.domain.shared.eventbus;

import cl.drcde.cqrs.domain.shared.aggregateroot.AggregateRoot;

import java.util.Collection;

public interface EventBus {
    void publish(Event event);
    default void publish(Collection<Event> events) { events.forEach(this::publish); }
    default void publish(EventCollection collection) { collection.publish(this); }
    default void publish(AggregateRoot aggregateRoot) {
        publish(aggregateRoot.getEvents());
    }

}
