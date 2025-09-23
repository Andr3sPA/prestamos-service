package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanApplicationMapperTest {
    @Test
    void testToEntityAndToModel() {
        LoanApplication model = LoanApplication.builder().id(1L).build();
        LoanApplicationMapper mapper = new LoanApplicationMapper() {
            @Override
            public LoanApplicationEntity toEntity(LoanApplication model) {
                LoanApplicationEntity entity = new LoanApplicationEntity();
                entity.setId(model.getId());
                return entity;
            }
            @Override
            public LoanApplication toModel(LoanApplicationEntity entity) {
                return LoanApplication.builder().id(entity.getId()).build();
            }
        };
        LoanApplicationEntity entity = mapper.toEntity(model);
        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        LoanApplication modelBack = mapper.toModel(entity);
        assertNotNull(modelBack);
        assertEquals(entity.getId(), modelBack.getId());
    }
}
