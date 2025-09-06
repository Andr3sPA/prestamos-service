package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;

import co.com.bancolombia.model.PageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanAppGateway {
    Mono<LoanApplication> register(LoanApplication loanApp,String email);
    Mono<PageResponse<LoanApplication>> findAll(int offset, int limit, int page);
    Mono<LoanApplication> update(Long id,String name);
}
