package co.com.bancolombia.model.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationRequestTest {
    @Test
    void testLoanApplicationRequestBuilderAndFields() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(5000))
                .term(12)
                .email("user@example.com")
                .stateId(1L)
                .loanTypeId(2L)
                .build();
        assertEquals(BigDecimal.valueOf(5000), request.getAmount());
        assertEquals(12, request.getTerm());
        assertEquals("user@example.com", request.getEmail());
        assertEquals(1L, request.getStateId());
        assertEquals(2L, request.getLoanTypeId());
    }
}
