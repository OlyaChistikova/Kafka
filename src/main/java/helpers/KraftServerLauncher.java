package helpers;

import java.io.File;
import java.io.IOException;

public class KraftServerLauncher {
    private static final String KAFKA_HOME = "C:/Users/Morfys/kafka";
    private static final String BIN_WIN = KAFKA_HOME + "/bin/windows/";
    private static final String CONFIG = KAFKA_HOME + "/config/server.properties";

    public static void startServer() {
        try {
            //указываем сгенерированный id
            String clusterId = "QxnONuwqSWeNLMo2FHKhUQ";

            System.out.println("Шаг 1: Форматирование хранилища...");
            execute("kafka-storage.bat format -t " + clusterId + " -c " + CONFIG);

            Thread.sleep(5000); // Ждем завершения записи на диск

            System.out.println("Шаг 2: Запуск сервера Kafka (KRaft)...");
            execute("kafka-server-start.bat " + CONFIG);

            System.out.println("Сервер запущен.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        try {
            System.out.println("Остановка сервера Kafka...");

            // 1. Пытаемся запустить родной скрипт остановки
            execute("kafka-server-stop.bat");

            // 2. На Windows скрипты часто не убивают процесс полностью,
            // поэтому принудительно завершаем Java-процессы Kafka
            ProcessBuilder killPb = new ProcessBuilder(
                    "taskkill", "/F", "/IM", "java.exe", "/FI", "WINDOWTITLE eq Kafka*"
            );
            killPb.start();

            System.out.println("Команда на остановку отправлена.");
        } catch (Exception e) {
            System.err.println("Ошибка при остановке: " + e.getMessage());
        }
    }


    private static void execute(String kafkaCommand) throws IOException {
        String fullPathToScript = BIN_WIN + kafkaCommand;

        ProcessBuilder pb = new ProcessBuilder(
                "powershell.exe",
                "-NoExit",
                "-Command",
                "Start-Process powershell.exe -ArgumentList '-NoExit', '-Command', '& \"" + fullPathToScript + "\"' "
        );

        pb.directory(new File(KAFKA_HOME));
        pb.start();
    }
}