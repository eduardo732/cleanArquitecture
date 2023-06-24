package cl.drcde.cqrs.domain.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Test
    public void userPropertiesTest() {
        User user = new User(1L, "Jhon_doe", "password");
        assertEquals(Long.valueOf(1), user.getId());
        assertEquals("Jhon_doe", user.getUsername());
        assertEquals("password", user.getPassword());
    }
}
