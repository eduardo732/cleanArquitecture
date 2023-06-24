package cl.drcde.cqrs.domain.shared.eventbus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class Event {
    private final UUID id;
    private final LocalDateTime date;

    public Event() {
        this(Collections.emptyMap());
    }

    public Event(Map<String, Object> data) {
        this.id = UUID.randomUUID();
        this.date = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
