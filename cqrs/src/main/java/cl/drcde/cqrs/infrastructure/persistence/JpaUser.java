package cl.drcde.cqrs.infrastructure.persistence;

import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.domain.vo.Username;
import cl.drcde.cqrs.infrastructure.persistence.converter.PasswordAttributeConverter;
import cl.drcde.cqrs.infrastructure.persistence.converter.UsernameAttributeConverter;
import cl.drcde.cqrs.infrastructure.persistence.model.JpaBaseModel;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Table(name = "user")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@Entity
public final class JpaUser extends JpaBaseModel {

    @NotNull(message = "The attribute username can't be empty")
    @Column(name = "username", nullable = false)
    @Convert(converter = UsernameAttributeConverter.class)
    private Username username;
    @NotNull(message = "The attribute password can't be empty")
    @Column(name = "password", nullable = false)
    @Convert(converter = PasswordAttributeConverter.class)
    private Password password;

    public JpaUser() {
        super();
    }

    public JpaUser(
            UUIDv4 id,
            Username username,
            Password password
    ) {
        super(id);
        this.username = username;
        this.password = password;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JpaUser jpaUser = (JpaUser) o;
        return id != null && Objects.equals(id, jpaUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
