package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T13:48:44+0000",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.2.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class LoanTypeMapperImpl implements LoanTypeMapper {

    @Override
    public LoanTypeEntity toEntity(LoanType model) {
        if ( model == null ) {
            return null;
        }

        LoanTypeEntity loanTypeEntity = new LoanTypeEntity();

        loanTypeEntity.setId( model.getId() );
        loanTypeEntity.setName( model.getName() );
        loanTypeEntity.setMinimumAmount( model.getMinimumAmount() );
        loanTypeEntity.setMaximumAmount( model.getMaximumAmount() );
        loanTypeEntity.setInterestRate( model.getInterestRate() );
        loanTypeEntity.setAutomaticValidation( model.getAutomaticValidation() );

        return loanTypeEntity;
    }

    @Override
    public LoanType toModel(LoanTypeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LoanType.LoanTypeBuilder loanType = LoanType.builder();

        loanType.id( entity.getId() );
        loanType.name( entity.getName() );
        loanType.minimumAmount( entity.getMinimumAmount() );
        loanType.maximumAmount( entity.getMaximumAmount() );
        loanType.interestRate( entity.getInterestRate() );
        loanType.automaticValidation( entity.getAutomaticValidation() );

        return loanType.build();
    }
}
