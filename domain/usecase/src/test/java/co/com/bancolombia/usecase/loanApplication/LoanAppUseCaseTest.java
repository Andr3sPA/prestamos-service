package co.com.bancolombia.usecase.loanApplication;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.PageResponse;
import co.com.bancolombia.model.gateways.LoanAppGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class LoanAppUseCaseTest {

    @Mock
    private LoanAppGateway loanAppGateway;

    @InjectMocks
    private LoanAppUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveLoanApp() {
        // Arrange
        LoanApplication loanApp = new LoanApplication();
        String email = "test@email.com";

        // CORRECCIÓN: Mock con ambos parámetros
        when(loanAppGateway.register(loanApp, email)).thenReturn(Mono.just(loanApp));

        // Act
        Mono<LoanApplication> result = useCase.saveLoanApp(loanApp, email);

        // Assert
        assertNotNull(result.block());
        // CORRECCIÓN: Verificar con ambos parámetros
        verify(loanAppGateway).register(loanApp, email);
    }

    // El método getLoanAppById no existe en LoanAppUseCase ni en LoanAppGateway
    // Se omite este test o se debe implementar si el método existe en el futuro

    @Test
    void testUpdateLoanApp() {
        Long id = 1L;
        String state = "APROBADO";
        LoanApplication loanApp = new LoanApplication();
        when(loanAppGateway.update(id, state)).thenReturn(Mono.just(loanApp));
        Mono<LoanApplication> result = useCase.updateLoanApp(id, state);
        assertNotNull(result.block());
        verify(loanAppGateway).update(id, state);
    }

    @Test
    void testGetLoanApps() {
        int offset = 0, limit = 10, page = 1;
        PageResponse<LoanApplication> pageResponse = new PageResponse<>();
        when(loanAppGateway.findAll(offset, limit, page)).thenReturn(Mono.just(pageResponse));
        Mono<PageResponse<LoanApplication>> result = useCase.getLoanApps(offset, limit, page);
        assertNotNull(result.block());
        verify(loanAppGateway).findAll(offset, limit, page);
    }
}