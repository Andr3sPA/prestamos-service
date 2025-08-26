package co.com.bancolombia.api;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.dto.LoanApplicationRequest;
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
    public Mono<ServerResponse> saveLoanApp(ServerRequest serverRequest){
        Mono<LoanApplicationRequest> incomingObject=serverRequest.bodyToMono(LoanApplicationRequest.class);
        return incomingObject.flatMap(object->ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loanAppCase.saveLoanApp(object),Object.class));
    }
}
