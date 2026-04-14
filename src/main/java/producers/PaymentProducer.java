package producers;

import helpers.AppConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class PaymentProducer {
    public void sendMessage() {
        try (var producer = new KafkaProducer<String, String>(AppConfig.getProducerProps())) {
            producer.send(new ProducerRecord<>("payments", "PAY-1", "Paid 1200$"));
            producer.send(new ProducerRecord<>("payments", "PAY-2", "Paid 25$"));
            System.out.println("Payments sent.");
        }
    }
}
