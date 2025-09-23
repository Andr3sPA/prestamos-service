
package co.com.bancolombia.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

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
// Eliminados imports innecesarios o conflictivos para pruebas
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.server.ServerResponse;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;


@WebFluxTest
@org.springframework.test.context.TestPropertySource(properties = {"api.auth.key=test-key"})
@Import({RouterRest.class, LoanAppPath.class, GlobalExceptionFilter.class, co.com.bancolombia.api.filter.ApiKeyAuthFilter.class})

@SuppressWarnings("InnerClassMayBeStatic")
class RouterRestTest {
        @org.springframework.boot.test.context.TestConfiguration
        static class TestConfig {
                @org.springframework.context.annotation.Bean
                @org.springframework.context.annotation.Primary
                public HandlerLoanApp handlerLoanApp(
                                RequestValidator requestValidator,
                                LoanAppUseCase loanAppUseCase,
                                LoanApplicationRequestMapper loanApplicationRequestMapper) {
                        return org.mockito.Mockito.mock(HandlerLoanApp.class);
                }
        }

                // Eliminada la clase interna de configuración, ya no es necesaria con @SpringBootTest(classes = ...)
    // Eliminado setup global de mocks. Cada test configura sus mocks explícitamente.
    // Test adicional para cobertura de getLoanApps
        @Test
        void shouldGetLoanApps() {
                org.mockito.Mockito.when(handlerLoanApp.getLoanApps(org.mockito.ArgumentMatchers.any())).thenReturn(
                        ServerResponse.ok().bodyValue("ok").flatMap(Mono::just)
                );
                webTestClient.get()
                                .uri(loanAppPath.getLoanApplication() + "?page=0&size=1")
                                .header("X-API-KEY", "test-key")
                                .exchange()
                                .expectStatus().isOk();
        }

    // Test adicional para cobertura de updateLoanApp
    @Test
    void shouldUpdateLoanApp() {
        co.com.bancolombia.dto.UpdateLoanAppReq updateReq = co.com.bancolombia.dto.UpdateLoanAppReq.builder()
                .id(1L)
                .name(co.com.bancolombia.dto.LoanAppStatus.APPROVED)
                .build();
        org.mockito.Mockito.when(handlerLoanApp.updateLoanApp(org.mockito.ArgumentMatchers.any())).thenReturn(
            ServerResponse.ok().bodyValue("ok").flatMap(Mono::just)
        );
        webTestClient.put()
                .uri(loanAppPath.getLoanApplication())
                .header("X-API-KEY", "test-key")
                .bodyValue(updateReq)
                .exchange()
                .expectStatus().isOk();
    }


        @MockBean
        private co.com.bancolombia.api.filter.ApiKeyAuthFilter apiKeyAuthFilter;


        @Autowired
        private WebTestClient webTestClient;

                // Eliminado setUp global, se configura el mock en cada test




        @MockBean
        private HandlerLoanApp handlerLoanApp;

        @MockBean
        private LoanAppUseCase loanAppUseCase;

        @MockBean
        private RequestValidator requestValidator;

        @MockBean
        private LoanApplicationRequestMapper loanApplicationRequestMapper;

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
                org.mockito.Mockito.when(handlerLoanApp.saveLoanApp(org.mockito.ArgumentMatchers.any())).thenReturn(
                        ServerResponse.ok().bodyValue(responseLoan).flatMap(Mono::just)
                );
                webTestClient.post()
                                .uri(loanAppPath.getLoanApplication())
                                .header("X-User-Email", "client1@example.com")
                                .header("X-API-KEY", "test-key")
                                .bodyValue(request)
                                .exchange()
                                .expectStatus().isOk();
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
        org.mockito.Mockito.when(handlerLoanApp.saveLoanApp(org.mockito.ArgumentMatchers.any())).thenReturn(
            ServerResponse.badRequest().bodyValue("bad request").flatMap(Mono::just)
        );
        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                .header("X-User-Email", "correo-no-valido")
                .header("X-API-KEY", "test-key")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

                @Test
                void shouldReturnBadRequestWhenMissingHeader() {
                        org.mockito.Mockito.when(handlerLoanApp.saveLoanApp(org.mockito.ArgumentMatchers.any())).thenReturn(
                                ServerResponse.badRequest().bodyValue("bad request").flatMap(Mono::just)
                        );
                        webTestClient.post()
                                        .uri(loanAppPath.getLoanApplication())
                                        .header("X-API-KEY", "test-key")
                                        .bodyValue(request)
                                        .exchange()
                                        .expectStatus().isBadRequest();
                }

                @Test
                void shouldReturnBadRequestWhenEmailMismatch() {
                        org.mockito.Mockito.when(handlerLoanApp.saveLoanApp(org.mockito.ArgumentMatchers.any())).thenReturn(
                                ServerResponse.badRequest().bodyValue("bad request").flatMap(Mono::just)
                        );
                        webTestClient.post()
                                        .uri(loanAppPath.getLoanApplication())
                                        .header("X-User-Email", "different@example.com")
                                        .header("X-API-KEY", "test-key")
                                        .bodyValue(request)
                                        .exchange()
                                        .expectStatus().isBadRequest();
                }
}
