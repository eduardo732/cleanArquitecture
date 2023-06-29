package cl.drcde.cqrs.domain.repository;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.vo.UUIDv4;

import java.util.UUID;


public interface UserRepository {
    User save(User user);

    User findById(UUIDv4 id);

    void delete(User user);
}
