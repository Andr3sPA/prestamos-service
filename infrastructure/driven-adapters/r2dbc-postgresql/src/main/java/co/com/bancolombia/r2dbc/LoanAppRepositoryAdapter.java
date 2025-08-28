package co.com.bancolombia.r2dbc;


import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.gateways.LoanAppRepository;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationMapper;
import co.com.bancolombia.r2dbc.mapper.LoanTypeMapper;
import co.com.bancolombia.r2dbc.mapper.StateMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class LoanAppRepositoryAdapter implements LoanAppRepository {
    private static final Logger log = LoggerFactory.getLogger(LoanAppRepositoryAdapter.class);
    private final LoanAppReactiveRepository repoLoanApp;
    private final StateReactiveRepository repoState;
    private final LoanTypeReactiveRepository repoLoanType;
    private final LoanApplicationMapper loanApplicationMapper;
    private final LoanTypeMapper loanTypeMapper;
    private final StateMapper stateMapper;

    @Override
    public Mono<LoanApplication> register(LoanApplication loanApp) {

        if(loanApp.getState().getId()==null){
            loanApp.getState().setId(1L);
        }
        log.trace("Iniciando registro de préstamo: {}", loanApp);
        return Mono.zip(

                repoState.findById(loanApp.getState().getId()),
                repoLoanType.findById(loanApp.getLoanType().getId())
        ).flatMap(tuple -> {
            log.trace("State y LoanType obtenidos para IDs: {}, {}", loanApp.getState().getId(), loanApp.getLoanType().getId());
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
                    .doOnSuccess(saved -> log.trace("Préstamo registrado exitosamente: {}", saved))
                    .doOnError(error -> log.error("Error al registrar préstamo", error))
                    .flatMap(saved -> Mono.zip(
                            repoState.findById(saved.getState().getId()),
                            repoLoanType.findById(saved.getLoanType().getId())
                    ).map(fullTuple -> {
                        saved.setState(stateMapper.toModel(fullTuple.getT1()));
                        saved.setLoanType(loanTypeMapper.toModel(fullTuple.getT2()));
                        return saved;
                    }));
        });
    }


}

