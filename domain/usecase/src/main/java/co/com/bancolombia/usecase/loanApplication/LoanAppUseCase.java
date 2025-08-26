package co.com.bancolombia.usecase.loanApplication;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.dto.LoanApplicationRequest;
import co.com.bancolombia.model.gateways.LoanAppRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanAppUseCase {
    private final LoanAppRepository loanAppRepository;
    public Mono<LoanApplication> saveLoanApp(LoanApplicationRequest loanApp){
        return loanAppRepository.register(loanApp);
    }
}
