package co.com.bancolombia.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class LoanApplicationRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

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

    @Test
    void validationPassesForValidRequest() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@email.com")
                .loanTypeId(2L)
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validationFailsForNullAmount() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .term(12)
                .email("test@email.com")
                .loanTypeId(2L)
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("amount", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void validationFailsForNegativeAmount() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("-1000"))
                .term(12)
                .email("test@email.com")
                .loanTypeId(2L)
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }

    @Test
    void validationFailsForAmountTooHigh() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("20000000"))
                .term(12)
                .email("test@email.com")
                .loanTypeId(2L)
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }

    @Test
    void validationFailsForNullTerm() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .email("test@email.com")
                .loanTypeId(2L)
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("term", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void validationFailsForNegativeTerm() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(-1)
                .email("test@email.com")
                .loanTypeId(2L)
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("term")));
    }

    @Test
    void validationFailsForNullEmail() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .loanTypeId(2L)
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("email", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void validationFailsForInvalidEmail() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("invalid-email")
                .loanTypeId(2L)
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validationFailsForNullLoanTypeId() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@email.com")
                .build();

        Set<ConstraintViolation<LoanApplicationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("loanTypeId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void toStringWorks() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@email.com")
                .stateId(1L)
                .loanTypeId(2L)
                .build();

        String toString = request.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("10000"));
        assertTrue(toString.contains("12"));
        assertTrue(toString.contains("test@email.com"));
    }

    @Test
    void equalsAndHashCodeWork() {
        LoanApplicationRequest request1 = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@email.com")
                .stateId(1L)
                .loanTypeId(2L)
                .build();

        LoanApplicationRequest request2 = LoanApplicationRequest.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@email.com")
                .stateId(1L)
                .loanTypeId(2L)
                .build();

        LoanApplicationRequest request3 = LoanApplicationRequest.builder()
                .amount(new BigDecimal("20000"))
                .term(12)
                .email("test@email.com")
                .stateId(1L)
                .loanTypeId(2L)
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
    }
}
