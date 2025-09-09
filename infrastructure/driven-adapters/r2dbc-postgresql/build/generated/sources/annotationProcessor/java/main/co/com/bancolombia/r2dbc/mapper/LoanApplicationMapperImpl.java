package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T20:02:15-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class LoanApplicationMapperImpl implements LoanApplicationMapper {

    @Override
    public LoanApplicationEntity toEntity(LoanApplication model) {
        if ( model == null ) {
            return null;
        }

        LoanApplicationEntity loanApplicationEntity = new LoanApplicationEntity();

        loanApplicationEntity.setStateId( modelStateId( model ) );
        loanApplicationEntity.setLoanTypeId( modelLoanTypeId( model ) );
        loanApplicationEntity.setId( model.getId() );
        loanApplicationEntity.setAmount( model.getAmount() );
        loanApplicationEntity.setTerm( model.getTerm() );
        loanApplicationEntity.setEmail( model.getEmail() );

        return loanApplicationEntity;
    }

    @Override
    public LoanApplication toModel(LoanApplicationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LoanApplication.LoanApplicationBuilder loanApplication = LoanApplication.builder();

        loanApplication.state( loanApplicationEntityToState( entity ) );
        loanApplication.loanType( loanApplicationEntityToLoanType( entity ) );
        loanApplication.id( entity.getId() );
        loanApplication.amount( entity.getAmount() );
        loanApplication.term( entity.getTerm() );
        loanApplication.email( entity.getEmail() );

        return loanApplication.build();
    }

    private Long modelStateId(LoanApplication loanApplication) {
        if ( loanApplication == null ) {
            return null;
        }
        State state = loanApplication.getState();
        if ( state == null ) {
            return null;
        }
        Long id = state.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long modelLoanTypeId(LoanApplication loanApplication) {
        if ( loanApplication == null ) {
            return null;
        }
        LoanType loanType = loanApplication.getLoanType();
        if ( loanType == null ) {
            return null;
        }
        Long id = loanType.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected State loanApplicationEntityToState(LoanApplicationEntity loanApplicationEntity) {
        if ( loanApplicationEntity == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.id( loanApplicationEntity.getStateId() );

        return state.build();
    }

    protected LoanType loanApplicationEntityToLoanType(LoanApplicationEntity loanApplicationEntity) {
        if ( loanApplicationEntity == null ) {
            return null;
        }

        LoanType.LoanTypeBuilder loanType = LoanType.builder();

        loanType.id( loanApplicationEntity.getLoanTypeId() );

        return loanType.build();
    }
}
