package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.model.LoanApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationRequestMapperTest {

    private LoanApplicationRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LoanApplicationRequestMapper();
    }

    @Test
    void shouldMapRequestToModel() {
        // Arrange
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(5000))
                .term(12)
                .email("test@example.com")
                .stateId(1L)
                .loanTypeId(2L)
                .build();

        // Act
        LoanApplication model = mapper.toModel(request);

        // Assert
        assertNotNull(model);
        assertEquals(request.getAmount(), model.getAmount());
        assertEquals(request.getTerm(), model.getTerm());
        assertEquals(request.getEmail(), model.getEmail());
        assertEquals(request.getStateId(), model.getState().getId());
        assertEquals(request.getLoanTypeId(), model.getLoanType().getId());
    }
}
