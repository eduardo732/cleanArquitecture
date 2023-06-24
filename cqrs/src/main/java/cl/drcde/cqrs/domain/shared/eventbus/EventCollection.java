package cl.drcde.cqrs.domain.shared.eventbus;

import java.util.ArrayList;
import java.util.List;

public class EventCollection {
    private final List<Event> eventList;

    public EventCollection(List<Event> eventList) {
        this.eventList = eventList;
    }
    public EventCollection() { this.eventList = new ArrayList<>(); }
    public List<Event> all() { return this.eventList; }
    public void add(Event event) { this.eventList.add(event); }
    public void clear() { eventList.clear(); }
    public void publish(EventBus eventBus) {
        eventBus.publish(this.eventList);
        clear();
    }
}
