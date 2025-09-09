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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void testGetLoanApps() {
        // Arrange
        int offset = 0;
        int limit = 10;
        int page = 1;
        List<LoanApplication> loanApps = List.of(new LoanApplication());
        PageResponse<LoanApplication> pageResponse = new PageResponse<>(loanApps, 1, page, limit);

        when(loanAppGateway.findAll(offset, limit, page)).thenReturn(Mono.just(pageResponse));

        // Act
        Mono<PageResponse<LoanApplication>> result = useCase.getLoanApps(offset, limit, page);

        // Assert
        PageResponse<LoanApplication> response = result.block();
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(1, response.getTotalElements());
        verify(loanAppGateway).findAll(offset, limit, page);
    }

    @Test
    void testUpdateLoanApp() {
        // Arrange
        Long id = 1L;
        String state = "APPROVED";
        LoanApplication updatedLoanApp = LoanApplication.builder().id(id).state(null).build(); // Assuming state is enum or string

        when(loanAppGateway.update(id, state)).thenReturn(Mono.just(updatedLoanApp));

        // Act
        Mono<LoanApplication> result = useCase.updateLoanApp(id, state);

        // Assert
        LoanApplication loanApp = result.block();
        assertNotNull(loanApp);
        assertEquals(id, loanApp.getId());
        verify(loanAppGateway).update(id, state);
    }
}
