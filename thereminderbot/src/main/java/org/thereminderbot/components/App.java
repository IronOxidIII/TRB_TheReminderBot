package org.thereminderbot.components;

import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.components.service.ServiceComponent;
import java.io.PrintStream;
import java.util.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.Duration;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.components.service.NotificationService;
import org.thereminderbot.components.service.NotificationBuffer;

public class App {
    private final RepositoryComponent repositoryComponent;
    private final ServiceComponent serviceComponent;

    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream userOut = System.out;
    private final NotificationService notificationService;
    private final NotificationBuffer notificationBuffer;
    /**
     * Конструктор по умолчанию.
     */
    public App() {
        repositoryComponent = new RepositoryComponent();
        serviceComponent = new ServiceComponent();
        notificationBuffer = new NotificationBuffer();

        notificationService = new NotificationService(
                repositoryComponent.getRemindRepository(),
                notificationBuffer
        );

        notificationService.start();

        startNotificationListener(notificationBuffer);
    }

    /**
     * Запустить консольного бота.
     */
    public void start() {
        printWelcome();

        while (true) {
            System.out.print("Введите команду: ");
            String command = scanner.nextLine().trim().toLowerCase();
            handleMenuInput(command);
            System.out.println();
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
    private void addUser() {
        //TODO
    }

    /**
     * Удалить пользователя.
     * @param id - строка с id пользователя.
     */
    private void deleteUser(String id) {
        //TODO
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
    private void getUserInfo(String id) {
        //TODO
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
    private void changeUserName(String id) {
        //TODO
    }

    private void printWelcome() {
        System.out.println("|----------------------------------------|");
        System.out.println("|       Консольный TheReminderBot        |");
        System.out.println("|----------------------------------------ad|");
        printHelp();
    }


    private void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("  /add_remind         - Добавить напоминание");
        System.out.println("  /add_user           - Добавить пользователя");
        System.out.println("  /delete_user        - Удалить пользователя");
        System.out.println("  /delete_remind      - Удалить напоминание");
        System.out.println("  /get_remind_info    - Информация о напоминании");
        System.out.println("  /list_all_reminds   - Вывести все напоминания");
        System.out.println("  /change_remind_text - Изменить текст напоминания");
        System.out.println("  /change_username [username]   - Изменить имя пользователя");
        System.out.println("  /help               - Эта справка");
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
            System.out.println("Ввод не может быть пустым.");
            userOut.println("Ввод не может быть пустым.");
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
                    System.out.println(getMethodHelp(inputArr[0]));
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                deleteUser(inputArr[1]);
                break;
            case "/delete_remind":
                if (inputArr.length <= 1) {
                    System.out.println(getMethodHelp(inputArr[0]));
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                delete_remind(inputArr[1]);
                break;
            case "/get_remind_info":
                if (inputArr.length <= 1) {
                    System.out.println(getMethodHelp(inputArr[0]));
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                getRemindInfo(inputArr[1]);
                break;
            case "/get_user_info":
                if (inputArr.length <= 1) {
                    System.out.println(getMethodHelp(inputArr[0]));
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                getUserInfo(inputArr[1]);
                break;
            case "/help":
                printHelp();
                userOut.println(getHelpMessage());
                break;
            case "/list_all_reminds":
                listAllReminds();
                break;
            case "/list_users_reminds":
                if (inputArr.length <= 1) {
                    System.out.println(getMethodHelp(inputArr[0]));
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                listUsersReminds(inputArr[1]);
                break;
            case "/change_remind_text":
                if (inputArr.length <= 1) {
                    System.out.println(getMethodHelp(inputArr[0]));
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                changeRemindText(inputArr[1]);
                break;
            case "/change_username":
                if (inputArr.length <= 1) {
                    System.out.println(getMethodHelp(inputArr[0]));
                    userOut.println(getMethodHelp(inputArr[0]));
                    break;
                }
                changeUserName(inputArr[1]);
                break;
            default:
                System.out.println("Неизвестная команда. Введите /help");
                userOut.println("Неизвестная команда. Введите /help");
                break;
        }
    }

    private String getMethodHelp(String method) {
        String prefix = "Использование команды: ";
        String help;

        switch (method) {
            case "/delete_user":
                help = "/delete_user [userId] - Id пользователя";
                break;
            case "/delete_remind":
                help = "/delete_remind [remindId] - Id напоминания";
                break;
            case "/get_user_info":
                help = "/get_user_info [userId] - Id пользователя";
                break;
            case "/get_remind_info":
                help = "/get_remind_info [remindId] - Id напоминания";
                break;
            case "/list_users_reminds":
                help = "/list_users_reminds [userId] - Id пользователя";
                break;
            case "/change_remind_text":
                help = "/change_remind_text [remindId] - Id напоминания";
                break;
            case "/change_username":
                help = "/change_username [userId] - Id пользователя";
                break;
            default:
                help = "Нет справки для данной команды.";
                break;
        }

        return prefix + help;
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
    private void handleNotification(Remind remind) {
        userOut.println(
                String.format(
                        "Напоминание для пользователя %d: %s",
                        remind.getUserId(),
                        remind.getText()
                )
        );
    }
    private void startNotificationListener(NotificationBuffer buffer) {
        new Thread(() -> {
            while (true) {
                Remind remind = buffer.take();
                handleNotification(remind);
            }
        }).start();
    }
}
