package org.thereminderbot.components;

import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.components.service.ServiceComponent;
import java.io.PrintStream;
import java.util.*;

public class App {
    private final RepositoryComponent repositoryComponent;
    private final ServiceComponent serviceComponent;
    private final Set<String> commands;

    private final Scanner scanner = new Scanner(System.in);

    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream userOut = System.out;

    /**
     * Конструктор по умолчанию.
     */
    public App() {
        repositoryComponent = new RepositoryComponent();
        serviceComponent = new ServiceComponent();
        commands = new HashSet<>();
        initializeCommands();
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
    private void getUserInfo(String id) {
        //TODO
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
    private void listUsersReminds(String id) {
        //TODO
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
            System.out.println("Ввод не можнт быть пустым.");
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

        private String getMethodHelp (String method) {
        String prefix = "Использование команды: ";
        return prefix + switch (method) {
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

        private void initializeCommands () {
            commands.add("/add_remind");
            commands.add("/add_user");
            commands.add("/delete_user");
            commands.add("/delete_remind");
            commands.add("/get_remind_info");
            commands.add("/get_user_info");
            commands.add("/help");
            commands.add("/list_all_reminds");
            commands.add("/list_users_reminds");
            commands.add("/change_remind_text");
            commands.add("/change_username");
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
