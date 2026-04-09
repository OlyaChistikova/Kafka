package helpers;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TopicManager {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(props)) {

            List<NewTopic> newTopics = Arrays.asList(
                    new NewTopic("transactions", 1, (short) 1),
                    new NewTopic("orders", 1, (short) 1),
                    new NewTopic("payments", 1, (short) 1),
                    new NewTopic("user-actions", 1, (short) 1)
            );

            adminClient.createTopics(newTopics).all().get();
            System.out.println("Успех! Топики созданы: transactions, orders, payments, user-actions");
        } catch (Exception e) {
            System.out.println("Возможно, топики уже созданы или сервер не запущен.");
            e.printStackTrace();
        }
    }
}
