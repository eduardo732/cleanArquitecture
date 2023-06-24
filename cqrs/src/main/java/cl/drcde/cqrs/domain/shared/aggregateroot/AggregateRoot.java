package cl.drcde.cqrs.domain.shared.aggregateroot;

import cl.drcde.cqrs.domain.shared.eventbus.EventCollection;

public interface AggregateRoot {
    EventCollection getEvents();
}
