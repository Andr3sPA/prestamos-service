package co.com.bancolombia.usecase.loanApplication;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.gateways.LoanAppGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanAppUseCase {
    private final LoanAppGateway loanAppGateway;
    public Mono<LoanApplication> saveLoanApp(LoanApplication loanApp){
        return loanAppGateway.register(loanApp);
    }
    public Flux<LoanApplication> getLoanApps(){
        return loanAppGateway.findAll();
    }
}