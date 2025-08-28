package co.com.bancolombia.r2dbc;
import co.com.bancolombia.model.State;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.dto.LoanApplicationRequest;
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
        // Arrange
        LoanApplication request = new LoanApplication();
        request.setAmount(new java.math.BigDecimal("1000.0"));
        request.setTerm(12);
        request.setEmail("test@example.com");

        // crear los modelos de dominio
        State stateModel = new State();
        stateModel.setId(1L);
        stateModel.setName("Approved");
        stateModel.setDescription("Application approved");

        LoanType loanTypeModel = new LoanType();
        loanTypeModel.setId(2L);
        loanTypeModel.setName("Personal Loan");
        loanTypeModel.setMinimumAmount(new java.math.BigDecimal("500.0"));
        loanTypeModel.setMaximumAmount(new java.math.BigDecimal("10000.0"));
        loanTypeModel.setInterestRate(new java.math.BigDecimal("0.05"));
        loanTypeModel.setAutomaticValidation(true);

        // setear los objetos en la solicitud
        request.setState(stateModel);
        request.setLoanType(loanTypeModel);

        // mocks de entidades
        co.com.bancolombia.r2dbc.entity.StateEntity stateEntity = mock(co.com.bancolombia.r2dbc.entity.StateEntity.class);
        co.com.bancolombia.r2dbc.entity.LoanTypeEntity loanTypeEntity = mock(co.com.bancolombia.r2dbc.entity.LoanTypeEntity.class);
        co.com.bancolombia.r2dbc.entity.LoanApplicationEntity entity = mock(co.com.bancolombia.r2dbc.entity.LoanApplicationEntity.class);

        LoanApplication savedModel = new LoanApplication();
        savedModel.setId(99L);
        savedModel.setAmount(new java.math.BigDecimal("1000.0"));
        savedModel.setTerm(12);
        savedModel.setEmail("test@example.com");
        savedModel.setState(stateModel);
        savedModel.setLoanType(loanTypeModel);

        // Stubbing
        when(repoState.findById(1L)).thenReturn(Mono.just(stateEntity));
        when(repoLoanType.findById(2L)).thenReturn(Mono.just(loanTypeEntity));
        when(stateMapper.toModel(stateEntity)).thenReturn(stateModel);
        when(loanTypeMapper.toModel(loanTypeEntity)).thenReturn(loanTypeModel);
        when(loanApplicationMapper.toEntity(any(LoanApplication.class))).thenReturn(entity);
        when(repoLoanApp.save(entity)).thenReturn(Mono.just(entity));
        when(loanApplicationMapper.toModel(entity)).thenReturn(savedModel);

        // Act
        Mono<LoanApplication> result = adapter.register(request);

        // Assert
        LoanApplication response = result.block();
        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals(12, response.getTerm());
        assertEquals(new java.math.BigDecimal("1000.0"), response.getAmount());
        assertNotNull(response.getState());
        assertNotNull(response.getLoanType());

        verify(repoState).findById(1L);
        verify(repoLoanType).findById(2L);
        verify(loanApplicationMapper).toEntity(any(LoanApplication.class));
        verify(repoLoanApp).save(entity);
        verify(loanApplicationMapper).toModel(entity);
    }

}
