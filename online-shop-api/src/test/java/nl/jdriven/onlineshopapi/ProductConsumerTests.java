package nl.jdriven.onlineshopapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.jdriven.onlineshopapi.consumers.ProductsConsumer;
import nl.jdriven.onlineshopapi.exceptions.OnlineShopException;
import nl.jdriven.onlineshopapi.repositories.ProductRepository;
import nl.jdriven.onlineshopapi.repositories.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductConsumerTests {

    @SpyBean
    ProductRepository productRepository;
    @Autowired
    ProductsConsumer productsConsumer;

    @DisplayName("When the onReceive method is executed" +
            "Then a consumer of type string should be returned")
    @Test
    void testOnReceiveIsConsumer() {
        // When
        Object consumer = productsConsumer.onReceive();

        // Assert
        assertNotNull(consumer);
        assertTrue(consumer instanceof Consumer<?>);
    }

    @DisplayName("Given a valid product " +
            "When the onReceive method is executed" +
            "Then the product should be added to the database")
    @Test
    void testOnReceiveSuccess() {
        // Given
        String message = "{\"id\": 1234, \"name\": \"Product 1\", \"description\": \"Description 1\", \"price\": 9.99}";
        Consumer<String> consumer = productsConsumer.onReceive();

        // When
        when(productRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        // Perform - Invoke the onReceive() method with the message
        consumer.accept(message);

        // Assert - Verify that the ProductRepository's existsById() and save() methods were called with the correct arguments
        verify(productRepository, times(1)).existsById(1234L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("Given a product with no primary key" +
            "When the onReceive method is executed" +
            "Then the onReceive method should throw an Exception with the appropriate message")
    @Test
    void testOnReceiveInvalidPrimaryKey() {
        // Given
        String message = "{\"name\": \"Product 1\", \"description\": \"Description 1\", \"price\": 9.99}";
        Consumer<String> consumer = productsConsumer.onReceive();

        // Perform
        String resultMessage = assertThrows(OnlineShopException.class, () -> consumer.accept(message)).getMessage();

        // Assert
        assertEquals("Product does not have a valid ID", resultMessage);
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @DisplayName("Given a product with an invalid json structure" +
            "When the onReceive method is executed" +
            "Then the onReceive method should throw an Exception with the appropriate message")
    @Test
    void testOnReceiveInvalidJson() {
        // Given
        String message = "\"name\": \"Product 1\", \"description\": \"Description 1\", \"price\": 9.99}";
        Consumer<String> consumer = productsConsumer.onReceive();

        // Perform
        String resultMessage = assertThrows(OnlineShopException.class, () -> consumer.accept(message)).getMessage();

        // Assert
        assertEquals("Error processing product", resultMessage);
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @DisplayName("Given a valid product " +
            "When the onReceive method is executed" +
            "Then the isNewProduct methods should be called when necessary")
    @Test
    void testOnReceiveSetsIsNewRecord() throws JsonProcessingException {
        // Given
        String message = "{\"id\": 1234, \"name\": \"Product 1\", \"description\": \"Description 1\", \"price\": 9.99}";
        Consumer<String> consumer = productsConsumer.onReceive();

        // When
        when(productRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        // Perform
        consumer.accept(message);

        // Assert
        // Capture argument
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();

        assertEquals(1234L, savedProduct.getId());
        assertTrue(savedProduct.isNew());
    }
}
