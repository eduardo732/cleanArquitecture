package cl.drcde.cqrs.domain.shared.querybus;

import cl.drcde.cqrs.domain.shared.querybus.exception.QueryBusException;

public interface QueryHandler<T, U extends Query<T>> {
    T handle(U query) throws QueryBusException;
}
