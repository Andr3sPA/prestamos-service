package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MissingHeaderExceptionTest {
    @Test
    void testConstructorAndMessage() {
        MissingHeaderException ex = new MissingHeaderException("X-Test-Header");
        assertEquals("MISSING_HEADER", ex.getErrorCode());
        assertEquals(400, ex.getStatus());
        assertTrue(ex.getMessage().contains("X-Test-Header"));
    }
}
