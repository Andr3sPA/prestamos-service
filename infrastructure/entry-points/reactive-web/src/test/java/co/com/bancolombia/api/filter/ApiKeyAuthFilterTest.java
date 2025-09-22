package co.com.bancolombia.api.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiKeyAuthFilterTest {
    private ApiKeyAuthFilter filter;
    private ServerRequest request;
    private HandlerFunction<ServerResponse> handler;

    @BeforeEach
    void setUp() {
        filter = new ApiKeyAuthFilter();
        request = mock(ServerRequest.class);
        handler = mock(HandlerFunction.class);
        // Mock para evitar NullPointerException en headers()
        org.springframework.web.reactive.function.server.ServerRequest.Headers headers = mock(org.springframework.web.reactive.function.server.ServerRequest.Headers.class);
        when(request.headers()).thenReturn(headers);
    }

    @Test
    void shouldAllowRequestWithValidApiKey() throws Exception {
        // Simular el valor de la API key usando reflexión
        java.lang.reflect.Field apiKeyField = ApiKeyAuthFilter.class.getDeclaredField("apiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(filter, "test-key");
        when(request.headers().firstHeader("X-API-KEY")).thenReturn("test-key");
        when(handler.handle(request)).thenReturn(Mono.just(mock(ServerResponse.class)));

        Mono<ServerResponse> result = filter.filter(request, handler);
        StepVerifier.create(result)
                .expectNextMatches(response -> response != null)
                .verifyComplete();
    }

    @Test
    void shouldRejectRequestWithInvalidApiKey() throws Exception {
        java.lang.reflect.Field apiKeyField = ApiKeyAuthFilter.class.getDeclaredField("apiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(filter, "test-key");
        when(request.headers().firstHeader("X-API-KEY")).thenReturn("wrong-key");
        when(handler.handle(request)).thenReturn(Mono.just(mock(ServerResponse.class)));

        Mono<ServerResponse> result = filter.filter(request, handler);
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().value() == 401)
                .verifyComplete();
    }
}
