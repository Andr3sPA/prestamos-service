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

    @Test
    void testLoanApplicationSettersAndGetters() {
        LoanApplication loanApp = new LoanApplication();
        State state = State.builder().id(1L).name("Pending").description("Pending approval").build();
        LoanType loanType = LoanType.builder().id(2L).name("Business").build();

        loanApp.setId(100L);
        loanApp.setAmount(BigDecimal.valueOf(10000));
        loanApp.setTerm(24);
        loanApp.setEmail("test@example.com");
        loanApp.setState(state);
        loanApp.setLoanType(loanType);

        assertEquals(100L, loanApp.getId());
        assertEquals(BigDecimal.valueOf(10000), loanApp.getAmount());
        assertEquals(24, loanApp.getTerm());
        assertEquals("test@example.com", loanApp.getEmail());
        assertEquals(state, loanApp.getState());
        assertEquals(loanType, loanApp.getLoanType());
    }

    @Test
    void testLoanApplicationNoArgsConstructor() {
        LoanApplication loanApp = new LoanApplication();
        assertNull(loanApp.getId());
        assertNull(loanApp.getAmount());
        assertEquals(0, loanApp.getTerm());
        assertNull(loanApp.getEmail());
        assertNull(loanApp.getState());
        assertNull(loanApp.getLoanType());
    }

    @Test
    void testLoanApplicationAllArgsConstructor() {
        State state = State.builder().id(1L).name("Approved").description("Approved state").build();
        LoanType loanType = LoanType.builder().id(2L).name("Personal").build();

        LoanApplication loanApp = new LoanApplication(
                50L,
                BigDecimal.valueOf(7500),
                18,
                "constructor@example.com",
                state,
                loanType
        );

        assertEquals(50L, loanApp.getId());
        assertEquals(BigDecimal.valueOf(7500), loanApp.getAmount());
        assertEquals(18, loanApp.getTerm());
        assertEquals("constructor@example.com", loanApp.getEmail());
        assertEquals(state, loanApp.getState());
        assertEquals(loanType, loanApp.getLoanType());
    }

    @Test
    void testLoanApplicationWithNullValues() {
        LoanApplication loanApp = LoanApplication.builder()
                .id(null)
                .amount(null)
                .term(0)
                .email(null)
                .state(null)
                .loanType(null)
                .build();

        assertNull(loanApp.getId());
        assertNull(loanApp.getAmount());
        assertEquals(0, loanApp.getTerm());
        assertNull(loanApp.getEmail());
        assertNull(loanApp.getState());
        assertNull(loanApp.getLoanType());
    }

    @Test
    void testLoanApplicationPartialBuilder() {
        LoanApplication loanApp = LoanApplication.builder()
                .id(5L)
                .email("partial@example.com")
                .build();

        assertEquals(5L, loanApp.getId());
        assertNull(loanApp.getAmount());
        assertEquals(0, loanApp.getTerm());
        assertEquals("partial@example.com", loanApp.getEmail());
        assertNull(loanApp.getState());
        assertNull(loanApp.getLoanType());
    }
}
