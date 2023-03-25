package nl.jdriven.externalproductsystem.repositories;

import nl.jdriven.externalproductsystem.repositories.entities.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Random;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    default Mono<Product> findRandom() {
        return count()
                .flatMap(count -> {
                    long randomIndex = new Random().nextLong(1, count + 1);
                    return findById(randomIndex);
                });
    }
}
