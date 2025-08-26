package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StateMapper {
    
    StateEntity toEntity(State model);
    
    State toModel(StateEntity entity);
}
