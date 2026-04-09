package consumers;

import helpers.AppConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;

public class OrderPaymentConsumer {
    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(AppConfig.getConsumerProps("combined-group"));
        consumer.subscribe(List.of("orders", "payments")); // Подписка на два топика
        while (true) {
            var records = consumer.poll(Duration.ofMillis(500));
            records.forEach(r -> System.out.printf("Topic: %s | Data: %s%n", r.topic(), r.value()));
        }
    }
}
