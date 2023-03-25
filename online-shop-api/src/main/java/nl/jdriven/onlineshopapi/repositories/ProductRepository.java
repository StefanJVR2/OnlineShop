package nl.jdriven.onlineshopapi.repositories;

import nl.jdriven.onlineshopapi.repositories.entities.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    @Query("SELECT * FROM product WHERE LOWER(name) LIKE :searchTerm " +
            "OR LOWER(description) LIKE :searchTerm")
    Flux<Product> searchByTerm(String searchTerm);

}
