package cl.drcde.cqrs.domain.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TaskTest {
    @Test
    public void taskPropertiesTest() {
        Task task = new Task(1L, "Do something", false);

        assertEquals(Long.valueOf(1), task.getId());
        assertEquals("Do something", task.getDescription());
        assertEquals(Boolean.FALSE, task.getIsCompleted());
    }
}
