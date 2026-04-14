package producers;

import helpers.AppConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TransactionProducer {
    public void sendMessage(String message) {
        try (var producer = new KafkaProducer<String, String>(AppConfig.getProducerProps())) {
            for (int i = 1; i <= 5; i++) {
                producer.send(new ProducerRecord<>("transactions", "T-ID-" + i, message + " number: " + i));
            }
            System.out.println("Transactions sent.");
        }
    }
}
