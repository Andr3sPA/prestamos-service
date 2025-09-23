package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanTypeMapperTest {
    @Test
    void testToEntityAndToModel() {
        LoanType model = LoanType.builder().id(1L).name("PERSONAL").build();
        LoanTypeMapper mapper = new LoanTypeMapper() {
            @Override
            public LoanTypeEntity toEntity(LoanType model) {
                LoanTypeEntity entity = new LoanTypeEntity();
                entity.setId(model.getId());
                entity.setName(model.getName());
                return entity;
            }
            @Override
            public LoanType toModel(LoanTypeEntity entity) {
                return LoanType.builder().id(entity.getId()).name(entity.getName()).build();
            }
        };
        LoanTypeEntity entity = mapper.toEntity(model);
        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        LoanType modelBack = mapper.toModel(entity);
        assertNotNull(modelBack);
        assertEquals(entity.getId(), modelBack.getId());
        assertEquals(entity.getName(), modelBack.getName());
    }
}
