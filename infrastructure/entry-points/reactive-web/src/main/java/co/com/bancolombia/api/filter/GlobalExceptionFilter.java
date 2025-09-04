package co.com.bancolombia.api.filter;

import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import co.com.bancolombia.api.response.ErrorResponse;
import co.com.bancolombia.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalExceptionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    @NonNull
    public Mono<ServerResponse> filter(@NonNull ServerRequest request,
                                       @NonNull HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(BaseException.class, ex -> {
                    log.trace(
                            "Exception: {} - {} - errors: {}",
                            ex.getErrorCode(),
                            ex.getMessage(),
                            ex.getErrors());
                    return ServerResponse.status(ex.getStatus()).bodyValue(
                            ErrorResponse.builder()
                                    .errorCode(ex.getErrorCode())
                                    .tittle(ex.getTitle())
                                    .message(ex.getMessage())
                                    .errors(ex.getErrors())
                                    .timestamp(ex.getTimestamp())
                                    .build()
                    );
                });
    }
}