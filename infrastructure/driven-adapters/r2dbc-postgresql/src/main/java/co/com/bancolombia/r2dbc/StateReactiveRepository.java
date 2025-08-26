package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StateReactiveRepository extends ReactiveCrudRepository<StateEntity, Long> {
}
