package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MissingEmailHeaderExceptionTest {
    @Test
    void testConstructorAndMessage() {
        MissingEmailHeaderException ex = new MissingEmailHeaderException();
        assertEquals("MISSING_EMAIL_HEADER", ex.getErrorCode());
        assertEquals(400, ex.getStatus());
        assertTrue(ex.getMessage().contains("X-User-Email"));
    }
}
