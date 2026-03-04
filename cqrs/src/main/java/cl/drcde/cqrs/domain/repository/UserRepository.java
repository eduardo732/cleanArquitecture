package cl.drcde.cqrs.domain.repository;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.vo.UUIDv4;

import java.util.List;
import java.util.UUID;

/**
 * UserRepository: Interfaz del repositorio
 *
 * VENTAJA: Domain no conoce JPA
 * VENTAJA: Agnóstico de BD
 * VENTAJA: Fácil de testear con mocks
 */
public interface UserRepository {
    User save(User user);

    User findById(UUIDv4 id);

    List<User> findAll();

    void delete(User user);
}
