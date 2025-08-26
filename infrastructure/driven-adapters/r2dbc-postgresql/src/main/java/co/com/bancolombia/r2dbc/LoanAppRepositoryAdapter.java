package co.com.bancolombia.r2dbc;


import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.dto.LoanApplicationRequest;
import co.com.bancolombia.model.gateways.LoanAppRepository;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationMapper;
import co.com.bancolombia.r2dbc.mapper.LoanTypeMapper;
import co.com.bancolombia.r2dbc.mapper.StateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class LoanAppRepositoryAdapter implements LoanAppRepository {
    private final LoanAppReactiveRepository repoLoanApp;
    private final StateReactiveRepository repoState;
    private final LoanTypeReactiveRepository repoLoanType;
    private final LoanApplicationMapper loanApplicationMapper;
    private final LoanTypeMapper loanTypeMapper;
    private final StateMapper stateMapper;

    @Override
    public Mono<LoanApplication> register(LoanApplicationRequest loanApp) {
        return Mono.zip(
                repoState.findById(loanApp.getStateId()),
                repoLoanType.findById(loanApp.getLoanTypeId())
        ).flatMap(tuple -> {
            var state = stateMapper.toModel(tuple.getT1());
            var loanType = loanTypeMapper.toModel(tuple.getT2());

            LoanApplication model = new LoanApplication();
            model.setAmount(loanApp.getAmount());
            model.setTerm(loanApp.getTerm());
            model.setEmail(loanApp.getEmail());
            model.setState(state);
            model.setLoanType(loanType);

            return repoLoanApp.save(loanApplicationMapper.toEntity(model))
                    .map(loanApplicationMapper::toModel)
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

