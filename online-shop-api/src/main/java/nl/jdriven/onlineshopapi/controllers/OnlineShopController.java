package nl.jdriven.onlineshopapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import nl.jdriven.onlineshopapi.models.Product;
import nl.jdriven.onlineshopapi.repositories.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
public class OnlineShopController {

    private final ProductRepository repository;

    public OnlineShopController(ProductRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Get a single product using id", responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class)))})
    @GetMapping("/products/{id}")
    public Mono<Product> getProduct(@Parameter(description = "The ID of the product") @PathVariable("id") Long id) {
        var product = repository.findById(id);
        return product.map(entity -> new Product(entity.getName(), entity.getDescription(), entity.getPrice(), entity.getImageUrl()));
    }

    @Operation(summary = "Search for a product based on a search term", responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class)))})
    @GetMapping("/products")
    public Flux<Product> getProductBySearchTerm(@Parameter(description = "The search term") @RequestParam(value = "searchTerm", required = false) String searchTerm) {
        var product = Objects.isNull(searchTerm) ? repository.findAll() :
                repository.searchByTerm("%" + searchTerm.toLowerCase() + "%");
        return product.map(entity -> new Product(entity.getName(), entity.getDescription(), entity.getPrice(), entity.getImageUrl()));
    }
}
