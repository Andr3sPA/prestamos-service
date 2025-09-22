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
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;


@ContextConfiguration(classes = {RouterRest.class, HandlerLoanApp.class, co.com.bancolombia.api.filter.GlobalExceptionFilter.class})
@org.springframework.test.context.TestPropertySource(properties = {"api.auth.key=test-key"})
@EnableConfigurationProperties(LoanAppPath.class)
@WebFluxTest
@Import({GlobalExceptionFilter.class, co.com.bancolombia.api.filter.ApiKeyAuthFilter.class})
class RouterRestTest {

        @org.springframework.boot.test.mock.mockito.MockBean
        private co.com.bancolombia.api.filter.ApiKeyAuthFilter apiKeyAuthFilter;

    @Autowired
    private WebTestClient webTestClient;

                // Eliminado setUp global, se configura el mock en cada test

        @MockBean
        private LoanAppUseCase loanAppUseCase;

        @MockBean
        private RequestValidator requestValidator;

        @MockBean
        private co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper loanApplicationRequestMapper;

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
        when(loanApplicationRequestMapper.toModel(any(LoanApplicationRequest.class))).thenReturn(responseLoan);
        when(loanApplicationRequestMapper.toModel(any(LoanApplicationRequest.class))).thenReturn(responseLoan);
        org.mockito.Mockito.doNothing().when(requestValidator).validate(any(), any());
        when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), anyString())).thenReturn(Mono.just(responseLoan));

        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                .header("X-User-Email", "client1@example.com")
                .header("X-API-KEY", "test-key")
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
        when(loanApplicationRequestMapper.toModel(any(LoanApplicationRequest.class))).thenReturn(responseLoan);
        when(loanApplicationRequestMapper.toModel(any(LoanApplicationRequest.class))).thenReturn(responseLoan);
        LoanApplicationRequest invalidRequest = LoanApplicationRequest.builder()
                .amount(BigDecimal.valueOf(-1000))
                .term(0)
                .email("correo-no-valido")
                .stateId(null)
                .loanTypeId(null)
                .build();
        org.mockito.Mockito.doThrow(new co.com.bancolombia.exception.MissingFieldException("amount")).when(requestValidator).validate(any(), any());

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
        when(loanApplicationRequestMapper.toModel(any(LoanApplicationRequest.class))).thenReturn(responseLoan);
                when(loanApplicationRequestMapper.toModel(any(LoanApplicationRequest.class))).thenReturn(responseLoan);
                org.mockito.Mockito.doNothing().when(requestValidator).validate(any(), any());
                when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), isNull())).thenReturn(Mono.error(new co.com.bancolombia.exception.EmailMismatchException("", "")));

                webTestClient.post()
                                .uri(loanAppPath.getLoanApplication())
                                .header("X-API-KEY", "test-key")
                                .bodyValue(request)
                                .exchange()
                                .expectStatus().isBadRequest();
    }

    @Test
        void shouldReturnBadRequestWhenEmailMismatch() {
        when(loanApplicationRequestMapper.toModel(any(LoanApplicationRequest.class))).thenReturn(responseLoan);
                when(loanApplicationRequestMapper.toModel(any(LoanApplicationRequest.class))).thenReturn(responseLoan);
                org.mockito.Mockito.doNothing().when(requestValidator).validate(any(), any());
                when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), anyString())).thenReturn(Mono.error(new co.com.bancolombia.exception.EmailMismatchException("client1@example.com", "different@example.com")));

                webTestClient.post()
                                .uri(loanAppPath.getLoanApplication())
                                .header("X-User-Email", "different@example.com")
                                .header("X-API-KEY", "test-key")
                                .bodyValue(request)
                                .exchange()
                                .expectStatus().isBadRequest();
    }
}
