package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.dto.LoanApplicationRequest;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationMapper;
import co.com.bancolombia.r2dbc.mapper.LoanTypeMapper;
import co.com.bancolombia.r2dbc.mapper.StateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanAppRepositoryAdapterTest {
    @Mock
    private LoanAppReactiveRepository repoLoanApp;
    @Mock
    private StateReactiveRepository repoState;
    @Mock
    private LoanTypeReactiveRepository repoLoanType;
    @Mock
    private LoanApplicationMapper loanApplicationMapper;
    @Mock
    private LoanTypeMapper loanTypeMapper;
    @Mock
    private StateMapper stateMapper;
    @InjectMocks
    private LoanAppRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setStateId("stateId");
        request.setLoanTypeId("loanTypeId");
        request.setAmount(1000.0);
        request.setTerm(12);
        request.setEmail("test@example.com");

        var stateEntity = mock(Object.class);
        var loanTypeEntity = mock(Object.class);
        var stateModel = mock(Object.class);
        var loanTypeModel = mock(Object.class);
        LoanApplication model = new LoanApplication();
        LoanApplication entity = new LoanApplication();
        LoanApplication savedModel = new LoanApplication();

        when(repoState.findById("stateId")).thenReturn(Mono.just(stateEntity));
        when(repoLoanType.findById("loanTypeId")).thenReturn(Mono.just(loanTypeEntity));
        when(stateMapper.toModel(stateEntity)).thenReturn(stateModel);
        when(loanTypeMapper.toModel(loanTypeEntity)).thenReturn(loanTypeModel);
        when(loanApplicationMapper.toEntity(any())).thenReturn(entity);
        when(repoLoanApp.save(entity)).thenReturn(Mono.just(entity));
        when(loanApplicationMapper.toModel(entity)).thenReturn(savedModel);
        when(repoState.findById(any())).thenReturn(Mono.just(stateEntity));
        when(repoLoanType.findById(any())).thenReturn(Mono.just(loanTypeEntity));
        when(stateMapper.toModel(stateEntity)).thenReturn(stateModel);
        when(loanTypeMapper.toModel(loanTypeEntity)).thenReturn(loanTypeModel);

        Mono<LoanApplication> result = adapter.register(request);
        LoanApplication loanAppResult = result.block();
        assertNotNull(loanAppResult);
        verify(repoState, atLeastOnce()).findById("stateId");
        verify(repoLoanType, atLeastOnce()).findById("loanTypeId");
        verify(loanApplicationMapper).toEntity(any());
        verify(repoLoanApp).save(entity);
        verify(loanApplicationMapper).toModel(entity);
    }
}
