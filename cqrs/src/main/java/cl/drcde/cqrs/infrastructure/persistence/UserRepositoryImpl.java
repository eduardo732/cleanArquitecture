package cl.drcde.cqrs.infrastructure.persistence;


import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

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
        return new User(savedUser.getId(), savedUser.getUsername(), savedUser.getPassword());
    }

    @Override
    public User findById(UUIDv4 id) {
        Optional<JpaUser> jpaUser = this.jpaUserRepository.findById(id);
        if(jpaUser.isEmpty()) throw new EntityNotFoundException("User not found");
        return new User(
                jpaUser.get().getId(),
                jpaUser.get().getUsername(),
                jpaUser.get().getPassword()
        );
    }

    @Override
    public void delete(User user) {
        JpaUser jpaUser = new JpaUser(user.getId(), user.getUsername(), user.getPassword());
        this.jpaUserRepository.delete(jpaUser);
    }
}
