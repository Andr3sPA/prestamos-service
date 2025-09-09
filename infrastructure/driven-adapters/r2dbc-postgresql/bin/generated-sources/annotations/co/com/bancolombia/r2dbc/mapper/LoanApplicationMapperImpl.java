package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T16:34:41-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
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
        loanApplicationEntity.setAmount( model.getAmount() );
        loanApplicationEntity.setEmail( model.getEmail() );
        loanApplicationEntity.setId( model.getId() );
        loanApplicationEntity.setTerm( model.getTerm() );

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
        loanApplication.amount( entity.getAmount() );
        loanApplication.email( entity.getEmail() );
        loanApplication.id( entity.getId() );
        loanApplication.term( entity.getTerm() );

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
