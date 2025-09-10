package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateMapperImplTest {

    private final StateMapperImpl mapper = new StateMapperImpl();

    @Test
    void toEntity_shouldMapAllFields() {
        State model = State.builder()
                .id(1L)
                .name("Active")
                .description("Application is active")
                .build();

        StateEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getDescription(), entity.getDescription());
    }

    @Test
    void toModel_shouldMapAllFields() {
        StateEntity entity = new StateEntity();
        entity.setId(1L);
        entity.setName("Active");
        entity.setDescription("Application is active");

        State model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDescription(), model.getDescription());
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
