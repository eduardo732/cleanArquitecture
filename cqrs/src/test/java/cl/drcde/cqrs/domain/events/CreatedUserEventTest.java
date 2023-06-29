package cl.drcde.cqrs.domain.events;

import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.domain.vo.Username;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CreatedUserEventTest {
    private static final UserId USER_ID = new UserId(UUIDv4.generate().value());
    private static final Username USERNAME = new Username("USERNAME");
    private static final Password PASSWORD = new Password("Password");
    @Test
    public void createdUserEventSuccess() {
        CreatedUserEvent createdUserEvent = new CreatedUserEvent(USER_ID, USERNAME, PASSWORD);
        assertEquals(USER_ID, createdUserEvent.getUserId());
        assertEquals(USERNAME, createdUserEvent.getUsername());
        assertEquals(PASSWORD, createdUserEvent.getPassword());
    }
}
