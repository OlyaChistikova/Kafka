## 🛠 Предварительные требования

1. **Java 17**.
2. **Apache Kafka 4.3.0** (скачанная и распакованная).
3. Библиотека `kafka-clients` в `pom.xml`.

## 🚀 Быстрый запуск

### 1. Запуск сервера Kafka
Откройте и запустите класс `KraftServerLauncher`. 
* Запустит сервер брокера в отдельном окне **PowerShell**.
* Используется конфиг: `config/kraft/server.properties`.

### 2. Создание топиков
Запустите класс `TopicManager`. Он создаст необходимые топики через `AdminClient`:
- `transactions`
- `orders`
- `payments`
- `user-actions`

## 📝 Описание реализации

### Задание 1: Transactions
- **Producer:** `TransactionProducer` — отправляет 5 тестовых сообщений.
- **Consumer:** `TransactionConsumer` — читает сообщения и выводит их в консоль.

### Задание 2: Orders & Payments
- **Order-Producer:** Реализован `OrderProducer`, который отправляет сообщение в топик `orders`.
- **Payment-Producer:** Реализован `PaymentProducer`, который отправляет сообщение в топик `payments`.
- **Combined Consumer:** `OrderPaymentConsumer` — один читатель, подписанный сразу на два топика (`orders` и `payments`).

### Задание 3: Группы потребителей (User Actions)
- **Topic:** `user-actions`.
- **Consumer Groups:** Реализованы два независимых класса `UserActionConsumerA` и `UserActionConsumerB` (разные `group.id`). 
- **Результат:** Одно сообщение, отправленное в топик, доставляется **обоим** группам одновременно.
