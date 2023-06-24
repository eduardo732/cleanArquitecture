package cl.drcde.cqrs.domain.repository;

import cl.drcde.cqrs.domain.model.User;


public interface UserRepository {
    User save(User user);
    User findById(Long id);
    void delete(User user);
}
