package cl.drcde.cqrs.application.query;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.domain.shared.querybus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Query: Buscar usuario por ID
 *
 * VENTAJA: Query inmutable
 * VENTAJA: No tiene efectos secundarios
 * VENTAJA: Completamente separada de escrituras
 */
@Getter
public class FindByIdQuery extends Query<User> {
    private final String userId;

    public FindByIdQuery(UUIDv4 userId) {
        this.userId = userId.value();
    }

    public FindByIdQuery(String userId) {
        this.userId = userId;
    }
}
