package co.com.bancolombia.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationRequestTest {
    @Test
    void builderAndGettersWork() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@email.com")
                .stateId(1L)
                .loanTypeId(2L)
                .build();

        assertEquals(new BigDecimal("10000"), request.getAmount());
        assertEquals(12, request.getTerm());
        assertEquals("test@email.com", request.getEmail());
        assertEquals(1L, request.getStateId());
        assertEquals(2L, request.getLoanTypeId());
    }

    @Test
    void noArgsConstructorWorks() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        assertNull(request.getAmount());
        assertNull(request.getTerm());
        assertNull(request.getEmail());
        assertNull(request.getStateId());
        assertNull(request.getLoanTypeId());
    }

    @Test
    void allArgsConstructorWorks() {
        LoanApplicationRequest request = new LoanApplicationRequest(
                new BigDecimal("5000"), 6, "user@email.com", 3L, 4L
        );
        assertEquals(new BigDecimal("5000"), request.getAmount());
        assertEquals(6, request.getTerm());
        assertEquals("user@email.com", request.getEmail());
        assertEquals(3L, request.getStateId());
        assertEquals(4L, request.getLoanTypeId());
    }
}
