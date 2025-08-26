package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationTest {
    @Test
    void testLoanApplicationBuilderAndFields() {
        State state = State.builder().id(1L).name("Active").description("Active state").build();
        LoanType loanType = LoanType.builder().id(2L).name("Personal").build();
        LoanApplication loanApp = LoanApplication.builder()
                .id(10L)
                .amount(BigDecimal.valueOf(5000))
                .term(12)
                .email("user@example.com")
                .state(state)
                .loanType(loanType)
                .build();
        assertEquals(10L, loanApp.getId());
        assertEquals(BigDecimal.valueOf(5000), loanApp.getAmount());
        assertEquals(12, loanApp.getTerm());
        assertEquals("user@example.com", loanApp.getEmail());
        assertEquals(state, loanApp.getState());
        assertEquals(loanType, loanApp.getLoanType());
    }
}
