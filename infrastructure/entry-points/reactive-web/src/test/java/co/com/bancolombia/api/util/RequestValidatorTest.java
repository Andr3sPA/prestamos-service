package co.com.bancolombia.api.util;

import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.exception.MissingFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RequestValidatorTest {

    private RequestValidator requestValidator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        requestValidator = new RequestValidator(validator);
    }

    @Test
    void shouldValidateCorrectRequest() {
        LoanApplicationRequest validRequest = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(5000))
                .term(12)
                .email("test@example.com")
                .stateId(1L)
                .loanTypeId(1L)
                .build();

        assertDoesNotThrow(() -> 
            requestValidator.validate(validRequest, LoanApplicationRequest.class));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        LoanApplicationRequest invalidRequest = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(5000))
                .term(12)
                .email("invalid-email")
                .stateId(1L)
                .loanTypeId(1L)
                .build();

        assertThrows(MissingFieldException.class, () -> 
            requestValidator.validate(invalidRequest, LoanApplicationRequest.class));
    }

    @Test
    void shouldThrowExceptionForNullAmount() {
        LoanApplicationRequest invalidRequest = LoanApplicationRequest.builder()
                .amount(null)
                .term(12)
                .email("test@example.com")
                .stateId(1L)
                .loanTypeId(1L)
                .build();

        assertThrows(MissingFieldException.class, () -> 
            requestValidator.validate(invalidRequest, LoanApplicationRequest.class));
    }
}
