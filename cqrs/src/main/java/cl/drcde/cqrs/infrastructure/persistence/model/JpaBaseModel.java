package cl.drcde.cqrs.infrastructure.persistence.model;

import cl.drcde.cqrs.domain.vo.Active;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.infrastructure.persistence.converter.ActiveAttributeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class JpaBaseModel {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
    public String id;

    @NotNull(message = "The date can't be empty")
    @Column(name = "created_date", nullable = false)
    protected LocalDateTime createdDate;

    @NotNull(message = "The attribute active can't be empty")
    @Convert(converter = ActiveAttributeConverter.class)
    @Column(name = "active", nullable = false)
    protected Active active;

    public JpaBaseModel(UUIDv4 id) {
        this.id = id != null ? id.value() : null;
        this.createdDate = LocalDateTime.now();
        this.active = new Active(Boolean.TRUE);
    }

    public JpaBaseModel() {
        this.id = UUIDv4.generate().value();
        this.createdDate = LocalDateTime.now();
        this.active = new Active(Boolean.TRUE);
    }

    public String getId() {
        return id;
    }

    public UUIDv4 getIdAsUUID() {
        return id != null ? new UUIDv4(id) : null;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Active getActive() {
        return active;
    }
}
