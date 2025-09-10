package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanTypeMapperImplTest {

    private final LoanTypeMapperImpl mapper = new LoanTypeMapperImpl();

    @Test
    void toEntity_shouldMapAllFields() {
        LoanType model = LoanType.builder()
                .id(1L)
                .name("Personal Loan")
                .minimumAmount(new BigDecimal("1000"))
                .maximumAmount(new BigDecimal("5000"))
                .interestRate(new BigDecimal("5.5"))
                .automaticValidation(true)
                .build();

        LoanTypeEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getMinimumAmount(), entity.getMinimumAmount());
        assertEquals(model.getMaximumAmount(), entity.getMaximumAmount());
        assertEquals(model.getInterestRate(), entity.getInterestRate());
        assertEquals(model.getAutomaticValidation(), entity.getAutomaticValidation());
    }

    @Test
    void toModel_shouldMapAllFields() {
        LoanTypeEntity entity = new LoanTypeEntity();
        entity.setId(1L);
        entity.setName("Personal Loan");
        entity.setMinimumAmount(new BigDecimal("1000"));
        entity.setMaximumAmount(new BigDecimal("5000"));
        entity.setInterestRate(new BigDecimal("5.5"));
        entity.setAutomaticValidation(true);

        LoanType model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getMinimumAmount(), model.getMinimumAmount());
        assertEquals(entity.getMaximumAmount(), model.getMaximumAmount());
        assertEquals(entity.getInterestRate(), model.getInterestRate());
        assertEquals(entity.getAutomaticValidation(), model.getAutomaticValidation());
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
