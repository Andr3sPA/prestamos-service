package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanApplicationNotFoundExceptionTest {
    @Test
    void testConstructorAndMessage() {
        Long id = 123L;
        LoanApplicationNotFoundException ex = new LoanApplicationNotFoundException(id);
        assertTrue(ex.getMessage().contains("id: 123"));
        assertEquals("LOAN_APPLICATION_NOT_FOUND", ex.getErrorCode());
        assertEquals(404, ex.getStatus());
    }
}
