package co.com.bancolombia.api;

import co.com.bancolombia.api.config.LoanAppPath;
import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.dto.UpdateLoanAppReq;
import co.com.bancolombia.dto.LoanAppStatus;
import co.com.bancolombia.exception.EmailMismatchException;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import co.com.bancolombia.model.PageResponse;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, HandlerLoanApp.class})
@EnableConfigurationProperties(LoanAppPath.class)
@WebFluxTest
@Import({GlobalExceptionFilter.class, RequestValidator.class, RouterRestTest.TestConfig.class})
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

    private final PageResponse<LoanApplication> pageResponse = new PageResponse<>(
            List.of(responseLoan), 1L, 0, 10);

    private final UpdateLoanAppReq updateRequest = UpdateLoanAppReq.builder()
            .id(1L)
            .name(LoanAppStatus.APPROVED)
            .build();

    // Clase de configuración para beans de test
    @Configuration
    static class TestConfig {
        @Bean
        public Validator validator() {
            return new LocalValidatorFactoryBean();
        }
        @Bean
        public LoanApplicationRequestMapper loanApplicationRequestMapper() {
            return new LoanApplicationRequestMapper();
        }
    }

    @Test
    void shouldCreateLoanApplication() {
        // Mock to return success when emails match
        when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), anyString()))
                .thenAnswer(invocation -> {
                    LoanApplication model = invocation.getArgument(0);
                    String email = invocation.getArgument(1);
                    if (email != null && email.equals(model.getEmail())) {
                        return Mono.just(responseLoan);
                    }
                    return Mono.error(new EmailMismatchException(model.getEmail(), email));
                });

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
        // Mock to return success when validation is bypassed
        when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), anyString()))
                .thenReturn(Mono.just(responseLoan));

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
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(body -> {
                    assert body.getResponseBody() != null;
                    assert !body.getResponseBody().isEmpty();
                });
    }

    @Test
    void shouldReturnBadRequestWhenMissingHeader() {
        // Mock to throw exception when email is null
        when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), isNull()))
                .thenReturn(Mono.error(new EmailMismatchException(request.getEmail(), null)));

        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                // Sin header X-User-Email
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenEmailMismatch() {
        // Mock to throw exception when emails don't match
        when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), anyString()))
                .thenAnswer(invocation -> {
                    LoanApplication model = invocation.getArgument(0);
                    String email = invocation.getArgument(1);
                    if (!email.equals(model.getEmail())) {
                        return Mono.error(new EmailMismatchException(model.getEmail(), email));
                    }
                    return Mono.just(responseLoan);
                });

        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                // Header con email diferente al del body
                .header("X-User-Email", "different@example.com")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldGetLoanAppsWithDefaultPagination() {
        when(loanAppUseCase.getLoanApps(0, 10, 0)).thenReturn(Mono.just(pageResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageResponse.class)
                .consumeWith(response -> {
                    PageResponse<?> page = response.getResponseBody();
                    assert page != null;
                    Assertions.assertThat(page.getContent()).isNotEmpty();
                    Assertions.assertThat(page.getPage()).isEqualTo(0);
                    Assertions.assertThat(page.getSize()).isEqualTo(10);
                });
    }

    @Test
    void shouldGetLoanAppsWithCustomPagination() {
        // For page=2, size=5, offset should be 2*5=10
        when(loanAppUseCase.getLoanApps(10, 5, 2)).thenReturn(Mono.just(pageResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication())
                        .queryParam("page", "2")
                        .queryParam("size", "5")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageResponse.class)
                .consumeWith(response -> {
                    PageResponse<?> page = response.getResponseBody();
                    assert page != null;
                    Assertions.assertThat(page.getContent()).isNotEmpty();
                    Assertions.assertThat(page.getPage()).isEqualTo(0);
                    Assertions.assertThat(page.getSize()).isEqualTo(10);
                });
    }

    @Test
    void shouldReturnBadRequestWhenInvalidPagination() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication())
                        .queryParam("page", "-1")
                        .queryParam("size", "0")
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Parámetros de paginación inválidos");
    }

    @Test
    void shouldUpdateLoanAppSuccessfully() {
        when(loanAppUseCase.updateLoanApp(updateRequest.getId(), updateRequest.getName().getDisplayName()))
                .thenReturn(Mono.just(responseLoan));

        webTestClient.put()
                .uri(loanAppPath.getLoanApplication())
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanApplication.class)
                .consumeWith(response -> {
                    LoanApplication loanApp = response.getResponseBody();
                    assert loanApp != null;
                    Assertions.assertThat(loanApp.getId()).isEqualTo(updateRequest.getId());
                });
    }

    @Test
    void shouldReturnBadRequestWhenUpdateLoanAppWithInvalidRequest() {
        // Mock to return success when validation is bypassed
        when(loanAppUseCase.updateLoanApp(any(), any()))
                .thenReturn(Mono.just(responseLoan));

        UpdateLoanAppReq invalidRequest = UpdateLoanAppReq.builder()
                .id(null)
                .name(null)
                .build();

        webTestClient.put()
                .uri(loanAppPath.getLoanApplication())
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(body -> {
                    assert body.getResponseBody() != null;
                    assert !body.getResponseBody().isEmpty();
                });
    }

    @Test
    void shouldReturnBadRequestWhenUpdateLoanAppFails() {
        when(loanAppUseCase.updateLoanApp(updateRequest.getId(), updateRequest.getName().getDisplayName()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri(loanAppPath.getLoanApplication())
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("No se pudo actualizar la solicitud");
    }

    @Test
    void shouldHandleBoundaryPaginationValues() {
        // Test page=0, size=1 (minimum valid values)
        when(loanAppUseCase.getLoanApps(0, 1, 0)).thenReturn(Mono.just(pageResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication())
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldHandleLargePaginationValues() {
        // Test large values to ensure no overflow issues
        when(loanAppUseCase.getLoanApps(1000, 100, 10)).thenReturn(Mono.just(pageResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication())
                        .queryParam("page", "10")
                        .queryParam("size", "100")
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldHandleEmptyResultFromUseCase() {
        // Test when use case returns empty result
        PageResponse<LoanApplication> emptyResponse = new PageResponse<>(List.of(), 0L, 0, 10);
        when(loanAppUseCase.getLoanApps(0, 10, 0)).thenReturn(Mono.just(emptyResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageResponse.class)
                .consumeWith(response -> {
                    PageResponse<?> page = response.getResponseBody();
                    assert page != null;
                    Assertions.assertThat(page.getContent()).isEmpty();
                    Assertions.assertThat(page.getTotalElements()).isEqualTo(0L);
                });
    }

    @Test
    void shouldHandleUseCaseError() {
        // Test when use case throws an error
        when(loanAppUseCase.saveLoanApp(any(LoanApplication.class), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                .header("X-User-Email", "client1@example.com")
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void shouldHandleNullQueryParams() {
        // Test with null query params (should default to 0 and 10)
        when(loanAppUseCase.getLoanApps(0, 10, 0)).thenReturn(Mono.just(pageResponse));

        webTestClient.get()
                .uri(loanAppPath.getLoanApplication())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldHandleNegativeSizeOnly() {
        // Test size=-1, page=0 (only size invalid)
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication())
                        .queryParam("page", "0")
                        .queryParam("size", "-1")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldHandleZeroSize() {
        // Test size=0, page=0 (size <= 0)
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication())
                        .queryParam("page", "0")
                        .queryParam("size", "0")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInvalidPageFormat() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication())
                        .queryParam("page", "abc")
                        .queryParam("size", "10")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInvalidSizeFormat() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(loanAppPath.getLoanApplication())
                        .queryParam("page", "0")
                        .queryParam("size", "abc")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInvalidJsonInSave() {
        webTestClient.post()
                .uri(loanAppPath.getLoanApplication())
                .header("X-User-Email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{invalid json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenInvalidJsonInUpdate() {
        webTestClient.put()
                .uri(loanAppPath.getLoanApplication())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{invalid json")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
