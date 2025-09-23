package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.time.Instant;
public class BaseExceptionTest {
    @Test
    void testBaseExceptionFields() {
        BaseException ex = new BaseException(
                "CODE",
                "Título",
                "Mensaje",
                List.of("error1", "error2"),
                400
        );
        assertEquals("CODE", ex.getErrorCode());
        assertEquals("Título", ex.getTitle());
        assertEquals("Mensaje", ex.getMessage());
        assertEquals(List.of("error1", "error2"), ex.getErrors());
        assertEquals(400, ex.getStatus());
        assertNotNull(ex.getTimestamp());
    }
}
