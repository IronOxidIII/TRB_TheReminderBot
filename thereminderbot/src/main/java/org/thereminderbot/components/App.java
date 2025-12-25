package org.thereminderbot.components;

import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.components.service.ServiceComponent;
import org.thereminderbot.domain.Remind;

import java.io.PrintStream;
import java.util.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thereminderbot.domain.User;

public class App {
    private final RepositoryComponent repositoryComponent;
    private final ServiceComponent serviceComponent;

    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream userOut = System.out;

    /**
     * Конструктор по умолчанию.
     */
    public App() {
        repositoryComponent = new RepositoryComponent();
        serviceComponent = new ServiceComponent();
    }

    /**
     * Запустить консольного бота.
     */
    public void start() {
        printWelcome();

        while (true) {
            userOut.println("Введите команду: ");
            String command = scanner.nextLine().trim().toLowerCase();
            handleMenuInput(command);
            userOut.println();
        }
    }

    /**
     * Добавить напоминание.
     */
    private void addRemind() {
        long userId = readUserId();
        if (userId == -1) return;
        String text = readRemindText();
        if (text == null) return;
        OffsetDateTime time = readRemindTime();
        if (time == null) return;
        Duration frequency = readInterval();
        if (frequency == null) return;
        long remindId = repositoryComponent.getRemindRepository().getAll().size() + 1;
        Remind remind = new Remind(remindId, text, userId, time, frequency);
        repositoryComponent.getRemindRepository().addRemind(remind);

        userOut.println(String.format("Напоминание ID %d добавлено пользователю %d.", remindId, userId));
    }

    /**
     * Чтение ID пользователя
     */
    private long readUserId() {
        userOut.print("Введите ID пользователя: ");
        String userIdStr = scanner.nextLine().trim();

        if (!ParsingHelper.isId(userIdStr)) {
            userOut.println(String.format("Некорректный ID пользователя: %s", userIdStr));
            return -1;
        }

        long userId = Long.parseLong(userIdStr);
        var user = repositoryComponent.getUserRepository().getUserById(userId);
        if (user == null) {
            userOut.println(String.format("Пользователь с ID %d не найден.", userId));
            return -1;
        }
        return userId;
    }

    /**
     * Чтение текста напоминания
     */
    private String readRemindText() {
        userOut.print("Введите текст напоминания: ");
        String text = scanner.nextLine().trim();
        if (text.isEmpty()) {
            userOut.println("Текст напоминания не может быть пустым.");
            return null;
        }
        return text;
    }

    /**
     * Чтение даты и времени напоминания
     */
    private OffsetDateTime readRemindTime() {
        userOut.print("Введите дату и время (формат YYYY-MM-DD HH:MM): ");
        String timeStr = scanner.nextLine().trim();
        try {
            var localDT = java.time.LocalDateTime.parse(
                    timeStr,
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );

            ZoneId userZone = ZoneId.systemDefault();
            return localDT.atZone(userZone).toOffsetDateTime();
        } catch (Exception e) {
            userOut.println(String.format("Некорректная дата/время: %s", timeStr));
            return null;
        }
    }

    /**
     * Чтение интервала повторения напоминания в часах
     */
    private Duration readInterval() {
        userOut.print("Введите интервал в часах (например 0, 1, 24): ");
        String intervalStr = scanner.nextLine().trim();
        try {
            long hours = Long.parseLong(intervalStr);
            return Duration.ofHours(hours);
        } catch (NumberFormatException e) {
            userOut.println(String.format("Некорректный интервал: %s", intervalStr));
            return null;
        }
    }
    /**
     * Добавить пользователя.
     */
    private static long nextUserId = 1;

    private void addUser() {
        try {
            userOut.print("Введите имя пользователя: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                userOut.println("Имя пользователя не может быть пустым.");
                return;
            }

            userOut.print("Введите часовой пояс пользователя (например, Europe/Moscow): ");
            String tzInput = scanner.nextLine().trim();
            if (tzInput.isEmpty()) {
                userOut.println("Часовой пояс пользователя не может быть пустым.");
                return;
            }

            ZoneId zone;
            try {
                zone = ZoneId.of(tzInput);
            } catch (Exception e) {
                userOut.println("Некорректный часовой пояс: " + tzInput);
                return;
            }

            long id = nextUserId++;
            int defaultMenu = 0;
            User user = new User(id, name, zone, defaultMenu);

            repositoryComponent.getUserRepository().addUser(user);
            userOut.println(String.format("Пользователь '%s' добавлен с ID %d", name, id));

        } catch (Exception e) {
            userOut.println("Ошибка при добавлении пользователя: " + e.getMessage());
        }
    }

    /**
     * Удалить пользователя.
     * @param idStr - строка с id пользователя.
     */
    private void deleteUser(String idStr) {
        long userId = ParsingHelper.parseId(idStr);
        if (userId == -1) {
            userOut.println(String.format("Некорректный ID пользователя: %s", idStr));
            return;
        }

        try {
            repositoryComponent.getUserRepository().deleteUser(userId);
            userOut.println(String.format(
                    "Пользователь с ID %d и его напоминания удалены.",
                    userId
            ));
        } catch (IllegalArgumentException e) {
            userOut.println(String.format(
                    "Ошибка при удалении пользователя: %s",
                    e.getMessage()
            ));
        }
    }

    /**
     * Удалить напоминание.
     * @param idStr - строка с id напоминания.
     */
    private void delete_remind(String idStr) {
        long id = ParsingHelper.parseId(idStr);
        if (id == -1) {
            userOut.println(String.format("Некорректный ID напоминания: %s", idStr));
            return;
        }

        try {
            serviceComponent.getRemindService().deleteRemind(id);
            userOut.println(String.format("Напоминание с ID %d удалено.", id));
        } catch (Exception e) {
            userOut.println("Не удалось удалить напоминание. Попробуйте позже.");
        }
    }

    /**
     * Напечатать информацию о напоминании.
     * @param idStr - строка с id напоминания.
     */
    private void getRemindInfo(String idStr) {
        long remindId = ParsingHelper.parseId(idStr);
        if (remindId == -1) {
            userOut.println(String.format("Некорректный ID напоминания: %s", idStr));
            return;
        }

        try {
            Remind remind = repositoryComponent.getRemindRepository().getRemindById(remindId);

            if (remind == null) {
                userOut.println(String.format("Напоминание с ID %d не найдено.", remindId));
                return;
            }

            userOut.println("Информация о напоминании:");
            userOut.println(remind);

        } catch (Exception e) {
            userOut.println("Не удалось получить напоминание. Попробуйте позже.");
        }
    }

    /**
     * Напечатать информацию о пользователе.
     */
    private void getUserInfo(String idStr) {
        long userId = ParsingHelper.parseId(idStr);
        if (userId == -1) {
            userOut.println(String.format("Некорректный ID пользователя: %s", idStr));
            return;
        }

        try {
            serviceComponent.getUserService().printUserInfo(userId);
        } catch (IllegalArgumentException e) {
            userOut.println(String.format(
                    "Ошибка при получении информации о пользователе: %s",
                    e.getMessage()
            ));
        }
    }

    /**
     * Вывести все напомининия.
     */
    private void listAllReminds() {
        var reminds = repositoryComponent.getRemindRepository().getAll();

        if (reminds.isEmpty()) {
            userOut.println("Нет ни одного напоминания.");
            return;
        }

        userOut.println("Список всех напоминаний:");
        for (var remind : reminds) {
            userOut.println(remind);
        }
    }

    /**
     * Напечатать все напоминания пользователя.
     */
    private void listUsersReminds(String idStr) {
        long userId = ParsingHelper.parseId(idStr);
        if (userId == -1) {
            userOut.println(String.format("Некорректный ID пользователя: %s", idStr));
            return;
        }

        var reminds = repositoryComponent.getRemindRepository().getRemindsByUser(userId);

        if (reminds.isEmpty()) {
            userOut.println(String.format("У пользователя с ID %d нет напоминаний.", userId));
            return;
        }

        userOut.println(String.format("Напоминания пользователя %d:", userId));
        for (var r : reminds) {
            userOut.println(r);
        }
    }

    /**
     * Изменить текст напоминания.
     */
    private void changeRemindText(String idStr) {
        long remindId = ParsingHelper.parseId(idStr);
        if (remindId == -1) {
            userOut.println(String.format("Некорректный ID напоминания: %s", idStr));
            return;
        }

        userOut.print("Введите новый текст напоминания: ");
        String newText = scanner.nextLine().trim();

        if (newText.isEmpty()) {
            userOut.println("Текст не может быть пустым.");
            return;
        }

        try {
            serviceComponent.getRemindService().changeRemindText(remindId, newText);
            userOut.println("Текст напоминания обновлён.");
        } catch (Exception e) {
            userOut.println("Не удалось изменить текст напоминания. Попробуйте позже.");
        }
    }

    /**
     * Изменить имя пользователя.
     */
    private void changeUserName(String idStr) {
        long userId = ParsingHelper.parseId(idStr);
        if (userId == -1) {
            userOut.println(String.format("Некорректный ID пользователя: %s", idStr));
            return;
        }

        try {
            User user = repositoryComponent.getUserRepository().getUserById(userId);
            userOut.print("Введите новое имя пользователя: ");
            String newName = scanner.nextLine().trim();

            if (newName.isEmpty()) {
                userOut.println("Имя пользователя не может быть пустым.");
                return;
            }

            user.setUserName(newName);
            userOut.println(String.format(
                    "Имя пользователя с ID %d изменено на '%s'",
                    userId, newName
            ));
        } catch (IllegalArgumentException e) {
            userOut.println(String.format(
                    "Ошибка при изменении имени пользователя: %s",
                    e.getMessage()
            ));
        }
    }

    private void printWelcome() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("|----------------------------------------|");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("|       Консольный TheReminderBot        |");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("|----------------------------------------|");
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append(getHelpMessage());
        userOut.println(stringBuilder.toString());
    }


    private String getHelpMessage() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Справка по использованию:");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Доступные команды:");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /add_remind                    - Добавить напоминание");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /add_user                      - Добавить пользователя");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /delete_user [userId]          - Удалить пользователя");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /delete_remind [userId]        - Удалить напоминание");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /get_remind_info [remindId]    - Информация о напоминании");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /list_all_reminds              - Вывести все напоминания");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /change_remind_text [remindId] - Изменить текст напоминания");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /change_username [userId]      - Изменить имя пользователя");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("  /help                          - Эта справка");

        return stringBuilder.toString();
    }

    private void handleMenuInput(String input) {
        if (input == null || input.isEmpty()) {
            userOut.println("Ввод не можнт быть пустым.");
        }

        String[] inputArr = input.split(" ");

        switch (inputArr[0]) {
            case "/add_remind":
                addRemind();
                break;
            case "/add_user":
                addUser();
                break;
            case "/delete_user":
                if (inputArr.length <= 1) {
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                deleteUser(inputArr[1]);
                break;
            case "/delete_remind":
                if (inputArr.length <= 1) {
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                delete_remind(inputArr[1]);
                break;
            case "/get_remind_info":
                if (inputArr.length <= 1) {
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                getRemindInfo(inputArr[1]);
                break;
            case "/get_user_info":
                if (inputArr.length <= 1) {
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                getUserInfo(inputArr[1]);
                break;
            case "/help":
                userOut.println(getHelpMessage());
                break;
            case "/list_all_reminds":
                listAllReminds();
                break;
            case "/list_users_reminds":
                if (inputArr.length <= 1) {
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                listUsersReminds(inputArr[1]);
                break;
            case "/change_remind_text":
                if (inputArr.length <= 1) {
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                changeRemindText(inputArr[1]);
                break;
            case "/change_username":
                if (inputArr.length <= 1) {
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                changeUserName(inputArr[1]);
                break;
            default:
                userOut.println("Неизвестная команда. Введите /help");
                break;
        }
    }

    private String getMethodHelp (String method) {
        String prefix = "Использование команды: ";
        return prefix + switch (method) {
            case "/delete_user" -> "/delete_user [userId] - Id пользователя";
            case "/delete_remind" -> "/delete_remind [remindId] - Id напоминания";
            case "/get_user_info" -> "/get_user_info [userId] - Id пользователя";
            case "/get_remind_info" -> "/get_remind_info [remindId] - Id напоминания";
            case "/list_users_reminds" -> "/list_users_reminds [userId] - Id пользователя";
            case "/change_remind_text" -> "/change_remind_text [remindId] - Id напоминания";
            case "/change_username" -> "/change_username [userId] - Id пользователя";
            default -> "Нет справки для данного метода.";
        };
    }

    static public class ParsingHelper {
        static boolean isId(String input) {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Строка не может быть пустой.");
            }

            try {
                Long.parseLong(input);
            }
            catch (NumberFormatException e) {
                return false;
            }

            return true;
        }

        static long parseId(String idStr) {
            if (!isId(idStr)) {
                return -1;
            }
            return Long.parseLong(idStr);
        }
    }
}