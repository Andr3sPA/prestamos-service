package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.gateways.TransactionalGateway;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Component
public class TransactionalAdapter implements TransactionalGateway {
    private static final Logger log = LoggerFactory.getLogger(TransactionalAdapter.class);
    private final TransactionalOperator transactionalOperator;


    @Override
    public <T> Mono<T> transactional(Mono<T> action) {
        log.trace("Iniciando transacción reactiva");
        return transactionalOperator.transactional(action)
            .doOnSuccess(result -> log.trace("Transacción completada exitosamente: {}", result))
            .doOnError(error -> log.error("Error en transacción reactiva", error));
    }
}
