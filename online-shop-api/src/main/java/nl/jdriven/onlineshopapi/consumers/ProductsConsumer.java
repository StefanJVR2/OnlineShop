package nl.jdriven.onlineshopapi.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jdriven.onlineshopapi.exceptions.OnlineShopException;
import nl.jdriven.onlineshopapi.repositories.ProductRepository;
import nl.jdriven.onlineshopapi.repositories.entities.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ProductsConsumer {
    Logger log = LoggerFactory.getLogger(ProductsConsumer.class);
    private final ProductRepository productRepository;

    public ProductsConsumer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Bean
    public Consumer<String> onReceive() {
        var mapper = new ObjectMapper();
        return message -> {
            try {
                var product = mapper.readValue(message, Product.class);

                assert product.getId() != null;

                productRepository
                        .existsById(product.getId())
                        .flatMap(exists -> {
                            if (Boolean.FALSE.equals(exists))
                                product.setInsertProduct(true);

                            log.info("Product with ID: {}, Price change: {}", product.getId(), product.getPrice());
                            return productRepository.save(product);
                        }).subscribe();

            } catch (JsonProcessingException jsonProcessingException) {
                throw new OnlineShopException("Error processing product", jsonProcessingException);
            } catch (AssertionError assertionError) {
                throw new OnlineShopException("Product does not have a valid ID", assertionError);
            }
        };
    }
}
