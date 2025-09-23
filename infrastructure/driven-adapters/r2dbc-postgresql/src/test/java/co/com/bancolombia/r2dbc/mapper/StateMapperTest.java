package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StateMapperTest {
    @Test
    void testToEntityAndToModel() {
        State model = State.builder().id(1L).name("PENDING").build();
        StateMapper mapper = new StateMapper() {
            @Override
            public StateEntity toEntity(State model) {
                StateEntity entity = new StateEntity();
                entity.setId(model.getId());
                entity.setName(model.getName());
                return entity;
            }
            @Override
            public State toModel(StateEntity entity) {
                return State.builder().id(entity.getId()).name(entity.getName()).build();
            }
        };
        StateEntity entity = mapper.toEntity(model);
        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        State modelBack = mapper.toModel(entity);
        assertNotNull(modelBack);
        assertEquals(entity.getId(), modelBack.getId());
        assertEquals(entity.getName(), modelBack.getName());
    }
}
