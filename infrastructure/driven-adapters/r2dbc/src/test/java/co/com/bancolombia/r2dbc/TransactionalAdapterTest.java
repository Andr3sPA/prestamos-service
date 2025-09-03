package co.com.bancolombia.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionalAdapterTest {

    @Mock
    private TransactionalOperator transactionalOperator;

    private TransactionalAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new TransactionalAdapter(transactionalOperator);
    }

    @Test
    void shouldExecuteTransactional() {
        // Arrange
        String expectedResult = "success";
        Mono<String> action = Mono.just(expectedResult);
        when(transactionalOperator.transactional(any(Mono.class))).thenReturn(action);

        // Act
        Mono<String> result = adapter.transactional(action);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedResult)
                .verifyComplete();
    }
}
