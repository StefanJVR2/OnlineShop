package nl.jdriven.externalproductsystem;

import nl.jdriven.externalproductsystem.repositories.ProductRepository;
import nl.jdriven.externalproductsystem.repositories.entities.Product;
import nl.jdriven.externalproductsystem.scheduled.ProductPriceUpdater;
import nl.jdriven.externalproductsystem.scheduled.StreamBridgeWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class ProducePriceUpdaterTests {
    @MockBean
    private StreamBridgeWrapper mockStreamBridge;

    @Autowired
    private ProductPriceUpdater productPriceUpdater;
    @SpyBean
    private ProductRepository productRepository;
    private static final String PRODUCTS_TOPIC = "products-topic";

    @DisplayName("When the application starts" +
            "Then events are sent within 2 seconds")
    @Test
    void sendUpdateIsExecuted() {
        // Wait for the sendProductUpdate() method to be called within a span of 2 seconds
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> verify(mockStreamBridge, atLeastOnce()).send(eq(PRODUCTS_TOPIC), any(Product.class)));
    }

    @DisplayName("When a produce price update is sent" +
            "Then a random product must be selected from the repository")
    @Test
    void selectedRandomProduct() {
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> verify(productRepository, atLeastOnce()).findRandom());
    }

    @DisplayName("When a produce price update is sent" +
            "Then the price is correctly updated")
    @Test
    void priceIsUpdatedCorrectly() {
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository).save(productCaptor.capture());

            var capturedProduct = productCaptor.getValue();
            assertTrue(capturedProduct.getPrice() < 100.00);
            assertTrue(capturedProduct.getPrice() > 0.00);
        });
    }

    @DisplayName("When a price change event is produced" +
            "Then the product should be saved to the database and placed on the queue")
    @Test
    void saveProductToDbAndAddToQueue() {
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            verify(productRepository, atLeastOnce()).save(any(Product.class));
            verify(mockStreamBridge, atLeastOnce()).send(eq(PRODUCTS_TOPIC), any(Product.class));
        });
    }
}
