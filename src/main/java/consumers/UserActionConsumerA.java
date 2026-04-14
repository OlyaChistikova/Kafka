package consumers;

import helpers.AppConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserActionConsumerA {
    public final List<String> buffer = new CopyOnWriteArrayList<>();
    public volatile boolean keepRunning = true;

    public void getMessage() {
        var consumer = new KafkaConsumer<String, String>(AppConfig.getConsumerProps("groupA"));
        consumer.subscribe(List.of("user-actions"));
        while (keepRunning) {
            consumer.poll(Duration.ofMillis(500)).forEach(r -> buffer.add(r.value()));
        }
        consumer.close();
    }
}
