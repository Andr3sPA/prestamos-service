package co.com.bancolombia.r2dbc;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LoanAppReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, String>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {
    Mono<LoanApplicationEntity> save(LoanApplication loanApp);
}