package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "loanType.id", target = "loanTypeId")
    LoanApplicationEntity toEntity(LoanApplication model);

    @Mapping(source = "stateId", target = "state.id")
    @Mapping(source = "loanTypeId", target = "loanType.id")
    LoanApplication toModel(LoanApplicationEntity entity);
}
