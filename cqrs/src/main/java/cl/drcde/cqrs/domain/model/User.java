package cl.drcde.cqrs.domain.model;

import cl.drcde.cqrs.domain.events.CreatedUserEvent;
import cl.drcde.cqrs.domain.shared.aggregateroot.AggregateRoot;
import cl.drcde.cqrs.domain.shared.eventbus.EventCollection;
import cl.drcde.cqrs.domain.shared.model.BaseModel;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.domain.vo.Username;

import javax.persistence.Transient;

public class User extends BaseModel implements AggregateRoot {
    private Username username;
    private Password password;
    @Transient
    private final EventCollection events = new EventCollection();

    public User() {
        super();
    }

    public User(
            UUIDv4 id,
            Username username,
            Password password
    ) {
        super(id);
        this.username = username;
        this.password = password;
        this.getEvents().add(
                new CreatedUserEvent(
                        new UserId(this.id.value()),
                        this.username,
                        this.password
                )
        );
    }

    public User(
            Username username,
            Password password
    ) {
        super();
        this.username = username;
        this.password = password;
    }

    public Username getUsername() {
        return this.username;
    }

    public Password getPassword() {
        return this.password;
    }

    @Override
    public EventCollection getEvents() {
        return this.events;
    }
}
