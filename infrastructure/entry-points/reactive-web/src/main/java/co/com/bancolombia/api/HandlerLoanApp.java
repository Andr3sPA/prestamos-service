package co.com.bancolombia.api;

import co.com.bancolombia.dto.CalculateCapacityReq;
import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.dto.UpdateLoanAppReq;
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
    public Mono<ServerResponse> updateLoanApp(ServerRequest req){
        return req.bodyToMono(UpdateLoanAppReq.class)
                .flatMap(dto->{
                    requestValidator.validate(dto,UpdateLoanAppReq.class);
                    return Mono.just(dto);
                })
                .flatMap(body->loanAppCase.updateLoanApp(body.getId(), String.valueOf(body.getName())))
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result))
                .switchIfEmpty(ServerResponse.badRequest().bodyValue("No se pudo actualizar la solicitud"));
    }
    public Mono<ServerResponse> calcularCapacidad(ServerRequest serverRequest) {
        Long userId = Long.valueOf(serverRequest.headers().firstHeader("X-User-Id"));
        Double salary = Double.valueOf(serverRequest.headers().firstHeader("X-User-Salary"));

        return serverRequest.bodyToMono(CalculateCapacityReq.class)
                .flatMap(dto->{
                    requestValidator.validate(dto, CalculateCapacityReq.class);
                    return Mono.just(dto);
                })
                .flatMap(model -> loanAppCase.calcularCapacidadEndeudamiento(model.getLoanId(), new co.com.bancolombia.model.request.CalcularCapacidadRequest.UserDTO(userId,salary)))
                .flatMap(result -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result));
    }
}
