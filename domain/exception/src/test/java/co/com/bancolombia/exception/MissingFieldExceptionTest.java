package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MissingFieldExceptionTest {
    @Test
    void testConstructorAndMessage() {
        MissingFieldException ex = new MissingFieldException("campoX");
        assertEquals("MISSING_FIELD", ex.getErrorCode());
        assertEquals(400, ex.getStatus());
        assertTrue(ex.getMessage().contains("campoX"));
    }
}
