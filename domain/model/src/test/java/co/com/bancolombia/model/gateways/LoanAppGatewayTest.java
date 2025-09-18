package co.com.bancolombia.model.gateways;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.PageResponse;
import co.com.bancolombia.model.request.CalcularCapacidadRequest;
import co.com.bancolombia.model.response.CalcularCapacidadResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanAppGatewayTest {

    @Test
    void testRegisterAndFindAllMethodSignatures() {
        LoanAppGateway repo = new LoanAppGateway() {
            @Override
            public Mono<LoanApplication> register(LoanApplication loanApp, String email) {
                return Mono.just(new LoanApplication());
            }

            @Override
            public Mono<PageResponse<LoanApplication>> findAll(int offset, int limit, int page) {
                PageResponse<LoanApplication> pageResponse = new PageResponse<>();
                pageResponse.setContent(List.of(new LoanApplication()));
                pageResponse.setTotalElements(1);
                pageResponse.setPage(page);
                pageResponse.setSize(limit);
                return Mono.just(pageResponse);
            }

            @Override
            public Mono<LoanApplication> update(Long id, String name) {
                return Mono.just(new LoanApplication());
            }

            @Override
            public Mono<CalcularCapacidadResponse> calcularEndeudamiento(Long id, CalcularCapacidadRequest.UserDTO userDTO) {
                CalcularCapacidadResponse response = new CalcularCapacidadResponse();
                response.setDecision("APROBADO");
                return Mono.just(response);
            }
        };

        LoanApplication loanApplication = new LoanApplication();
        Mono<LoanApplication> result = repo.register(loanApplication, "test@email.com");
        assertNotNull(result);
        assertNotNull(result.block());

        Mono<PageResponse<LoanApplication>> pageResult = repo.findAll(0, 10, 1);
        assertNotNull(pageResult);
        assertNotNull(pageResult.block());

        Mono<LoanApplication> updateResult = repo.update(1L, "NuevoNombre");
        assertNotNull(updateResult);
        assertNotNull(updateResult.block());

        CalcularCapacidadRequest.UserDTO userDTO = new CalcularCapacidadRequest.UserDTO();
        Mono<CalcularCapacidadResponse> capacidadResult = repo.calcularEndeudamiento(1L, userDTO);
        assertNotNull(capacidadResult);
        assertEquals("APROBADO", capacidadResult.block().getDecision());
    }
}
