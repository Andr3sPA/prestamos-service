package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T16:34:42-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class LoanTypeMapperImpl implements LoanTypeMapper {

    @Override
    public LoanTypeEntity toEntity(LoanType model) {
        if ( model == null ) {
            return null;
        }

        LoanTypeEntity loanTypeEntity = new LoanTypeEntity();

        loanTypeEntity.setAutomaticValidation( model.getAutomaticValidation() );
        loanTypeEntity.setId( model.getId() );
        loanTypeEntity.setInterestRate( model.getInterestRate() );
        loanTypeEntity.setMaximumAmount( model.getMaximumAmount() );
        loanTypeEntity.setMinimumAmount( model.getMinimumAmount() );
        loanTypeEntity.setName( model.getName() );

        return loanTypeEntity;
    }

    @Override
    public LoanType toModel(LoanTypeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LoanType.LoanTypeBuilder loanType = LoanType.builder();

        loanType.automaticValidation( entity.getAutomaticValidation() );
        loanType.id( entity.getId() );
        loanType.interestRate( entity.getInterestRate() );
        loanType.maximumAmount( entity.getMaximumAmount() );
        loanType.minimumAmount( entity.getMinimumAmount() );
        loanType.name( entity.getName() );

        return loanType.build();
    }
}
