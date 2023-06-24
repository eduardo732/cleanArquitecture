package cl.drcde.cqrs.infrastructure.persistence;

import cl.drcde.cqrs.infrastructure.entity.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUser, Long> {
}
