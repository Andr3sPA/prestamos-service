package co.com.bancolombia.usecase.loanApplication;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.gateways.LoanAppRepository;
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
    private LoanAppRepository loanAppRepository;

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
        when(loanAppRepository.register(loanApp)).thenReturn(Mono.just(loanApp));

        // Act
        Mono<LoanApplication> result = useCase.saveLoanApp(loanApp);

        // Assert
        assertNotNull(result.block());
        verify(loanAppRepository).register(loanApp);
    }
}
