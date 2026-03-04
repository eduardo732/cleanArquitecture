package cl.drcde.cqrs.application.query;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.shared.querybus.Query;

import java.util.List;

/**
 * Query: Obtener todos los usuarios
 *
 * VENTAJA: Query inmutable
 * VENTAJA: No tiene efectos secundarios
 * VENTAJA: Separada completamente de comandos
 */
public class FindAllUsersQuery extends Query<List<User>> {
    // Esta query no requiere parámetros
    // Podrías agregar paginación, filtros, etc si lo necesitas
}
