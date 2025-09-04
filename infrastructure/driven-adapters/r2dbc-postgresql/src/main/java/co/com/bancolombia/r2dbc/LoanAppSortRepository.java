package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface LoanAppSortRepository extends ReactiveSortingRepository<LoanApplicationEntity, Long> {
    Flux<LoanApplication> findAllBy(Pageable pageable);

}
