package cl.drcde.cqrs.domain.shared.querybus;

import cl.drcde.cqrs.domain.shared.querybus.exception.QueryBusException;

/**
 * QueryHandler: Handler genérico para Queries
 * Q: tipo de Query (extiende Query<R>)
 * R: tipo de respuesta
 */
public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query) throws QueryBusException;
}
