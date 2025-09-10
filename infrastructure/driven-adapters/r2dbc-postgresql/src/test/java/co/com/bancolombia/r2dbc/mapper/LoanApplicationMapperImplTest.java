package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationMapperImplTest {

    private final LoanApplicationMapperImpl mapper = new LoanApplicationMapperImpl();

    @Test
    void toEntity_shouldMapAllFields() {
        State state = State.builder().id(1L).name("Active").build();
        LoanType loanType = LoanType.builder().id(2L).name("Personal").build();

        LoanApplication model = LoanApplication.builder()
                .id(100L)
                .amount(new BigDecimal("5000"))
                .term(12)
                .email("test@example.com")
                .state(state)
                .loanType(loanType)
                .build();

        LoanApplicationEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getAmount(), entity.getAmount());
        assertEquals(model.getTerm(), entity.getTerm());
        assertEquals(model.getEmail(), entity.getEmail());
        assertEquals(state.getId(), entity.getStateId());
        assertEquals(loanType.getId(), entity.getLoanTypeId());
    }

    @Test
    void toModel_shouldMapAllFields() {
        LoanApplicationEntity entity = new LoanApplicationEntity();
        entity.setId(100L);
        entity.setAmount(new BigDecimal("5000"));
        entity.setTerm(12);
        entity.setEmail("test@example.com");
        entity.setStateId(1L);
        entity.setLoanTypeId(2L);

        LoanApplication model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getAmount(), model.getAmount());
        assertEquals(entity.getTerm(), model.getTerm());
        assertEquals(entity.getEmail(), model.getEmail());
        assertNotNull(model.getState());
        assertEquals(entity.getStateId(), model.getState().getId());
        assertNotNull(model.getLoanType());
        assertEquals(entity.getLoanTypeId(), model.getLoanType().getId());
    }

    @Test
    void toEntity_shouldHandleNullStateAndLoanType() {
        LoanApplication model = LoanApplication.builder()
                .id(100L)
                .amount(new BigDecimal("5000"))
                .term(12)
                .email("test@example.com")
                .build();

        LoanApplicationEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertNull(entity.getStateId());
        assertNull(entity.getLoanTypeId());
    }

    @Test
    void toModel_shouldHandleNullIds() {
        LoanApplicationEntity entity = new LoanApplicationEntity();
        entity.setId(100L);
        entity.setAmount(new BigDecimal("5000"));
        entity.setTerm(12);
        entity.setEmail("test@example.com");
        // stateId and loanTypeId are null

        LoanApplication model = mapper.toModel(entity);

        assertNotNull(model);
        assertNull(model.getState());
        assertNull(model.getLoanType());
    }

    @Test
    void toEntity_shouldReturnNullWhenModelIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toModel_shouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.toModel(null));
    }
}
