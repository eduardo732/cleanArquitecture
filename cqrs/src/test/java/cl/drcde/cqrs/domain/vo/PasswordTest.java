package cl.drcde.cqrs.domain.vo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PasswordTest {
    private final String PASSWORD = "PASSWORD";
    @Test
    public void validateTest() {
        Password password = new Password(PASSWORD);
        assertEquals(PASSWORD, password.value());
    }
}
