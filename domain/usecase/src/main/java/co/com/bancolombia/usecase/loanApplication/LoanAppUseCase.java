package co.com.bancolombia.usecase.loanApplication;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.PageResponse;
import co.com.bancolombia.model.gateways.LoanAppGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class LoanAppUseCase {
    private final LoanAppGateway loanAppGateway;
    public Mono<LoanApplication> saveLoanApp(LoanApplication loanApp,String email){
        return loanAppGateway.register(loanApp,email);
    }
    public Mono<PageResponse<LoanApplication>> getLoanApps(int offset, int limit, int page) {
        return loanAppGateway.findAll(offset, limit, page);
    }
    public Mono<LoanApplication> updateLoanApp(Long id, String state) {
        return loanAppGateway.update(id, state);
    }


}