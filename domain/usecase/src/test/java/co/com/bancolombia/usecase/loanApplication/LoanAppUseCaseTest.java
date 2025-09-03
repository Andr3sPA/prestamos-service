package co.com.bancolombia.usecase.loanApplication;

import co.com.bancolombia.model.LoanApplication;
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
}