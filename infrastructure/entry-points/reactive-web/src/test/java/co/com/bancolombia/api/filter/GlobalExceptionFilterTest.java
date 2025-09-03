package co.com.bancolombia.api.filter;

import co.com.bancolombia.exception.EmailMismatchException;
import co.com.bancolombia.exception.MissingFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionFilterTest {

    private GlobalExceptionFilter filter;
    private ServerRequest request;
    private HandlerFunction<ServerResponse> handler;

    @BeforeEach
    void setUp() {
        filter = new GlobalExceptionFilter();
        request = mock(ServerRequest.class);
        handler = mock(HandlerFunction.class);
    }

    @Test
    void shouldHandleEmailMismatchException() {
        // Arrange
        EmailMismatchException exception = new EmailMismatchException("req@test.com", "session@test.com");
        when(handler.handle(request)).thenReturn(Mono.error(exception));

        // Act
        Mono<ServerResponse> result = filter.filter(request, handler);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().value() == 400)
                .verifyComplete();
    }

    @Test
    void shouldHandleMissingFieldException() {
        // Arrange
        MissingFieldException exception = new MissingFieldException("amount");
        when(handler.handle(request)).thenReturn(Mono.error(exception));

        // Act
        Mono<ServerResponse> result = filter.filter(request, handler);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().value() == 400)
                .verifyComplete();
    }
}
