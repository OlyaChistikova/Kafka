package consumers;

import helpers.AppConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionConsumer {
    public final List<String> buffer = new CopyOnWriteArrayList<>();

    public void getMessage() {
        var consumer = new KafkaConsumer<String, String>(AppConfig.getConsumerProps("trans-group"));
        consumer.subscribe(List.of("transactions"));
        while (true) {
            var records = consumer.poll(Duration.ofMillis(500));
            records.forEach(r -> {
                buffer.add(r.value()); // Сохраняем для теста
                System.out.println("Transaction: " + r.value());
            });
        }
    }
}
