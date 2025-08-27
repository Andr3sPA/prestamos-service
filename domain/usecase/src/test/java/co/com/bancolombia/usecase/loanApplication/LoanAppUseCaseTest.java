package co.com.bancolombia.usecase.loanApplication;

import co.com.bancolombia.model.LoanApplication;
import co.com.bancolombia.model.gateways.LoanAppRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;
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
        LoanApplication loanApp = new LoanApplication();
        when(loanAppRepository.register(request)).thenReturn(Mono.just(loanApp));
        Mono<LoanApplication> result = useCase.saveLoanApp(request);
        assertNotNull(result.block());
        verify(loanAppRepository).register(request);
    }
}
