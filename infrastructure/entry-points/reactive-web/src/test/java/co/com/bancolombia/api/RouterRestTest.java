package co.com.bancolombia.api;

import co.com.bancolombia.api.config.LoanAppPath;
import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, HandlerLoanApp.class})
@EnableConfigurationProperties(LoanAppPath.class)
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LoanAppUseCase loanAppUseCase;

    @Autowired
    private LoanAppPath loanAppPath;

    private final LoanApplicationRequest request = LoanApplicationRequest.builder()
            .amount(BigDecimal.valueOf(5000))
            .term(12)
            .email("client1@example.com")
            .stateId(1L)
            .loanTypeId(1L)
            .build();

    private final LoanApplication responseLoan = LoanApplication.builder()
            .id(1L)
            .amount(BigDecimal.valueOf(5000))
            .term(12)
            .email("client1@example.com")
            .state(State.builder()
                    .id(1L)
                    .name("PENDING")
                    .description("Solicitud pendiente")
                    .build())
            .loanType(LoanType.builder()
                    .id(1L)
                    .name("PERSONAL")
                    .minimumAmount(BigDecimal.valueOf(1000))
                    .maximumAmount(BigDecimal.valueOf(10000))
                    .interestRate(BigDecimal.valueOf(0.05))
                    .automaticValidation(true)
                    .build())
            .build();

    @Test
    void shouldCreateLoanApplication() {
        // CORRECCIÓN: Mock con ambos parámetros
        when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), anyString()))
                .thenReturn(Mono.just(responseLoan));

        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                // CORRECCIÓN: Agregar el header requerido
                .header("X-User-Email", "client1@example.com")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanApplication.class)
                .consumeWith(res -> {
                    LoanApplication result = res.getResponseBody();
                    assert result != null;
                    Assertions.assertThat(result.getId()).isEqualTo(1L);
                    Assertions.assertThat(result.getState().getId()).isEqualTo(1L);
                    Assertions.assertThat(result.getLoanType().getId()).isEqualTo(1L);
                    Assertions.assertThat(result.getLoanType().getName()).isEqualTo("PERSONAL");
                });
    }

    @Test
    void shouldReturnBadRequestWhenInvalidRequest() {
        LoanApplicationRequest invalidRequest = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(-1000))
                .term(0)
                .email("correo-no-valido")
                .stateId(null)
                .loanTypeId(null)
                .build();

        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                // CORRECCIÓN: Agregar header incluso en test de error
                .header("X-User-Email", "correo-no-valido")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenMissingHeader() {
        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                // Sin header X-User-Email
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenEmailMismatch() {
        // CORRECCIÓN: Mock con ambos parámetros
        when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), anyString()))
                .thenReturn(Mono.just(responseLoan));

        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                // Header con email diferente al del body
                .header("X-User-Email", "different@example.com")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
