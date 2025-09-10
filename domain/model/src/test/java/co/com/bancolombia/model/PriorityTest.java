package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityTest {

    @Test
    void testEnumValues() {
        Priority[] priorities = Priority.values();
        assertEquals(3, priorities.length);
        assertEquals(Priority.LOW, priorities[0]);
        assertEquals(Priority.MEDIUM, priorities[1]);
        assertEquals(Priority.HIGH, priorities[2]);
    }
}
