package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, Long> {
}
