package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoanAppGatewayTest {

    @Test
    void testRegisterMethodSignature() {
        LoanAppGateway repo = new LoanAppGateway() {
            @Override
            public Mono<LoanApplication> register(LoanApplication loanApp, String email) {
                return Mono.just(new LoanApplication());
            }

            @Override
            public Flux<LoanApplication> findAll() {
                return Flux.just(new LoanApplication(), new LoanApplication());
            }
        };

        LoanApplication loanApplication = new LoanApplication();
        Mono<LoanApplication> result = repo.register(loanApplication, "test@email.com");

        assertNotNull(result);
        assertNotNull(result.block());

        // También probamos findAll
        assertNotNull(repo.findAll());
        assertNotNull(repo.findAll().collectList().block());
    }
}
