package co.com.bancolombia.api.util;

import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
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
    void shouldMapDtoToModel() {
        LoanApplicationRequest dto = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(5000))
                .term(12)
                .email("test@example.com")
                .stateId(1L)
                .loanTypeId(2L)
                .build();

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

    @Test
    void shouldMapDtoWithNullValues() {
        LoanApplicationRequest dto = LoanApplicationRequest.builder()
                .amount(null)
                .term(0)
                .email(null)
                .stateId(null)
                .loanTypeId(null)
                .build();

        LoanApplication model = mapper.toModel(dto);

        assertNotNull(model);
        assertNull(model.getAmount());
        assertEquals(0, model.getTerm());
        assertNull(model.getEmail());
        assertNotNull(model.getState());
        assertNull(model.getState().getId());
        assertNotNull(model.getLoanType());
        assertNull(model.getLoanType().getId());
    }

    @Test
    void shouldMapDtoWithLargeValues() {
        LoanApplicationRequest dto = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(360)
                .email("large@example.com")
                .stateId(Long.MAX_VALUE)
                .loanTypeId(Long.MAX_VALUE)
                .build();

        LoanApplication model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals(BigDecimal.valueOf(1000000), model.getAmount());
        assertEquals(360, model.getTerm());
        assertEquals("large@example.com", model.getEmail());
        assertEquals(Long.MAX_VALUE, model.getState().getId());
        assertEquals(Long.MAX_VALUE, model.getLoanType().getId());
    }
}
