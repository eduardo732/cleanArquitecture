package cl.drcde.cqrs.infrastructure.eventbus;

import cl.drcde.cqrs.domain.shared.eventbus.Event;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

public class SpringEventBusTest {
    private ApplicationEventPublisher applicationEventPublisher;
    private SpringEventBus springEventBus;

    @Before
    public void setUp() {
        this.applicationEventPublisher = mock(ApplicationEventPublisher.class);
        this.springEventBus = new SpringEventBus(this.applicationEventPublisher);
    }

    @Test
    public void publishTest() {
        Event event = new Event();
        this.springEventBus.publish(event);
        verify(this.applicationEventPublisher, times(1)).publishEvent(event);
    }

}