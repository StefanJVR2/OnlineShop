package nl.jdriven.onlineshopapi.repositories;

import nl.jdriven.onlineshopapi.repositories.entities.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

}
