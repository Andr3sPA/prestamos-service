package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.LoanApplication;
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
        // Crear un LoanApplication con datos simulados
    LoanApplication request = new LoanApplication();
    request.setAmount(new java.math.BigDecimal("1000.0"));
    request.setTerm(12);
    request.setEmail("test@example.com");
    // Simular State y LoanType si es necesario
    // request.setState(...);
    // request.setLoanType(...);


    // Usar Long para los IDs
    co.com.bancolombia.r2dbc.entity.StateEntity stateEntity = mock(co.com.bancolombia.r2dbc.entity.StateEntity.class);
    co.com.bancolombia.r2dbc.entity.LoanTypeEntity loanTypeEntity = mock(co.com.bancolombia.r2dbc.entity.LoanTypeEntity.class);
    co.com.bancolombia.model.State stateModel = mock(co.com.bancolombia.model.State.class);
    co.com.bancolombia.model.LoanType loanTypeModel = mock(co.com.bancolombia.model.LoanType.class);
    co.com.bancolombia.r2dbc.entity.LoanApplicationEntity entity = mock(co.com.bancolombia.r2dbc.entity.LoanApplicationEntity.class);
    LoanApplication savedModel = new LoanApplication();

    when(repoState.findById(any(Long.class))).thenReturn(Mono.just(stateEntity));
    when(repoLoanType.findById(any(Long.class))).thenReturn(Mono.just(loanTypeEntity));
    when(stateMapper.toModel(stateEntity)).thenReturn(stateModel);
    when(loanTypeMapper.toModel(loanTypeEntity)).thenReturn(loanTypeModel);
    when(loanApplicationMapper.toEntity(any(LoanApplication.class))).thenReturn(entity);
    when(repoLoanApp.save(entity)).thenReturn(Mono.just(entity));
    when(loanApplicationMapper.toModel(entity)).thenReturn(savedModel);

    Mono<LoanApplication> result = adapter.register(request);
    LoanApplication loanAppResult = result.block();
    assertNotNull(loanAppResult);
    verify(repoState, atLeastOnce()).findById(any(Long.class));
    verify(repoLoanType, atLeastOnce()).findById(any(Long.class));
    verify(loanApplicationMapper).toEntity(any(LoanApplication.class));
    verify(repoLoanApp).save(entity);
    verify(loanApplicationMapper).toModel(entity);
    }
}
