package co.com.bancolombia.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class LoanTypeTest {
    @Test
    void testLoanTypeBuilderAndFields() {
        LoanType loanType = LoanType.builder()
                .id(1L)
                .name("Personal")
                .minimumAmount(BigDecimal.valueOf(1000))
                .maximumAmount(BigDecimal.valueOf(10000))
                .interestRate(BigDecimal.valueOf(5.5))
                .automaticValidation(true)
                .build();
        assertEquals(1L, loanType.getId());
        assertEquals("Personal", loanType.getName());
        assertEquals(BigDecimal.valueOf(1000), loanType.getMinimumAmount());
        assertEquals(BigDecimal.valueOf(10000), loanType.getMaximumAmount());
        assertEquals(BigDecimal.valueOf(5.5), loanType.getInterestRate());
        assertTrue(loanType.getAutomaticValidation());
    }
}
