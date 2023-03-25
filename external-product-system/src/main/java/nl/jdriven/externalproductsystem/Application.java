package nl.jdriven.externalproductsystem;

import nl.jdriven.externalproductsystem.repositories.ProductRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.EnableScheduling;

import static nl.jdriven.externalproductsystem.constants.Topics.PRODUCTS_TOPIC;


@EnableScheduling
@SpringBootApplication
public class Application implements ApplicationRunner {
    private final ProductRepository productRepository;
    private final StreamBridge streamBridge;

    public Application(ProductRepository productRepository, StreamBridge streamBridge) {
        this.productRepository = productRepository;
        this.streamBridge = streamBridge;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        productRepository.findAll().subscribe(product -> streamBridge.send(PRODUCTS_TOPIC, product));
    }
}
