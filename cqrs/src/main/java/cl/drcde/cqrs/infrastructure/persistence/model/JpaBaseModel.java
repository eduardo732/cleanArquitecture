package cl.drcde.cqrs.infrastructure.persistence.model;

import cl.drcde.cqrs.domain.vo.Active;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.infrastructure.persistence.converter.ActiveAttributeConverter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class JpaBaseModel {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = UUIDv4.LENGTH)
    protected UUIDv4 id;
    @NotNull(message = "The date can't be empty")
    @Column(name = "created_date", nullable = false)
    protected LocalDateTime createdDate;
    @NotNull(message = "The attribute active can't be empty")
    @Convert(converter = ActiveAttributeConverter.class)
    @Column(name = "active", nullable = false)
    protected Active active;

    public JpaBaseModel(UUIDv4 id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.active = new Active(Boolean.TRUE);
    }

    public JpaBaseModel() {
        this.id = UUIDv4.generate();
        this.createdDate = LocalDateTime.now();
        this.active = new Active(Boolean.TRUE);
    }

    public UUIDv4 getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Active getActive() {
        return active;
    }
}
