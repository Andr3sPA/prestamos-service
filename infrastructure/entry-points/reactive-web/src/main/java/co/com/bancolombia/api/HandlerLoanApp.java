package co.com.bancolombia.api;

import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.usecase.loanApplication.LoanAppUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.bancolombia.api.util.RequestValidator;
@Component
@RequiredArgsConstructor
public class HandlerLoanApp {
    private final RequestValidator requestValidator;
    private final LoanAppUseCase loanAppCase;
    private final LoanApplicationRequestMapper requestMapper;

    public Mono<ServerResponse> saveLoanApp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanApplicationRequest.class)
                .flatMap(dto->{
                    requestValidator.validate(dto,LoanApplicationRequest.class);
                    return Mono.just(dto);
                })
                .map(requestMapper::toModel)
                .flatMap(loanAppCase::saveLoanApp)
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result));
    }
}
