package consumers;

import helpers.AppConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;

public class UserActionConsumerB {
    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(AppConfig.getConsumerProps("groupB"));
        consumer.subscribe(List.of("user-actions"));
        while (true) {
            consumer.poll(Duration.ofMillis(500)).forEach(r -> System.out.println("Group B received: " + r.value()));
        }
    }
}
