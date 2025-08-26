package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.dto.LoanApplicationRequest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;

class LoanAppRepositoryTest {
    @Test
    void testRegisterMethodSignature() {
        LoanAppRepository repo = (loanApp) -> Mono.just(new LoanApplication());
        LoanApplicationRequest request = new LoanApplicationRequest();
        Mono<LoanApplication> result = repo.register(request);
        assertNotNull(result);
        assertNotNull(result.block());
    }
}
