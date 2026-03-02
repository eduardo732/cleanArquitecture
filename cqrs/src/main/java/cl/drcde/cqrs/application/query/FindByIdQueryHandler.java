package cl.drcde.cqrs.application.query;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.shared.querybus.QueryHandler;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * QueryHandler: Buscar usuario por ID
 *
 * ✅ VENTAJA: Completamente desacoplada de comandos
 * ✅ VENTAJA: Puede ser optimizada (caché, BD separada, etc)
 * ✅ VENTAJA: No genera eventos
 */
@Slf4j
@Component
public class FindByIdQueryHandler implements QueryHandler<FindByIdQuery, User> {

    private final UserRepository userRepository;

    public FindByIdQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Maneja la query de búsqueda por ID
     *
     * ✅ Solo lectura
     * ✅ Sin efectos secundarios
     * ✅ Puede ser cacheada sin problemas
     *
     * @param query Query con el ID a buscar
     * @return Usuario encontrado
     */
    @Override
    public User handle(FindByIdQuery query) {
        log.info("Buscando usuario con ID: {}", query.getUserId());

        // Aquí podrías:
        // - Usar Redis para caché
        // - Consultar una BD separada de lectura
        // - Usar Elasticsearch
        // - Implementar paginación
        // Sin afectar a los comandos en absoluto

        User user = this.userRepository.findById(new UUIDv4(query.getUserId()));
        log.info("Usuario encontrado: {}", user.getUsername().value());
        return user;
    }
}

