package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;

import reactor.core.publisher.Mono;

public interface LoanAppRepository {
    Mono<LoanApplication> register(LoanApplication loanApp);
}
