package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-09T20:54:54+0000",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.2.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class StateMapperImpl implements StateMapper {

    @Override
    public StateEntity toEntity(State model) {
        if ( model == null ) {
            return null;
        }

        StateEntity stateEntity = new StateEntity();

        stateEntity.setId( model.getId() );
        stateEntity.setName( model.getName() );
        stateEntity.setDescription( model.getDescription() );

        return stateEntity;
    }

    @Override
    public State toModel(StateEntity entity) {
        if ( entity == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.id( entity.getId() );
        state.name( entity.getName() );
        state.description( entity.getDescription() );

        return state.build();
    }
}
