package cl.drcde.cqrs.domain.model;

import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.domain.vo.Username;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Test
    public void userPropertiesTest() {
        UUIDv4 randomUserId = new UserId(UUIDv4.generate().value());
        Username username = new Username("test");
        Password password = new Password("password");
        User user = new User(randomUserId, username, password);
        assertEquals(randomUserId, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(Boolean.FALSE, user.getEvents().all().isEmpty());
    }
}
