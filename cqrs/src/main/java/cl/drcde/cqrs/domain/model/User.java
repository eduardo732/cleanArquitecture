package cl.drcde.cqrs.domain.model;

import cl.drcde.cqrs.domain.shared.aggregateroot.AggregateRoot;
import cl.drcde.cqrs.domain.shared.eventbus.Event;
import cl.drcde.cqrs.domain.shared.eventbus.EventCollection;

import javax.persistence.Transient;

public class User implements AggregateRoot {
    private Long id;
    private String username;
    private String password;
    @Transient
    private EventCollection events = new EventCollection();

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        //TODO: agregar evento de usuario creado
//        this.getEvents().add(
//            new Event()
//        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public EventCollection getEvents() {
        return null;
    }
}
