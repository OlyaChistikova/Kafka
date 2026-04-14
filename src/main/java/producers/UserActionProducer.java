package producers;

import helpers.AppConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class UserActionProducer {
    public void sendMessage() {
        try (var producer = new KafkaProducer<String, String>(AppConfig.getProducerProps())) {
            producer.send(new ProducerRecord<>("user-actions", "user_admin", "ACTION_LOGIN"));
            System.out.println("User action sent.");
        }
    }
}
