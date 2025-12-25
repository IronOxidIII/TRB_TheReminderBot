package org.thereminderbot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.thereminderbot.components.App;
import org.thereminderbot.components.TheReminderBot;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            App app = new App();
            app.start();
        }
        else if (args.length == 1) {
            if (args[0].equals("tg")) {
                try {
                    // Создаём Telegram Bots API
                    TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

                    // Регистрируем нашего бота
                    TheReminderBot bot = new TheReminderBot();
                    botsApi.registerBot(bot);

                    System.out.println("✅ Bot started successfully!");
                    System.out.println("📝 Bot username: " + bot.getBotUsername());

                } catch (TelegramApiException e) {
                    System.err.println("❌ Failed to start bot: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

}
