package cl.drcde.cqrs.infrastructure.persistence;


import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.infrastructure.entity.JpaUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        // Convertir la entidad User a JpaUser
        JpaUser jpaUser = new JpaUser(user.getUsername(), user.getPassword());
        // Guardar en el repositorio JPA
        JpaUser savedUser = this.jpaUserRepository.save(jpaUser);
        // Convertir la entidad JpaUser a User
        User savedDomainUser = new User(savedUser.getId(), savedUser.getUsername(), savedUser.getPassword());
        return savedDomainUser;
    }

    @Override
    public User findById(Long id) {
        Optional<JpaUser> jpaUser = this.jpaUserRepository.findById(id);
        if(jpaUser.isEmpty()) throw new EntityNotFoundException("User not found");
        User user = new User();
        user.setPassword(jpaUser.get().getPassword());
        user.setUsername(jpaUser.get().getUsername());
        user.setId(jpaUser.get().getId());
        return user;
    }

    @Override
    public void delete(User user) {
        JpaUser jpaUser = new JpaUser(user.getId(), user.getUsername(), user.getPassword());
        this.jpaUserRepository.delete(jpaUser);
    }
}
