package cl.drcde.cqrs.domain.events;

import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.shared.eventbus.Event;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;

public class CreatedUserEvent extends Event {
    private final UserId userId;
    private final Username username;
    private final Password password;

    public CreatedUserEvent(
            UserId userId,
            Username username,
            Password password
    ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public UserId getUserId() {
        return userId;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }
}
