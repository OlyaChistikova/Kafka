package consumers;

import helpers.AppConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;

public class TransactionConsumer {
    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(AppConfig.getConsumerProps("trans-group"));
        consumer.subscribe(List.of("transactions"));
        while (true) {
            var records = consumer.poll(Duration.ofMillis(500));
            records.forEach(r -> System.out.println("Transaction: " + r.value()));
        }
    }
}
