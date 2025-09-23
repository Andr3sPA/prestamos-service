package co.com.bancolombia.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.bancolombia.api.config.LoanAppPath;
import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.api.filter.ApiKeyAuthFilter;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import co.com.bancolombia.dto.UpdateLoanAppReq;
import co.com.bancolombia.dto.LoanApplicationRequest;

import java.math.BigDecimal;

@SpringBootTest
@Import({RouterRest.class, LoanAppPath.class, GlobalExceptionFilter.class, ApiKeyAuthFilter.class})
class RouterRestTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private LoanAppPath loanAppPath;

    @MockBean
    private HandlerLoanApp handlerLoanApp;
    @MockBean
    private LoanAppUseCase loanAppUseCase;
    @MockBean
    private LoanApplicationRequestMapper loanApplicationRequestMapper;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private ApiKeyAuthFilter apiKeyAuthFilter;

    @Test
    void shouldGetLoanApps() {
        Mockito.when(handlerLoanApp.getLoanApps(Mockito.any())).thenReturn(
                ServerResponse.ok().bodyValue("ok")
        );
        webTestClient.get()
                .uri("/api/v1/solicitud?page=0&size=1")
                .header("X-API-KEY", "test-key")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldUpdateLoanApp() {
        UpdateLoanAppReq updateReq = UpdateLoanAppReq.builder()
                .id(1L)
                .name(co.com.bancolombia.dto.LoanAppStatus.APPROVED)
                .build();
        Mockito.when(handlerLoanApp.updateLoanApp(Mockito.any())).thenReturn(
                ServerResponse.ok().bodyValue("ok")
        );
        webTestClient.put()
                .uri("/api/v1/solicitud")
                .header("X-API-KEY", "test-key")
                .bodyValue(updateReq)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnBadRequestWhenEmailMismatch() {
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .email("user@example.com")
                .amount(BigDecimal.valueOf(10000))
                .termMonths(12)
                .build();
        Mockito.when(handlerLoanApp.saveLoanApp(Mockito.any())).thenReturn(
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
