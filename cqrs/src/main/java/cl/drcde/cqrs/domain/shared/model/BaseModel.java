package cl.drcde.cqrs.domain.shared.model;

import cl.drcde.cqrs.domain.vo.Active;
import cl.drcde.cqrs.domain.vo.UUIDv4;

import java.time.LocalDateTime;

public abstract class BaseModel {


    protected UUIDv4 id;

    protected LocalDateTime createdDate;

    protected Active active;

    public BaseModel(UUIDv4 id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.active = new Active(Boolean.TRUE);
    }

    public BaseModel() {
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
