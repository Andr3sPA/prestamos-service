package co.com.bancolombia.api.config;

import co.com.bancolombia.api.RouterRest;
import co.com.bancolombia.api.HandlerLoanApp;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collections;
import co.com.bancolombia.model.PageResponse;
import co.com.bancolombia.model.LoanApplication;

import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class, HandlerLoanApp.class, LoanAppPath.class, co.com.bancolombia.api.filter.GlobalExceptionFilter.class})
@Import({CorsConfig.class, SecurityHeadersConfig.class})
@TestPropertySource(properties = {
        "routes.paths.loanApplication=/api/v1/solicitud",
        "cors.allowed-origins=*"
})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

        @org.springframework.boot.test.mock.mockito.MockBean
        private LoanAppUseCase loanAppCase;

        @org.springframework.boot.test.mock.mockito.MockBean
        private co.com.bancolombia.api.util.RequestValidator requestValidator;

    @org.springframework.boot.test.mock.mockito.MockBean
    private co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper loanApplicationRequestMapper;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        // Mock para cualquier parámetro usando Mockito
        org.mockito.Mockito.when(loanAppCase.getLoanApps(org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(new PageResponse<>(Collections.emptyList(), 0, 0, 0)));

        webTestClient.get()
                .uri("/api/v1/solicitud")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

    @Test
    void securityHeadersShouldBePresent() {
        org.mockito.Mockito.when(loanAppCase.getLoanApps(org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(new PageResponse<>(Collections.emptyList(), 0, 0, 0)));

        webTestClient.get()
                .uri("/api/v1/solicitud")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists("Content-Security-Policy")
                .expectHeader().exists("Strict-Transport-Security")
                .expectHeader().exists("X-Content-Type-Options")
                .expectHeader().exists("Cache-Control")
                .expectHeader().exists("Pragma")
                .expectHeader().exists("Referrer-Policy");
    }
}