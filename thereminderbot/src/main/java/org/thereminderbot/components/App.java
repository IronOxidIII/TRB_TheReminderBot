package org.thereminderbot.components;

import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.components.service.ServiceComponent;

import java.io.PrintStream;
import java.util.*;

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
        //TODO
    }

    /**
     * Добавить пользователя.
     */
    private void addUser() {
        try {
            System.out.print("Введите имя пользователя: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                log.warn("Имя пользователя не может быть пустым.");
                return;
            }
            User user = new User(name);
            repositoryComponent.getUserRepository().addUser(user);
            log.info("Пользователь '{}' добавлен с ID {}", name, user.getUserId());
        } catch (Exception e) {
            log.error("Ошибка при добавлении пользователя: {}", e.getMessage());
        }
    }

    /**
     * Удалить пользователя.
     * @param id - строка с id пользователя.
     */
    private void deleteUser(String idStr) {
        if (!ParsingHelper.isId(idStr)) {
            log.warn("Некорректный ID пользователя: {}", idStr);
            return;
        }
        long userId = Long.parseLong(idStr);
        try {
            ServiceComponent.getUserService().deleteUserAndReminds(userId);
            log.info("Пользователь с ID {} и его напоминания удалены.", userId);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при удалении пользователя: {}", e.getMessage());
        }
    }

    /**
     * Удалить напоминание.
     * @param id - строка с id напоминания.
     */
    private void delete_remind(String id) {
        //TODO
    }

    /**
     * Напечатать информацию о напоминании.
     * @param id - строка с id напоминания.
     */
    private void getRemindInfo(String id) {
        //TODO
    }

    /**
     * Напечатать информацию о пользователе.
     */
    private void getUserInfo(String idStr) {
        if (!ParsingHelper.isId(idStr)) {
            log.warn("Некорректный ID пользователя: {}", idStr);
            return;
        }
        long userId = Long.parseLong(idStr);
        try {
            ServiceComponent.getUserService().printUserInfo(userId);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при получении информации о пользователе: {}", e.getMessage());
        }
    }

    /**
     * Вывести все напомининия.
     */
    private void listAllReminds() {
        //TODO
    }

    /**
     * Напечатать все напоминания пользователя.
     */
    private void listUsersReminds(String idStr) {
        if (!ParsingHelper.isId(idStr)) {
            log.warn("Некорректный ID пользователя: {}", idStr);
            return;
        }
        long userId = Long.parseLong(idStr);
        try {
            ServiceComponent.getUserService().printUsersReminds(userId);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при выводе напоминаний пользователя: {}", e.getMessage());
        }
    }

    /**
     * Изменить текст напомининия.
     */
    private void changeRemindText(String id) {
        //TODO
    }

    /**
     * Изменить имя пользователя.
     */
    private void changeUserName(String idStr) {
        if (!ParsingHelper.isId(idStr)) {
            log.warn("Некорректный ID пользователя: {}", idStr);
            return;
        }
        long userId = Long.parseLong(idStr);
        try {
            User user = repositoryComponent.getUserRepository().getUserById(userId);
            log.info("Введите новое имя пользователя:");
            String newName = scanner.nextLine().trim();
            if (newName.isEmpty()) {
                log.warn("Имя пользователя не может быть пустым.");
                return;
            }
            user.setUserName(newName);
            log.info("Имя пользователя с ID {} изменено на '{}'", userId, newName);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при изменении имени пользователя: {}", e.getMessage());
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
    }
}
