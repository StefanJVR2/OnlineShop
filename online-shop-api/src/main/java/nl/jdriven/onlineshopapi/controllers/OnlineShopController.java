package nl.jdriven.onlineshopapi.controllers;

import nl.jdriven.onlineshopapi.models.Product;
import nl.jdriven.onlineshopapi.repositories.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class OnlineShopController {

    private final ProductRepository repository;

    public OnlineShopController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products")
    public Flux<Product> getProducts() {
        var products = repository.findAll();
        return products.map(product -> new Product(product.getName(), product.getDescription(), product.getPrice(), product.getImageUrl()));
    }
}
