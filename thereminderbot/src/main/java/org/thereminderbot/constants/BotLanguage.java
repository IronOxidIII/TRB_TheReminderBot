package org.thereminderbot.constants;

public class BotLanguage {
    public static final String Welcome = """
            |----------------------------------------|
            |             The Reminder Bot           |
            |----------------------------------------|
            """;

    public static final String Help = """
            Справка по использованию:
            Доступные команды:
            /add_remind                    - Добавить напоминание
            /list_all_reminds              - Вывести все напоминания
            /help                          - Эта справка
            """;

    public static final String InvalidOperation = "Неизвестная команда. Введите /help";

    public static final String EnterRemindText = "Введите текст нового напоминания.";

    public static final String RemindAdded = "Напоминание успешно добавлено.";

    public static final String InvalidRemindText = "Текст напоминания не должен быть пустым.";

    public static final String EnterRemindTime = "Введите дату и время (формат YYYY-MM-DD hh:mm).";

    public static final String InvalidRemindTime = "Неверно указано время. Проверьте ввод и повторите снова.";

    public static final String EnterRemindFrequency = "Введите частоту повторения напоминания в формате: HH:MM.";

    public static final String InvalidRemindFrequency = "Невено указана частота. Проверьте ввод и повторите снова.";

    public static final String InvalidId = "Не удалось распарсить id, проверьте ввод и повторите снова.";

    public static final String RemindDeleted = "Заметка успешно удалена.";
}
