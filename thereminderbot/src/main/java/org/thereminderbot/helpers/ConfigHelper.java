package org.thereminderbot.helpers;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigHelper {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory(PathHelper.ConfigPath)
            .ignoreIfMissing() // Не падать если нет файла.
            .load();

    /**
     * Получить токен бота.
     * @return String - токен бота.
     */
    public static String getBotToken() {
        String token = dotenv.get("TELEGRAM_BOT_TOKEN");
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException(
                    "TELEGRAM_BOT_TOKEN не найден! " +
                            "Создайте .env файл или установите переменную окружения"
            );
        }
        return token;
    }

    /**
     * Получить имя бота.
     * @return String - имя бота.
     */
    public static String getBotUsername() {
        return dotenv.get("TELEGRAM_BOT_USERNAME", "library_bot");
    }
}
