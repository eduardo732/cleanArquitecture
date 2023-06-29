package cl.drcde.cqrs.infrastructure.persistence;

import cl.drcde.cqrs.domain.vo.UUIDv4;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUser, UUIDv4> {
}
