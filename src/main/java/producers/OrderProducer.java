package producers;

import helpers.AppConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class OrderProducer {
    public static void main(String[] args) {
        try (var producer = new KafkaProducer<String, String>(AppConfig.getProducerProps())) {
            producer.send(new ProducerRecord<>("orders", "ORD-1", "Laptop"));
            producer.send(new ProducerRecord<>("orders", "ORD-2", "Mouse"));
            System.out.println("Orders sent.");
        }
    }
}
