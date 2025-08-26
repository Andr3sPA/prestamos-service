package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.dto.LoanApplicationRequest;
import reactor.core.publisher.Mono;

public interface LoanAppRepository {
    Mono<LoanApplication> register(LoanApplicationRequest loanApp);
}
