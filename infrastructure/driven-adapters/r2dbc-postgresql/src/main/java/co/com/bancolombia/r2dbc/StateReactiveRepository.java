package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StateReactiveRepository extends ReactiveCrudRepository<StateEntity, Long> {
    Mono<StateEntity> findByName(String name);
}
