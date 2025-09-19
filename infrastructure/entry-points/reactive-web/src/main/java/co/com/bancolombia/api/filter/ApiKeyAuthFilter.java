package co.com.bancolombia.api.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import reactor.core.publisher.Mono;

@Component
public class ApiKeyAuthFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Value("${api.auth.key}")
    private String apiKey;

    private static final String API_KEY_HEADER = "X-API-KEY";

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        String requestApiKey = request.headers().firstHeader(API_KEY_HEADER);
        if (apiKey.equals(requestApiKey)) {
            return next.handle(request);
        } else {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .bodyValue("Unauthorized: Invalid API Key");
        }
    }
}
