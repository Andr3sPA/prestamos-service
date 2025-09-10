package co.com.bancolombia.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionalAdapterTest {

    private TransactionalOperator transactionalOperator;
    private TransactionalAdapter transactionalAdapter;

    @BeforeEach
    void setUp() {
        transactionalOperator = Mockito.mock(TransactionalOperator.class);
        transactionalAdapter = new TransactionalAdapter(transactionalOperator);
    }

    @Test
    void transactionalSuccess() {
        Mono<String> action = Mono.just("success");
        when(transactionalOperator.transactional(action)).thenReturn(action);

        StepVerifier.create(transactionalAdapter.transactional(action))
                .expectNext("success")
                .verifyComplete();

        verify(transactionalOperator, times(1)).transactional(action);
    }

    @Test
    void transactionalError() {
        Mono<String> action = Mono.error(new RuntimeException("error"));
        when(transactionalOperator.transactional(action)).thenReturn(action);

        StepVerifier.create(transactionalAdapter.transactional(action))
                .expectError(RuntimeException.class)
                .verify();

        verify(transactionalOperator, times(1)).transactional(action);
    }
}
