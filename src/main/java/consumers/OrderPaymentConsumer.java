package consumers;

import helpers.AppConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderPaymentConsumer {
    public final List<String> buffer = new CopyOnWriteArrayList<>();
    public volatile boolean keepRunning = true;

    public void getMessage() {
        var consumer = new KafkaConsumer<String, String>(AppConfig.getConsumerProps("combined-group"));
        consumer.subscribe(List.of("orders", "payments")); // Подписка на два топика
        while (keepRunning) {
            var records = consumer.poll(Duration.ofMillis(500));
            records.forEach(r -> {
                String msg = String.format("Topic: %s | Data: %s", r.topic(), r.value());
                buffer.add(msg);
                System.out.println(msg);
            });
        }
    }
}
