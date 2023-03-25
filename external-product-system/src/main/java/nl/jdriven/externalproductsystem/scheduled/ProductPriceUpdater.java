package nl.jdriven.externalproductsystem.scheduled;

import nl.jdriven.externalproductsystem.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static nl.jdriven.externalproductsystem.constants.Topics.PRODUCTS_TOPIC;

@Component
public class ProductPriceUpdater {
    Logger log = LoggerFactory.getLogger(ProductPriceUpdater.class);
    private final StreamBridgeWrapper streamBridge;
    private final ProductRepository productRepository;

    public ProductPriceUpdater(StreamBridgeWrapper streamBridge, ProductRepository productRepository) {
        this.streamBridge = streamBridge;
        this.productRepository = productRepository;
    }

    @Scheduled(fixedDelay = 1000L)
    public void sendProductUpdate() {
        productRepository.findRandom()
                .subscribe(product -> {
                    log.info("Product with ID: {}, Price change: {}", product.getId(), product.getPrice());
                    product.setPrice((Math.random() * 100));
                    productRepository.save(product)
                            .and(savedProduct -> streamBridge.send(PRODUCTS_TOPIC, product))
                            .subscribe();
                });
    }
}
