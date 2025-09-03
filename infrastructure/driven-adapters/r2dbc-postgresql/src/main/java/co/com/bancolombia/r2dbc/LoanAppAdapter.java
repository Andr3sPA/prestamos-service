package co.com.bancolombia.r2dbc;


import co.com.bancolombia.exception.EmailMismatchException;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.gateways.LoanAppGateway;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationMapper;
import co.com.bancolombia.r2dbc.mapper.LoanTypeMapper;
import co.com.bancolombia.r2dbc.mapper.StateMapper;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<LoanApplication> findAll() {
        return repoLoanApp.findAll()
                .map(loanApplicationMapper::toModel)
                .flatMap(this::enrichLoanApplication);
    }
    @Override
    public Mono<LoanApplication> register(LoanApplication loanApp,String email) {
        if (loanApp.getState().getId() == null) {
            loanApp.getState().setId(1L);
        }
        if (email!= loanApp.getEmail())
            new EmailMismatchException(loanApp.getEmail(),email);
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

