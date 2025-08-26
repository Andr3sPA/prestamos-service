package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanTypeMapper {
    
    LoanTypeEntity toEntity(LoanType model);
    
    LoanType toModel(LoanTypeEntity entity);
}
