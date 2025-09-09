package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T16:34:38-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class StateMapperImpl implements StateMapper {

    @Override
    public StateEntity toEntity(State model) {
        if ( model == null ) {
            return null;
        }

        StateEntity stateEntity = new StateEntity();

        stateEntity.setDescription( model.getDescription() );
        stateEntity.setId( model.getId() );
        stateEntity.setName( model.getName() );

        return stateEntity;
    }

    @Override
    public State toModel(StateEntity entity) {
        if ( entity == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.description( entity.getDescription() );
        state.id( entity.getId() );
        state.name( entity.getName() );

        return state.build();
    }
}
