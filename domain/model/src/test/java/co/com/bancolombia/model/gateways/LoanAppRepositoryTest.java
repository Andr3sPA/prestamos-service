package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoanAppRepositoryTest {

    @Test
    void testRegisterMethodSignature() {
        LoanAppRepository repo = (loanApp) -> Mono.just(new LoanApplication());

        LoanApplication loanApplication = new LoanApplication(); // ya no LoanApplicationRequest
        Mono<LoanApplication> result = repo.register(loanApplication);

        assertNotNull(result);
        assertNotNull(result.block());
    }
}
