package nl.jdriven.onlineshopapi;

import nl.jdriven.onlineshopapi.consumers.ProductsConsumer;
import nl.jdriven.onlineshopapi.controllers.OnlineShopController;
import nl.jdriven.onlineshopapi.repositories.ProductRepository;
import nl.jdriven.onlineshopapi.repositories.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(OnlineShopController.class)
class OnlineShopControllerTests {
    @MockBean
    ProductRepository productRepository;
    @MockBean
    ProductsConsumer productsConsumer;

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("Given a mocked consumer" +
            "When the /products endpoint is called without a searchTerm" +
            "Then all products should be returned")
    @Test
    void getProductsSuccess() {
        // Define the mock behavior for the repository
        Product product1 = buildProduct(1L);
        Product product2 = buildProduct(2L);

        when(productRepository.findAll()).thenReturn(Flux.just(product1, product2));

        webTestClient.get().uri("/products")
                .exchange().expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(2);
    }

    @DisplayName("Given a mocked consumer" +
            "When the /products endpoint is called with a searchTerm" +
            "Then only products with the term should be returned")
    @Test
    void getProductsWithSearchSuccess() {
        // Define the mock behavior for the repository
        Product product1 = buildProduct(1L);

        when(productRepository.searchByTerm(anyString())).thenReturn(Flux.just(product1));

        webTestClient.get().uri("/products?searchTerm=term")
                .exchange().expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(1);
    }

    private Product buildProduct(long id) {
        Product product = new Product();
        product.setId(id);
        product.setName("Test");
        product.setDescription("Description");
        product.setImageUrl("http://localhost/url");
        product.setPrice(2D);

        return product;
    }
}
