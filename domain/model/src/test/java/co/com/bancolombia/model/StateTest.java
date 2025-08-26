package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StateTest {
    @Test
    void testStateBuilderAndFields() {
        State state = State.builder()
                .id(1L)
                .name("Active")
                .description("State is active")
                .build();
        assertEquals(1L, state.getId());
        assertEquals("Active", state.getName());
        assertEquals("State is active", state.getDescription());
    }
}
