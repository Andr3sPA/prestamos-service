package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailMismatchExceptionTest {
    @Test
    void testConstructorAndMessage() {
        EmailMismatchException ex = new EmailMismatchException("req@test.com", "session@test.com");
        assertEquals("EMAIL_MISMATCH", ex.getErrorCode());
        assertEquals(400, ex.getStatus());
        assertTrue(ex.getMessage().contains("req@test.com"));
        assertTrue(ex.getMessage().contains("session@test.com"));
    }
}
