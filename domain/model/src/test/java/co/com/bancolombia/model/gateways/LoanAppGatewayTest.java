package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.PageResponse;
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
            public Mono<PageResponse<LoanApplication>> findAll(int offset, int limit, int page) {
                return Mono.just(new PageResponse<>());
            }

            @Override
            public Mono<LoanApplication> update(Long id, String status) {
                return Mono.just(new LoanApplication());
            }
        };

        LoanApplication loanApplication = new LoanApplication();
        Mono<LoanApplication> result = repo.register(loanApplication, "test@email.com");

        assertNotNull(result);
        assertNotNull(result.block());

        // También probamos findAll
        assertNotNull(repo.findAll(0, 10, 1));
        assertNotNull(repo.findAll(0, 10, 1).block());
    }
}
