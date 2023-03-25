package nl.jdriven.externalproductsystem.scheduled;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class StreamBridgeWrapper {
    private final StreamBridge streamBridge;

    public StreamBridgeWrapper(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void send(String destination, Object payload) {
        streamBridge.send(destination, payload);
    }
}
