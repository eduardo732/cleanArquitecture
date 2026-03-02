package cl.drcde.cqrs.infrastructure.persistence;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserRepositoryImpl: Implementación del repositorio
 *
 * ✅ VENTAJA: Domain no conoce esta clase
 * ✅ VENTAJA: Mapea JpaUser ↔ User
 * ✅ VENTAJA: Fácil cambiar a otra BD
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UUIDv4 userId = UserId.generate();
        JpaUser jpaUser = new JpaUser(userId, user.getUsername(), user.getPassword());
        JpaUser savedUser = this.jpaUserRepository.save(jpaUser);
        return new User(
                new UserId(savedUser.getId()),
                savedUser.getUsername(),
                savedUser.getPassword()
        );
    }

    @Override
    public User findById(UUIDv4 id) {
        Optional<JpaUser> jpaUser = this.jpaUserRepository.findById(id.value());
        if(jpaUser.isEmpty()) throw new EntityNotFoundException("User not found");
        return new User(
            new UserId(jpaUser.get().getId()),
                jpaUser.get().getUsername(),
                jpaUser.get().getPassword()
        );
    }

    /**
     * Obtiene todos los usuarios
     *
     * ✅ Mapea JpaUser → User
     * ✅ Mantiene dominio independiente
     * ✅ Aquí podrías agregar paginación
     */
    @Override
    public List<User> findAll() {
        return this.jpaUserRepository.findAll()
                .stream()
                .map(jpaUser -> new User(
                        new UserId(jpaUser.getId()),
                        jpaUser.getUsername(),
                        jpaUser.getPassword()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(User user) {
        JpaUser jpaUser = new JpaUser(user.getId(), user.getUsername(), user.getPassword());
        this.jpaUserRepository.delete(jpaUser);
    }
}
