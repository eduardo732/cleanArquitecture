package cl.drcde.cqrs.infrastructure.eventbus;

import cl.drcde.cqrs.domain.shared.querybus.Query;
import cl.drcde.cqrs.domain.shared.querybus.QueryBus;
import cl.drcde.cqrs.domain.shared.querybus.QueryHandler;
import cl.drcde.cqrs.domain.shared.querybus.exception.QueryBusException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("SpringQueryBus")
@Primary
public final class SpringQueryBus extends BaseBus implements QueryBus {
    private final Map<Class, QueryHandler> handlers;

    public SpringQueryBus(List<QueryHandler> queryHandlers) {
        this.handlers = new HashMap<>();
        queryHandlers.forEach(queryHandler -> {
            Class queryClass = getQueryClass(queryHandler);
            handlers.put(queryClass, queryHandler);
        });
    }

    @Override
    public <T> T handle(Query<T> query) throws QueryBusException {
        //logs
        if(!this.handlers.containsKey(query.getClass())) {
            throw new QueryBusException("No existen handlers para " + query.getClass().getName());
        }
        T response = (T) this.handlers.get(query.getClass()).handle(query);
        return response;
    }

    private Class<?> getQueryClass(QueryHandler queryHandler) {
        //logs
        Type type = ((ParameterizedType) queryHandler.getClass().getGenericInterfaces()[1])
                .getActualTypeArguments()[0];
        //logs
        return this.getClass(type.getTypeName());
    }
}
