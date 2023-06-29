package cl.drcde.cqrs.domain.vo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UUIDv4Test {

    public static final String PASSWORD = "PASSWORD";
    public static final String VALUE = UUIDv4.generate().value();

    @Test
    public void validateIdTest() {
        UUIDv4 uuiDv4 = new UUIDv4(VALUE);
        assertEquals(VALUE, uuiDv4.value());
    }
    @Test
    public void generateTest() {
        UUIDv4 uuiDv4 = UUIDv4.generate();
        String value = uuiDv4.value();
        assertEquals(value, uuiDv4.value());
    }
}
