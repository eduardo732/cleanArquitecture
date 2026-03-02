package cl.drcde.cqrs.application.query;

import cl.drcde.cqrs.domain.vo.UUIDv4;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Query: Buscar usuario por ID
 *
 * ✅ VENTAJA: Query inmutable
 * ✅ VENTAJA: No tiene efectos secundarios
 * ✅ VENTAJA: Completamente separada de escrituras
 */
@Getter
@AllArgsConstructor
public class FindByIdQuery {
    private final String userId;

    public FindByIdQuery(UUIDv4 userId) {
        this.userId = userId.value();
    }
}

