package cl.drcde.cqrs.domain.vo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UsernameTest {

    public static final String USERNAME = "USERNAME";

    @Test
    public void validateUsernameTest() {
        Username username = new Username(USERNAME);
        assertEquals(USERNAME, username.value());
    }
}
