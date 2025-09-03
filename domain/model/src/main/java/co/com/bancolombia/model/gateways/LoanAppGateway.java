package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanAppGateway {
    Mono<LoanApplication> register(LoanApplication loanApp);
    Flux<LoanApplication> findAll();
}
