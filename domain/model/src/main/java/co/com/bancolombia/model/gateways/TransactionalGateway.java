package co.com.bancolombia.model.gateways;

import reactor.core.publisher.Mono;

public interface TransactionalGateway {
    <T> Mono<T> transactional(Mono<T> action);
}
