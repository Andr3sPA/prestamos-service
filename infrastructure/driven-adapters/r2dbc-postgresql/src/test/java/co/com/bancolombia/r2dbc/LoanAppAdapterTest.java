package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.State;
import co.com.bancolombia.model.LoanType;
import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationMapper;
import co.com.bancolombia.r2dbc.mapper.LoanTypeMapper;
import co.com.bancolombia.r2dbc.mapper.StateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanAppAdapterTest {

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
    private LoanAppAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Arrange
        String email = "test@example.com";
        LoanApplication request = LoanApplication.builder()
                .amount(new java.math.BigDecimal("1000.0"))
                .term(12)
                .email(email) // CORRECCIÓN: mismo email
                .build();

        State stateModel = State.builder()
                .id(1L)
                .name("Approved")
                .description("Application approved")
                .build();

        LoanType loanTypeModel = LoanType.builder()
                .id(2L)
                .name("Personal Loan")
                .minimumAmount(new java.math.BigDecimal("500.0"))
                .maximumAmount(new java.math.BigDecimal("10000.0"))
                .interestRate(new java.math.BigDecimal("0.05"))
                .automaticValidation(true)
                .build();

        request.setState(stateModel);
        request.setLoanType(loanTypeModel);

        // Mocks de entidades
        co.com.bancolombia.r2dbc.entity.StateEntity stateEntity =
                mock(co.com.bancolombia.r2dbc.entity.StateEntity.class);
        co.com.bancolombia.r2dbc.entity.LoanTypeEntity loanTypeEntity =
                mock(co.com.bancolombia.r2dbc.entity.LoanTypeEntity.class);
        co.com.bancolombia.r2dbc.entity.LoanApplicationEntity entity =
                mock(co.com.bancolombia.r2dbc.entity.LoanApplicationEntity.class);

        LoanApplication savedModel = LoanApplication.builder()
                .id(99L)
                .amount(new java.math.BigDecimal("1000.0"))
                .term(12)
                .email(email)
                .state(stateModel)
                .loanType(loanTypeModel)
                .build();

        // Stubbing
        when(repoState.findById(1L)).thenReturn(Mono.just(stateEntity));
        when(repoLoanType.findById(2L)).thenReturn(Mono.just(loanTypeEntity));
        when(stateMapper.toModel(stateEntity)).thenReturn(stateModel);
        when(loanTypeMapper.toModel(loanTypeEntity)).thenReturn(loanTypeModel);
        when(loanApplicationMapper.toEntity(any(LoanApplication.class))).thenReturn(entity);
        when(repoLoanApp.save(any(co.com.bancolombia.r2dbc.entity.LoanApplicationEntity.class)))
                .thenReturn(Mono.just(entity));
        when(loanApplicationMapper.toModel(any(co.com.bancolombia.r2dbc.entity.LoanApplicationEntity.class)))
                .thenReturn(savedModel);

        // Act - CORRECCIÓN: Pasar ambos parámetros
        Mono<LoanApplication> result = adapter.register(request, email);

        // Assert
        LoanApplication response = result.block();
        assertNotNull(response);
        assertEquals(email, response.getEmail());
        assertEquals(12, response.getTerm());
        assertEquals(new java.math.BigDecimal("1000.0"), response.getAmount());
        assertNotNull(response.getState());
        assertNotNull(response.getLoanType());

        verify(repoState, times(2)).findById(1L);
        verify(repoLoanType, times(2)).findById(2L);
        verify(loanApplicationMapper).toEntity(any(LoanApplication.class));
        verify(repoLoanApp).save(any(co.com.bancolombia.r2dbc.entity.LoanApplicationEntity.class));
        verify(loanApplicationMapper).toModel(any(co.com.bancolombia.r2dbc.entity.LoanApplicationEntity.class));
    }

    @Test
    void testRegisterWithEmailMismatch() {
        // Arrange
        LoanApplication request = LoanApplication.builder()
                .amount(new java.math.BigDecimal("1000.0"))
                .term(12)
                .email("request@example.com")
                .state(State.builder().id(1L).build())
                .loanType(LoanType.builder().id(2L).build())
                .build();

        String sessionEmail = "session@example.com";

        // Act & Assert
        assertThrows(co.com.bancolombia.exception.EmailMismatchException.class,
                () -> adapter.register(request, sessionEmail).block());
    }
}