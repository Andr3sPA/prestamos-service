package co.com.bancolombia.api;

import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationRequestMapper;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerLoanApp {

    private final LoanAppUseCase loanAppCase;
    private final LoanApplicationRequestMapper requestMapper;

    public Mono<ServerResponse> saveLoanApp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanApplicationRequest.class)
                .map(requestMapper::toModel)
                .flatMap(loanAppCase::saveLoanApp)
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result));
    }
}
