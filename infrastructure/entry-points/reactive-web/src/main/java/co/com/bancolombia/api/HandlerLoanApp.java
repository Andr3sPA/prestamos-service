package co.com.bancolombia.api;

import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import co.com.bancolombia.api.util.RequestValidator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HandlerLoanApp {
    private final RequestValidator requestValidator;
    private final LoanAppUseCase loanAppCase;
    private final LoanApplicationRequestMapper requestMapper;
    public Mono<ServerResponse> getLoanApps(ServerRequest req) {
    int page = Integer.parseInt(req.queryParam("page").orElse("0"));
    int size = Integer.parseInt(req.queryParam("size").orElse("10"));

    int offset = page * size;
    return loanAppCase.getLoanApps(offset, size, page)
        .flatMap(response ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response)
        );
    }

    public Mono<ServerResponse> saveLoanApp(ServerRequest serverRequest) {
        String email = serverRequest.headers().firstHeader("X-User-Email");
        return serverRequest.bodyToMono(LoanApplicationRequest.class)
                .flatMap(dto->{
                    requestValidator.validate(dto,LoanApplicationRequest.class);
                    return Mono.just(dto);
                })
                .map(requestMapper::toModel)
                .flatMap(model -> loanAppCase.saveLoanApp(model, email))
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result));
    }

}
