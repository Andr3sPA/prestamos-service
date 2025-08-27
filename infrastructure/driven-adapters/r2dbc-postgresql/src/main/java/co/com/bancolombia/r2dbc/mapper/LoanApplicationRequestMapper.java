package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.dto.LoanApplicationRequest;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.State;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationRequestMapper {

    public LoanApplication toModel(LoanApplicationRequest dto) {
        LoanApplication model = new LoanApplication();
        model.setAmount(dto.getAmount());
        model.setTerm(dto.getTerm());
        model.setEmail(dto.getEmail());

        State state = new State();
        state.setId(dto.getStateId());
        model.setState(state);

        LoanType loanType = new LoanType();
        loanType.setId(dto.getLoanTypeId());
        model.setLoanType(loanType);

        return model;
    }
}
