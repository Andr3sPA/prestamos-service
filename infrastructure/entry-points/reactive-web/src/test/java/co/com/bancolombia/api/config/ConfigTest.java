package co.com.bancolombia.api.config;

import co.com.bancolombia.api.RouterRest;
import co.com.bancolombia.api.HandlerLoanApp;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import co.com.bancolombia.api.filter.GlobalExceptionFilter;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, HandlerLoanApp.class, LoanAppPath.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class, GlobalExceptionFilter.class, RequestValidator.class, ConfigTest.TestConfig.class})
@TestPropertySource(properties = {
        "routes.paths.loanApplication=/api/v1/solicitud",
        "cors.allowed-origins=*"
})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LoanAppUseCase loanAppUseCase;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        // Mock para el endpoint GET (obtener loans)
        when(loanAppUseCase.getLoanApps(anyInt(), anyInt(), anyInt())).thenReturn(Mono.empty());

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
        when(loanAppUseCase.getLoanApps(anyInt(), anyInt(), anyInt())).thenReturn(Mono.empty());

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
}