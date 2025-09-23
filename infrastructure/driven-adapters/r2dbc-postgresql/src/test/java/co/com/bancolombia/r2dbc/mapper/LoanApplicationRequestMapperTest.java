package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.model.LoanApplication;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class LoanApplicationRequestMapperTest {
    @Test
    void testToModel() {
        LoanApplicationRequest dto = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(1000.0))
                .term(12)
                .stateId(1L)
                .loanTypeId(2L)
                .email("test@test.com")
                .build();

        LoanApplicationRequestMapper mapper = new LoanApplicationRequestMapper();
        LoanApplication model = mapper.toModel(dto);
        assertNotNull(model);
        assertEquals(dto.getAmount(), model.getAmount());
        assertEquals(dto.getTerm(), model.getTerm());
        assertEquals(dto.getEmail(), model.getEmail());
        assertNotNull(model.getState());
        assertEquals(dto.getStateId(), model.getState().getId());
        assertNotNull(model.getLoanType());
        assertEquals(dto.getLoanTypeId(), model.getLoanType().getId());
    }
}
