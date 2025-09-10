package co.com.bancolombia.api.filter;

import co.com.bancolombia.exception.BaseException;
import co.com.bancolombia.exception.MissingFieldException;
import co.com.bancolombia.exception.EmailMismatchException;
import co.com.bancolombia.api.response.ErrorResponse;
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
    void shouldHandleBaseException() {
        BaseException exception = new BaseException("ERR_CODE", "Title", "Message", null, 400);
        when(handler.handle(request)).thenReturn(Mono.error(exception));

        Mono<ServerResponse> result = filter.filter(request, handler);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().value() == 400)
                .verifyComplete();
    }

    @Test
    void shouldHandleMissingFieldException() {
        MissingFieldException exception = new MissingFieldException("fieldName");
        when(handler.handle(request)).thenReturn(Mono.error(exception));

        Mono<ServerResponse> result = filter.filter(request, handler);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().value() == 400)
                .verifyComplete();
    }

    @Test
    void shouldHandleEmailMismatchException() {
        EmailMismatchException exception = new EmailMismatchException("request@example.com", "session@example.com");
        when(handler.handle(request)).thenReturn(Mono.error(exception));

        Mono<ServerResponse> result = filter.filter(request, handler);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode().value() == 400)
                .verifyComplete();
    }

    @Test
    void shouldHandleGenericException() {
        RuntimeException exception = new RuntimeException("Generic error");
        when(handler.handle(request)).thenReturn(Mono.error(exception));

        Mono<ServerResponse> result = filter.filter(request, handler);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
