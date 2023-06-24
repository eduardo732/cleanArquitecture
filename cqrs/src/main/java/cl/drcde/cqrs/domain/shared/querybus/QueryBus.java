package cl.drcde.cqrs.domain.shared.querybus;

import cl.drcde.cqrs.domain.shared.querybus.exception.QueryBusException;

public interface QueryBus {
    <T> T handle(Query<T> query) throws QueryBusException;
}
