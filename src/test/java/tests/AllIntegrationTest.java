package tests;

import consumers.OrderPaymentConsumer;
import consumers.TransactionConsumer;
import consumers.UserActionConsumerA;
import consumers.UserActionConsumerB;
import helpers.KraftServerLauncher;
import helpers.TopicManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import producers.OrderProducer;
import producers.PaymentProducer;
import producers.TransactionProducer;
import producers.UserActionProducer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class AllIntegrationTest {

    @BeforeAll
    public static void startServerAndCreateTopics(){
        KraftServerLauncher.startServer();
        TopicManager.createTopics();
    }

    @AfterAll
    public static void stopServer(){
        TopicManager.deleteTopics(List.of("transactions", "orders", "payments", "user-actions"));
        KraftServerLauncher.stopServer();
    }

    @Test
    public void testTransaction(){
        TransactionProducer producer = new TransactionProducer();
        TransactionConsumer consumer = new TransactionConsumer();

        CompletableFuture.runAsync(consumer::getMessage);
        producer.sendMessage("My message");

        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> consumer.buffer.size() == 5);

        List<String> expectedMessages = List.of(
                "My message number: 1",
                "My message number: 2",
                "My message number: 3",
                "My message number: 4",
                "My message number: 5"
        );

        Assertions.assertIterableEquals(expectedMessages, consumer.buffer,
                "Списки сообщений не совпадают");
    }

    @Test
    public void testOrderPaymentTopics() {
        OrderPaymentConsumer consumer = new OrderPaymentConsumer();
        OrderProducer orderProducer = new OrderProducer();
        PaymentProducer paymentProducer = new PaymentProducer();

        CompletableFuture.runAsync(consumer::getMessage);

        orderProducer.sendMessage();
        paymentProducer.sendMessage();

        await().atMost(10, TimeUnit.SECONDS).until(() -> consumer.buffer.size() >= 2);

        List<String> results = consumer.buffer;
        Assertions.assertEquals(4, results.size(), "Должно быть 4 сообщения");

        boolean hasOrder = results.stream().anyMatch(s -> s.contains("Topic: orders"));
        boolean hasPayment = results.stream().anyMatch(s -> s.contains("Topic: payments"));

        Assertions.assertTrue(hasOrder, "Сообщение из топика orders не получено");
        Assertions.assertTrue(hasPayment, "Сообщение из топика payments не получено");

        consumer.keepRunning = false;
    }

    @Test
    public void testMultipleGroups() {
        UserActionConsumerA consumerA = new UserActionConsumerA();
        UserActionConsumerB consumerB = new UserActionConsumerB();
        UserActionProducer userActionProducer = new UserActionProducer();

        CompletableFuture.runAsync(consumerA::getMessage);
        CompletableFuture.runAsync(consumerB::getMessage);

        userActionProducer.sendMessage();

        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> consumerA.buffer.size() == 1);

        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> consumerB.buffer.size() == 1);

        String expectedAction = "ACTION_LOGIN";
        Assertions.assertEquals(expectedAction, consumerA.buffer.get(0));
        Assertions.assertEquals(expectedAction, consumerB.buffer.get(0));

        consumerA.keepRunning = false;
        consumerB.keepRunning = false;
    }
}