
package co.com.bancolombia.api;


import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import co.com.bancolombia.api.HandlerLoanApp;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import co.com.bancolombia.api.config.LoanAppPath;
import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import co.com.bancolombia.dto.LoanApplicationRequest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {RouterRest.class, HandlerLoanApp.class, GlobalExceptionFilter.class, co.com.bancolombia.api.filter.ApiKeyAuthFilter.class})
@EnableConfigurationProperties(LoanAppPath.class)
@WebFluxTest
@TestPropertySource(properties = {
        "api.auth.key=test-key",
        "routes.paths.loanApplication=/api/v1/solicitud",
        "routes.paths.loanCapacity=/api/v1/capacidad"
})
class RouterRestTest {
    @Autowired
    private WebTestClient webTestClient;

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
            .state(State.builder().id(1L).name("PENDING").description("Solicitud pendiente").build())
            .loanType(LoanType.builder().id(1L).name("PERSONAL").minimumAmount(BigDecimal.valueOf(1000)).maximumAmount(BigDecimal.valueOf(10000)).interestRate(BigDecimal.valueOf(0.05)).automaticValidation(true).build())
            .build();

        @Test
        void shouldGetLoanApps() {
        when(handlerLoanApp.getLoanApps(any())).thenReturn(Mono.just(ServerResponse.ok().bodyValue(responseLoan).block()));
                webTestClient.get()
                                .uri(loanAppPath.getLoanApplication() + "?page=0&size=1")
                                .header("X-API-KEY", "test-key")
                                .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(LoanApplication.class)
                                .value(resp -> org.assertj.core.api.Assertions.assertThat(resp.getId()).isEqualTo(responseLoan.getId()));
        }

    @Test
    void shouldUpdateLoanApp() {
        co.com.bancolombia.dto.UpdateLoanAppReq updateReq = co.com.bancolombia.dto.UpdateLoanAppReq.builder()
                .id(1L)
                .name(co.com.bancolombia.dto.LoanAppStatus.APPROVED)
                .build();
        when(handlerLoanApp.updateLoanApp(any())).thenReturn(Mono.just(ServerResponse.ok().bodyValue(responseLoan).block()));
        webTestClient.put()
                .uri(loanAppPath.getLoanApplication())
                .header("X-API-KEY", "test-key")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateReq)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanApplication.class)
                .value(resp -> org.assertj.core.api.Assertions.assertThat(resp.getId()).isEqualTo(responseLoan.getId()));
    }

        @Test
        void shouldCreateLoanApplication() {
        when(handlerLoanApp.saveLoanApp(any())).thenReturn(Mono.just(ServerResponse.ok().bodyValue(responseLoan).block()));
                webTestClient.post()
                                .uri(loanAppPath.getLoanApplication())
                                .header("X-User-Email", "client1@example.com")
                                .header("X-API-KEY", "test-key")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(request)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(LoanApplication.class)
                                .value(resp -> org.assertj.core.api.Assertions.assertThat(resp.getEmail()).isEqualTo("client1@example.com"));
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
        when(handlerLoanApp.saveLoanApp(any())).thenReturn(Mono.just(ServerResponse.badRequest().bodyValue("bad request").block()));
        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                .header("X-User-Email", "correo-no-valido")
                .header("X-API-KEY", "test-key")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(body -> org.assertj.core.api.Assertions.assertThat(body).contains("bad request"));
    }

        @Test
        void shouldReturnBadRequestWhenMissingHeader() {
        when(handlerLoanApp.saveLoanApp(any())).thenReturn(Mono.just(ServerResponse.badRequest().bodyValue("bad request").block()));
                webTestClient.post()
                                .uri(loanAppPath.getLoanApplication())
                                .header("X-API-KEY", "test-key")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(request)
                                .exchange()
                                .expectStatus().isBadRequest()
                                .expectBody(String.class)
                                .value(body -> org.assertj.core.api.Assertions.assertThat(body).contains("bad request"));
        }

        @Test
        void shouldReturnBadRequestWhenEmailMismatch() {
        when(handlerLoanApp.saveLoanApp(any())).thenReturn(Mono.just(ServerResponse.badRequest().bodyValue("bad request").block()));
                webTestClient.post()
                                .uri(loanAppPath.getLoanApplication())
                                .header("X-User-Email", "different@example.com")
                                .header("X-API-KEY", "test-key")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(request)
                                .exchange()
                                .expectStatus().isBadRequest()
                                .expectBody(String.class)
                                .value(body -> org.assertj.core.api.Assertions.assertThat(body).contains("bad request"));
        }
}
