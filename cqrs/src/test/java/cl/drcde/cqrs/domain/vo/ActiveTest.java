package cl.drcde.cqrs.domain.vo;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActiveTest {

    public static final Boolean TRUE = Boolean.TRUE;
    public static final Boolean FALSE = Boolean.FALSE;

    @Test
    public void activeVOTest(){
        Active active = new Active(TRUE);
        assertEquals(Boolean.TRUE, active.value());
    }
    @Test
    public void onOffTest(){
        Active active = new Active(TRUE);
        active.onOff();
        assertEquals(FALSE, active.value());
    }
}
