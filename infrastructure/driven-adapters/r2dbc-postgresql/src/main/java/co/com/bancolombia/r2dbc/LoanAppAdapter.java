package co.com.bancolombia.r2dbc;
import co.com.bancolombia.dto.LoanUpdateMessage;
import co.com.bancolombia.exception.LoanApplicationNotFoundException;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import co.com.bancolombia.model.request.CalcularCapacidadRequest;
import co.com.bancolombia.model.response.CalcularCapacidadResponse;
import co.com.bancolombia.r2dbc.aws.CapacidadEndeudService;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import co.com.bancolombia.r2dbc.entity.StateEntity;
import com.google.gson.JsonObject;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import co.com.bancolombia.model.PageResponse;
import co.com.bancolombia.exception.EmailMismatchException;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.gateways.LoanAppGateway;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationMapper;
import co.com.bancolombia.r2dbc.mapper.LoanTypeMapper;
import co.com.bancolombia.r2dbc.mapper.StateMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.reactivecommons.utils.ObjectMapper;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.google.gson.Gson;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LoanAppAdapter implements LoanAppGateway {
    private static final Logger log = LoggerFactory.getLogger(LoanAppAdapter.class);
    private final LoanAppReactiveRepository repoLoanApp;
    private final StateReactiveRepository repoState;
    private final LoanTypeReactiveRepository repoLoanType;
    private final LoanApplicationMapper loanApplicationMapper;
    private final LoanTypeMapper loanTypeMapper;
    private final StateMapper stateMapper;
    private final ObjectMapper mapper;
    private final SqsTemplate sqsTemplate;
    private final R2dbcEntityTemplate template;
    private Mono<LoanApplication> enrichLoanApplication(LoanApplication loanApp) {
        return Mono.zip(
                repoState.findById(loanApp.getState().getId()),
                repoLoanType.findById(loanApp.getLoanType().getId())
        ).map(tuple -> {
            loanApp.setState(stateMapper.toModel(tuple.getT1()));
            loanApp.setLoanType(loanTypeMapper.toModel(tuple.getT2()));
            return loanApp;
        });
    }
    @Override
    public Mono<PageResponse<LoanApplication>> findAll(int offset, int limit, int page) {

        Query query = Query.empty().limit(limit).offset(offset);

        Flux<LoanApplication> contentFlux = template.select(query, LoanApplicationEntity.class)
                .map(loanApplicationMapper::toModel)
                .flatMap(this::enrichLoanApplication);

        Mono<Long> totalCount = template.count(Query.empty(), LoanApplicationEntity.class);

        return contentFlux.collectList()
        .zipWith(totalCount)
        .map(t -> new PageResponse<>(
            t.getT1(),
            t.getT2(),
            page,
            limit
                ));
    }

    @Override
    public Mono<LoanApplication> update(Long id, String state) {
        log.trace("Iniciando actualización de préstamo con ID {}: nuevo estado '{}'", id, state);
        return repoLoanApp.findById(id)
                .switchIfEmpty(Mono.error(new LoanApplicationNotFoundException(id)))
                .flatMap(existing ->
                        repoState.findByName(state)
                                .map(StateEntity::getId)
                                .flatMap(stateId -> {
                                    existing.setStateId(stateId);
                                    return repoLoanApp.save(existing);
                                })
                )
                .map(loanApplicationMapper::toModel)
                .flatMap(this::enrichLoanApplication)
                .switchIfEmpty(Mono.error(new RuntimeException("No se pudo actualizar el préstamo")))
                .doOnSuccess(updated -> {
                    log.trace("Préstamo actualizado exitosamente: {}", updated);
                    LoanUpdateMessage msg = new LoanUpdateMessage(
                            updated.getId(),
                            updated.getState().getName(),
                            updated.getEmail(),
                            updated.getAmount() != null ? updated.getAmount().doubleValue() : 0.0
                    );
                    String message = new Gson().toJson(msg);
                    sqsTemplate.send("update_request", message);
                    if ("approved".equalsIgnoreCase(updated.getState().getName())) {
                        sqsTemplate.send("accepted_loans", message);
                    }
                })
                .doOnError(error -> log.error("Error al actualizar prestamo", error));
    }


    @Override
    public Mono<CalcularCapacidadResponse> calcularEndeudamiento(Long id, CalcularCapacidadRequest.UserDTO userDTO) {
        return repoLoanApp.findById(id)
                .switchIfEmpty(Mono.error(new LoanApplicationNotFoundException(id)))
                .map(loanApplicationMapper::toModel)
                .flatMap(loanApp -> {
                    if (loanApp.getState().getId() == null) {
                        loanApp.getState().setId(1L);
                    }
                    log.trace("Iniciando cálculo de capacidad de endeudamiento para el préstamo: {}", loanApp);

                    CapacidadEndeudService service = LambdaInvokerFactory.builder()
                            .lambdaClient(AWSLambdaClientBuilder.defaultClient())
                            .build(CapacidadEndeudService.class);

                    Mono<LoanType> loanTypeMono = repoLoanType.findById(loanApp.getLoanType().getId())
                            .map(loanTypeMapper::toModel);
                    Mono<State> stateMono = repoState.findById(loanApp.getState().getId())
                            .map(stateMapper::toModel);

                    Flux<CalcularCapacidadRequest.ExistingLoanDTO> existingLoansFlux = repoLoanApp.findByEmail(loanApp.getEmail())
                            .filter(existing -> !existing.getId().equals(loanApp.getId()))
                            .flatMap(entity -> repoLoanType.findById(entity.getLoanTypeId())
                                    .map(loanTypeEntity -> new CalcularCapacidadRequest.ExistingLoanDTO(
                                            entity.getAmount() != null ? entity.getAmount().doubleValue() : 0.0,
                                            entity.getTerm(),
                                            loanTypeEntity.getInterestRate() != null ? loanTypeEntity.getInterestRate().doubleValue() : 0.0
                                    ))
                            );

                    Mono<List<CalcularCapacidadRequest.ExistingLoanDTO>> existingLoansListMono = existingLoansFlux.collectList();

                    return Mono.zip(loanTypeMono, stateMono, existingLoansListMono)
                            .map(tuple -> {
                                LoanType loanType = tuple.getT1();
                                State state = tuple.getT2();
                                List<CalcularCapacidadRequest.ExistingLoanDTO> existingLoansList = tuple.getT3();

                                CalcularCapacidadRequest request = CalcularCapacidadRequest.builder()
                                        .user(new CalcularCapacidadRequest.UserDTO(userDTO.getUserId(), userDTO.getBaseSalary()))
                                        .application(new CalcularCapacidadRequest.ApplicationDTO(
                                                loanApp.getId(),
                                                loanApp.getAmount(),
                                                loanApp.getTerm() != null ? loanApp.getTerm() : 12,
                                                loanApp.getEmail(),
                                                state.getId(),
                                                new CalcularCapacidadRequest.LoanTypeDTO(
                                                        loanApp.getLoanType().getId(),
                                                        loanType.getName(),
                                                        loanType.getMinimumAmount(),
                                                        loanType.getMaximumAmount(),
                                                        loanType.getInterestRate(),
                                                        true
                                                )
                                        ))
                                        .existingLoans(existingLoansList)
                                        .build();
                                log.trace("Request que se enviará a la Lambda: {}", request);
                                return request;
                            })
                            .flatMap(calcularCapacidadRequest ->
                                    Mono.fromCallable(() -> service.calcularCapacidadEndeudamiento(calcularCapacidadRequest))
                            )
                            .map(lambdaResponse -> {
                                CalcularCapacidadResponse response = mapper.map(lambdaResponse, CalcularCapacidadResponse.class);
                                if (response.getCuotaNueva() == null && lambdaResponse instanceof java.util.Map) {
                                    Object cuotaNueva = ((java.util.Map<?, ?>) lambdaResponse).get("cuotaNueva");
                                    if (cuotaNueva instanceof Number) {
                                        response.setCuotaNueva(((Number) cuotaNueva).doubleValue());
                                    }
                                }
                                return response;
                            })
                            .flatMap(result -> {
                                log.trace("Cálculo de capacidad de endeudamiento exitoso: {}", result);
                                if ("APROBADO".equalsIgnoreCase(result.getDecision())) {
                                    return update(id, "APPROVED")
                                            .map(updated -> {
                                                log.trace("Estado actualizado a Approved para préstamo {} dentro de calcularEndeudamiento", updated.getId());
                                                return result;
                                            });
                                } else if ("REJECTED".equalsIgnoreCase(result.getDecision())) {
                                    return update(id, "Rejected")
                                            .map(updated -> {
                                                log.trace("Estado actualizado a Rejected para préstamo {} dentro de calcularEndeudamiento", updated.getId());
                                                return result;
                                            });
                                } else {
                                    return Mono.just(result);
                                }
                            })
                            .doOnError(error -> log.error("Error al calcular capacidad de endeudamiento", error));
                });
    }


    @Override
    public Mono<LoanApplication> register(LoanApplication loanApp,String email) {
        if (loanApp.getState().getId() == null) {
            loanApp.getState().setId(1L);
        }
        if (!email.equals(loanApp.getEmail())) {
            throw new EmailMismatchException(loanApp.getEmail(), email);
        }
        log.trace("Iniciando registro de préstamo: {}", loanApp);

        return Mono.zip(
                repoState.findById(loanApp.getState().getId()),
                repoLoanType.findById(loanApp.getLoanType().getId())
        ).flatMap(tuple -> {
            var state = stateMapper.toModel(tuple.getT1());
            var loanType = loanTypeMapper.toModel(tuple.getT2());

            LoanApplication model = new LoanApplication();
            model.setAmount(loanApp.getAmount());
            model.setTerm(loanApp.getTerm());
            model.setEmail(loanApp.getEmail());
            model.setState(state);
            model.setLoanType(loanType);

            log.trace("Guardando entidad LoanApplication en base de datos: {}", model);
            return repoLoanApp.save(loanApplicationMapper.toEntity(model))
                    .map(loanApplicationMapper::toModel)
                    .flatMap(this::enrichLoanApplication)
                    .doOnSuccess(saved -> log.trace("Préstamo registrado exitosamente: {}", saved))
                    .doOnError(error -> log.error("Error al registrar préstamo", error));
        });
    }

}
