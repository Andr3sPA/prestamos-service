package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationNotFoundExceptionTest {

    @Test
    void testExceptionProperties() {
        Long id = 123L;
        LoanApplicationNotFoundException ex = new LoanApplicationNotFoundException(id);

        assertEquals("LOAN_APPLICATION_NOT_FOUND", ex.getErrorCode());
        assertEquals("Solicitud de préstamo no encontrada", ex.getTitle());
        assertEquals("No existe la solicitud con id: " + id, ex.getMessage());
        assertTrue(ex.getErrors().contains("No existe la solicitud con id: " + id));
        assertEquals(404, ex.getStatus());
    }
}
