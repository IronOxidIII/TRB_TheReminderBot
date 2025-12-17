package org.thereminderbot.components;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.components.service.ServiceComponent;
import org.thereminderbot.constants.BotLanguage;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.helpers.ConfigHelper;

import java.text.ParseException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TheReminderBot extends TelegramLongPollingBot {
    private final RepositoryComponent repositoryComponent = new RepositoryComponent();;
    private final ServiceComponent serviceComponent = new ServiceComponent();

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().hasText() ? update.getMessage().getText() : null;
        if (text == null)
        {
            return;
        }

        User user;
        try {
            user = repositoryComponent.getUserRepository().getUserBySession(chatId);
        }
        catch (Exception e) {
            long newUserId = repositoryComponent.getUserRepository().getNextId();
            user = new User(newUserId, "Test", ZoneId.systemDefault(), UserMenu.MainPage);
            repositoryComponent.getUserRepository().addUser(user);
        }

        // Если есть активное состояние - обработать его
        if (user.getCurrentMenu() != UserMenu.MainPage) {
            handleState(text, user);
            return;
        }
        // Иначе обработать как команду
        handleCommand(text, user);
    }

    @Override
    public String getBotUsername() {
        return ConfigHelper.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return ConfigHelper.getBotToken();
    }

    /**
     * Обработать ввод пользователя, если тот находится не в главном меню.
     * @param text - Сообщение пользователя.
     * @param user - Пользователь.
     */
    private void handleState(String text, User user) {
        switch(user.getCurrentMenu()) {
            case RemindCreateText:
                setRemindText(text, user);
                break;
            case RemindCreateTime:
                setRemindTime(text, user);
                break;
            case RemindCreateFrequency:
                setRemindFrequency(text, user);
                break;
            case RemindsList:
                listReminds(user);
                break;
            case RemindEditText:
                editTextRemind(text, user);
                break;
            default:
                sendMessage(user.getChatId(), "Что-то пошло не так. Попробуйте снова.");
        }
    }


    /**
     * Обработать команду, введенную пользователем.
     * @param command - Команда.
     */
    private void handleCommand(String command, User user) {
        String[] inputArr = command.split(" ");

        switch (inputArr[0]) {
            case "/add_remind":
                addRemind(user);
                break;
            case "/delete_remind":
                if (inputArr.length <= 1) {
                    sendMessage(user.getChatId(), getMethodHelp(inputArr[0]));
                    break;
                }
                deleteRemind(inputArr[1], user);
                break;
            case "/get_remind_info":
                if (inputArr.length <= 1) {
                    sendMessage(user.getChatId(), getMethodHelp(inputArr[0]));
                    break;
                }
                getRemindInfo(inputArr[1], user);
                break;
            case "/help":
                sendHelp(user.getChatId());
                break;
            case "/list_all_reminds":
                listReminds(user);
                break;
            case "/change_remind_text":
                if (inputArr.length <= 1) {
                    sendMessage(user.getChatId(), getMethodHelp(inputArr[0]));
                    break;
                }
                searchRemind(inputArr[1], user, UserMenu.RemindEditText);
                break;
            case "/start":
                sendWelcomeMessage(user.getChatId());
            default:
                sendInvalidOperationMessage(user.getChatId());
                break;
        }
    }

    private void addRemind(User user) {
        user.setCurrentMenu(UserMenu.RemindCreateText);
        sendMessage(user.getChatId(), BotLanguage.EnterRemindText);
    }

    private void setRemindText(String text, User user) {
        if (text == null || text.isEmpty()) {
            sendMessage(user.getChatId(), BotLanguage.InvalidRemindText);
        }

        user.remindConstructor.text = text;
        user.setCurrentMenu(UserMenu.RemindCreateTime);
        sendMessage(user.getChatId(), BotLanguage.EnterRemindTime);
    }

    private void setRemindTime(String text, User user) {
        OffsetDateTime dateTime;
        try
        {
            dateTime = ParsingHelper.parseDateTime(text);
        } catch (DateTimeParseException e) {
            sendMessage(user.getChatId(), BotLanguage.InvalidRemindTime);
            return;
        }

        user.remindConstructor.time = dateTime;
        user.setCurrentMenu(UserMenu.RemindCreateFrequency);
        sendMessage(user.getChatId(), BotLanguage.EnterRemindFrequency);
    }

    private void setRemindFrequency(String text, User user) {
        Duration frequency;
        try {
            frequency = ParsingHelper.parseDuration(text);
        }
        catch (ParseException e) {
            sendMessage(user.getChatId(), BotLanguage.InvalidRemindFrequency);
            return;
        }

        user.remindConstructor.frequencyOfRepetition = frequency;

        long newRemindId = repositoryComponent.getRemindRepository().getAll().size() + 1;
        repositoryComponent.getRemindRepository().addRemind(
                user.remindConstructor.Construct(newRemindId));

        user.setCurrentMenu(UserMenu.MainPage);
    }

    private void sendInvalidOperationMessage(Long chatId) {
        sendMessage(chatId, BotLanguage.InvalidOperation);
    }

    /**
     * Отправить сообщение в чат.
     * @param chatId - Id чата.
     * @param text - Текст сообщения.
     */
    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendHelp(Long chatId) {
        sendMessage(chatId, BotLanguage.Help);
    }

    private void listReminds(User user) {
        var reminds = repositoryComponent.getRemindRepository().getAll();

        if (reminds.isEmpty()) {
            sendMessage(user.getChatId(), "Нет ни одного напоминания.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Список всех напоминаний:");
        for (var remind : reminds) {
            if (remind.getUserId() == user.getUserId()) {
                sb.append(remind.toString());
            }
        }

        sendMessage(user.getChatId(), sb.toString());
    }

    private void getRemindInfo(String remindId, User user) {
        long lRemindId = ParsingHelper.parseLong(remindId);
        if (lRemindId == -1) {
            sendMessage(user.getChatId(), String.format("Некорректный ID напоминания: %s", remindId));
            return;
        }

        try {
            Remind remind = repositoryComponent.getRemindRepository().getRemindById(lRemindId);

            if (remind == null) {
                sendMessage(user.getChatId(), String.format("Напоминание с ID %d не найдено.", remindId));
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Информация о напоминании:");
            sb.append(remind.toString());

        } catch (Exception e) {
            sendMessage(user.getChatId(), "Не удалось получить напоминание. Попробуйте позже.");
        }
    }

    private void editTextRemind(String text, User user) {
        try {
            serviceComponent.getRemindService().changeRemindText(user.remindConstructor.id, text);
            sendMessage(user.getChatId(), "Текст напоминания обновлён.");
        } catch (Exception e) {
            sendMessage(user.getChatId(), "Не удалось изменить текст напоминания. Попробуйте позже.");
        }
    }
    private void deleteRemind(String remindId, User user) {
        Long lRemindId;
        try {
            lRemindId = ParsingHelper.parseLong(remindId);
        } catch (Exception e) {
            sendMessage(user.getChatId(), BotLanguage.InvalidId);
            return;
        }

        try {
            serviceComponent.getRemindService().deleteRemind(lRemindId);
        } catch (IllegalArgumentException e) {
            sendMessage(user.getChatId(), e.getMessage());
            return;
        }

        sendMessage(user.getChatId(), BotLanguage.RemindDeleted);
    }
    private void searchRemind(String id, User user, UserMenu nextMenu) {
        long remindId;
        try {
            remindId = ParsingHelper.parseLong(id);
        } catch (Exception e) {
            sendMessage(user.getChatId(), BotLanguage.InvalidId);
            return;
        }

        if (nextMenu == UserMenu.RemindEditText) {
            user.setCurrentMenu(nextMenu);
            sendMessage(user.getChatId(), String.format("Введите новый текст напоминания %s.", id));
            user.remindConstructor.id = remindId;
        }
    }
    
    private void sendWelcomeMessage(long chatId) {
        sendMessage(chatId, BotLanguage.Welcome);
        sendMessage(chatId, BotLanguage.Help);
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

        static OffsetDateTime parseDateTime(String input) {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Стока не может быть пустой.");
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                return OffsetDateTime.parse(input, formatter);
            } catch (Exception e) {
                throw new DateTimeParseException("Не удалось распарсить время.", input, 0);
            }
        }

        static Duration parseDuration(String input)
        throws ParseException {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Стока не может быть пустой.");
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
                return Duration.ofHours(Integer.parseInt(input));
            }
            catch (NumberFormatException e) {
                throw new ParseException("Не получилось распарсить число.", 1);
            }
        }

        static Long parseLong(String input) {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Строка не может быть пустой.");
            }

            try {
                return Long.parseLong(input);
            } catch (Exception e) {
                throw new NumberFormatException("Не удалось распарсить input в long.");
            }
        }
    }
}
