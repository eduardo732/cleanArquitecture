package cl.drcde.cqrs.application.query;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.shared.querybus.QueryHandler;
import cl.drcde.cqrs.domain.shared.querybus.exception.QueryBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * QueryHandler: Obtener todos los usuarios
 *
 * VENTAJA: Completamente desacoplada de comandos
 * VENTAJA: Puede escalar BD de lectura independientemente
 * VENTAJA: Sin efectos secundarios
 */
@Slf4j
@Component
public class FindAllUsersQueryHandler implements QueryHandler<FindAllUsersQuery, List<User>> {

    private final UserRepository userRepository;

    public FindAllUsersQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Maneja la query de obtener todos los usuarios
     *
     * Solo lectura
     * Sin efectos secundarios
     * Puede usar BD, caché o Elasticsearch
     *
     * @param query Query de búsqueda (vacía en este caso)
     * @return Lista de todos los usuarios
     */
    @Override
    public List<User> handle(FindAllUsersQuery query) throws QueryBusException {
        log.info("Obteniendo todos los usuarios");

        // Aquí podrías:
        // - Usar Redis para caché (sin afectar comandos)
        // - Consultar una BD separada optimizada para lecturas
        // - Usar Elasticsearch para búsquedas avanzadas
        // - Implementar paginación
        // - Aplicar filtros dinámicos
        // Todo esto SIN afectar a los comandos

        List<User> users = this.userRepository.findAll();
        log.info("Se encontraron {} usuarios", users.size());
        return users;
    }

}
