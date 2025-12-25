package org.thereminderbot.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.components.service.NotificationBuffer;
import org.thereminderbot.components.service.NotificationService;
import org.thereminderbot.components.service.ServiceComponent;
import org.thereminderbot.constants.BotConstants;
import org.thereminderbot.constants.BotLanguage;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;
import org.thereminderbot.domain.UserSession;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.helpers.ConfigHelper;
import org.thereminderbot.helpers.ParsingHelper;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheReminderBot extends TelegramLongPollingBot {
    private final RepositoryComponent repositoryComponent = new RepositoryComponent();;
    private final ServiceComponent serviceComponent = new ServiceComponent(repositoryComponent);
    private final NotificationService notificationService =
            new NotificationService(repositoryComponent.getRemindRepository());

    private Logger log = LoggerFactory.getLogger(TheReminderBot.class);
    private final ExecutorService notificationListenerExecutor =
            Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "notification-listener");
                t.setDaemon(true);
                return t;
            });

    public TheReminderBot() {
        notificationService.start();
        startNotificationListener();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            notificationListenerExecutor.shutdownNow();
            notificationService.stop();
        }, "shutdown-hook"));
    }

    @Override
    public void onUpdateReceived(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId;
        if (callbackQuery != null) {
            chatId = callbackQuery.getMessage().getChatId();
        }
        else {
            chatId = update.getMessage().getChatId();
        }

        UserSession userSession;
        try {
            userSession = repositoryComponent
                    .getUserSessionRepository()
                    .getUserSession(chatId);
        }
        catch (Exception e) {
            long newUserId = repositoryComponent.getUserRepository().getNextId();
            User user = new User(newUserId, "Test", ZoneId.systemDefault(), UserMenu.MainPage);
            repositoryComponent.getUserRepository().addUser(user);

            userSession = new UserSession(user, chatId);
            repositoryComponent.getUserSessionRepository().addUserSession(chatId, userSession);
        }

        if (update.hasCallbackQuery()) {
            handleState(update.getCallbackQuery().getData(), userSession);
            return;
        }

        // Если есть активное состояние - обработать его
        if (userSession.getUser().getCurrentMenu() != UserMenu.MainPage) {
            if (update.hasMessage() && update.getMessage().isCommand()) {
                userSession.clear();
                userSession.getUser().setCurrentMenu(UserMenu.MainPage);
                handleCommand(update.getMessage().getText(), userSession);
                return;
            }
            handleState(update.getMessage().getText(), userSession);
            return;
        }
        // Иначе обработать как команду
        handleCommand(update.getMessage().getText(), userSession);
    }

    /**
     * Обработать ввод пользователя, если тот находится не в главном меню.
     * @param text - Сообщение пользователя.
     * @param session - Сессия пользователя.
     */
    private void handleState(String text, UserSession session) {
        switch(session.getUser().getCurrentMenu()) {
            case RemindCreateText:
                setRemindText(text, session);
                break;
            case RemindCreateTime:
                setRemindTime(text, session);
                break;
            case RemindCreateFrequency:
                setRemindFrequency(text, session);
                break;
            case RemindsList:
                handleRemindListButtonClick(text, session);
                break;
            case RemindEditText:
                editTextRemind(text, session);
                break;
            case RemindMenu:
                handleRemindMenu(text, session);
                break;
            default:
                sendMessage(session.getChatId(), "Что-то пошло не так. Попробуйте снова.");
        }
    }

    private void handleRemindMenu(String text, UserSession session) {
        switch(text) {
            case "ChangeRemindText":
                session.getUser().setCurrentMenu(UserMenu.RemindEditText);
                sendMessage(session.getChatId(), "Введите новый текст напоминания.");
                break;
            case "DeleteRemind":
                session.getUser().setCurrentMenu(UserMenu.RemindDelete);
                deleteRemind(session);
        }
    }

    private void handleRemindListButtonClick(String text, UserSession session) {
        switch (text) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                session.getUser().setCurrentMenu(UserMenu.RemindMenu);;
                sendRemindMenu(getRemindId(Integer.parseInt(text), session), session);
                break;
            case "<-":
                try {
                    session.decRemindPage();
                } catch (Exception e) { }
                listReminds(session);
                break;
            case "->":
                session.incRemindPage();
                listReminds(session);
                break;
            default:
                sendMessage(session.getChatId(), BotLanguage.InvalidOperation);
                break;

        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton editRemind = new InlineKeyboardButton();
        editRemind.setText("Изменить текст напоминания.");
        editRemind.setCallbackData("");
    }

    private void sendRemindMenu(long remindId, UserSession session) {
        var remind = repositoryComponent.getRemindRepository().getRemindById(remindId);

        if (remind == null) {
            sendMessage(session.getChatId(), "Не найдено такое напоминание).");
            return;
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();

        InlineKeyboardButton editText = new InlineKeyboardButton();
        editText.setText("Изменить текст");
        editText.setCallbackData("ChangeRemindText");

        InlineKeyboardButton deleteRemind = new InlineKeyboardButton();
        deleteRemind.setText("Удалить напоминание");
        deleteRemind.setCallbackData("DeleteRemind");

        buttonRows.add(List.of(editText));
        buttonRows.add(List.of(deleteRemind));

        inlineKeyboardMarkup.setKeyboard(buttonRows);
        sendMessage(session.getChatId(), inlineKeyboardMarkup);
    }

    private long getRemindId(int remindIndexOnPage, UserSession session) {
        int remindPageIndex = session.getRemindPage();
        var userReminds = repositoryComponent
                .getRemindRepository()
                .getRemindsByUser(session.getUser().getUserId());

        return userReminds.get(remindPageIndex * BotConstants.RemindsPerPage + remindIndexOnPage).getId();
    }

    /**
     * Обработать команду, введенную пользователем.
     * @param command - Команда.
     */
    private void handleCommand(String command, UserSession session) {
        String[] inputArr = command.split(" ");

        switch (inputArr[0]) {
            case "/add_remind":
                addRemind(session);
                break;
            case "/help":
                sendHelp(session.getChatId());
                break;
            case "/list_all_reminds":
                session.getUser().setCurrentMenu(UserMenu.RemindsList);
                listReminds(session);
                break;
            case "/start":
                sendWelcomeMessage(session.getChatId());
            default:
                sendInvalidOperationMessage(session.getChatId());
                break;
        }
    }

    private void addRemind(UserSession session) {
        session.getUser().setCurrentMenu(UserMenu.RemindCreateText);
        sendMessage(session.getChatId(), BotLanguage.EnterRemindText);
    }

    private void setRemindText(String text, UserSession session) {
        if (text == null || text.isEmpty()) {
            sendMessage(session.getChatId(), BotLanguage.InvalidRemindText);
        }

        session.setText(text);
        session.getUser().setCurrentMenu(UserMenu.RemindCreateTime);
        sendMessage(session.getChatId(), BotLanguage.EnterRemindTime);
    }

    private void setRemindTime(String text, UserSession session) {
        OffsetDateTime offsetDateTime;
        try {
            var localDT = java.time.LocalDateTime.parse(
                    text,
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );

            offsetDateTime = localDT.atZone(session.getUser().getTimeZoneOffset()).toOffsetDateTime();
        } catch (Exception e) {
            sendMessage(session.getChatId(), BotLanguage.InvalidRemindTime);
            log.warn(e.getMessage());
            return;
        }

        session.setTime(offsetDateTime);
        session.getUser().setCurrentMenu(UserMenu.RemindCreateFrequency);
        sendMessage(session.getChatId(), BotLanguage.EnterRemindFrequency);
    }

    private void setRemindFrequency(String text, UserSession session) {
        Duration frequency;
        try {
            frequency = ParsingHelper.parseDuration(text);
        }
        catch (Exception e) {
            sendMessage(session.getChatId(), BotLanguage.InvalidRemindFrequency + e.getMessage());
            return;
        }

        session.setFrequencyOfRepetition(frequency);

        addConstructedRemind(session);
    }

    private void addConstructedRemind(UserSession session) {
        long newRemindId = repositoryComponent.getRemindRepository().getAll().size();
        repositoryComponent.getRemindRepository().addRemind(
                session.CreateRemind(newRemindId, session.getUser().getUserId()));

        session.getUser().setCurrentMenu(UserMenu.MainPage);
        sendMessage(session.getChatId(), BotLanguage.RemindAdded);
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
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setText("Выберите кнопку:");
        message.setChatId(chatId);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendHelp(Long chatId) {
        sendMessage(chatId, BotLanguage.Help);
    }

    private void listReminds(UserSession session) {
        var reminds = repositoryComponent.getRemindRepository().getRemindsByUser(session.getUser().getUserId());

        if (reminds.isEmpty()) {
            sendMessage(session.getChatId(), "Нет ни одного напоминания.");
            return;
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        for (int i = BotConstants.RemindsPerPage * session.getRemindPage(); i < reminds.size(); i++) {
            InlineKeyboardButton remindButton = new InlineKeyboardButton();
            remindButton.setText(reminds.get(i).getText());
            remindButton.setCallbackData(Integer.toString(i % BotConstants.RemindsPerPage));

            buttonRows.add(List.of(remindButton));

            if (i > 0 && (i + 1) % BotConstants.RemindsPerPage == 0) {
                break;
            }
        }

        List<InlineKeyboardButton> pageSelector = getPageSelectorButtons(session, reminds.size());
        if (!pageSelector.isEmpty()) {
            buttonRows.add(pageSelector);
        }

        if (buttonRows.isEmpty()) {
            session.decRemindPage();
            listReminds(session);
            return;
        }

        inlineKeyboardMarkup.setKeyboard(buttonRows);
        sendMessage(session.getChatId(), inlineKeyboardMarkup);
    }

    private List<InlineKeyboardButton> getPageSelectorButtons(UserSession session, int remindsCount) {
        List<InlineKeyboardButton> pageSelector = new ArrayList<>();

        InlineKeyboardButton prevButton = null;
        InlineKeyboardButton nextButton = null;
        if (remindsCount > (session.getRemindPage() + 1) * BotConstants.RemindsPerPage) {
            nextButton = new InlineKeyboardButton();
            nextButton.setText("->");
            nextButton.setCallbackData("->");
        }

        if (session.getRemindPage() > 0) {
            prevButton = new InlineKeyboardButton();
            prevButton.setText("<-");
            prevButton.setCallbackData("<-");
        }

        if (prevButton != null) {
            pageSelector.add(prevButton);
        }
        if (nextButton != null) {
            pageSelector.add(nextButton);
        }
        return pageSelector;
    }

    private void editTextRemind(String text, UserSession session) {
        try {
            serviceComponent.getRemindService().changeRemindText(session.getRemindId(), text);
            sendMessage(session.getChatId(), "Текст напоминания обновлён.");
        } catch (Exception e) {
            sendMessage(session.getChatId(), "Не удалось изменить текст напоминания. Попробуйте позже.");
        }
    }

    private void deleteRemind(UserSession session) {
        Long remindId = session.getRemindId();

        try {
            serviceComponent.getRemindService().deleteRemind(remindId);
        } catch (IllegalArgumentException e) {
            sendMessage(session.getChatId(), e.getMessage());
            return;
        }

        sendMessage(session.getChatId(), BotLanguage.RemindDeleted);
    }
    
    private void sendWelcomeMessage(long chatId) {
        sendMessage(chatId, BotLanguage.Welcome);
        sendMessage(chatId, BotLanguage.Help);
    }

    private void handleNotification(long chatId, Remind remind) {
        sendMessage(chatId, String.format("Напоминание сработало: %s", remind.getText()));
    }

    private void startNotificationListener() {
        notificationListenerExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Remind remind = NotificationBuffer.take(); // <-- статик
                    handleNotification(
                            repositoryComponent
                                    .getUserSessionRepository()
                                    .getChatIdByUserId(remind.getUserId()),
                            remind);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("Notification listener error", e);
                }
            }
        });
    }

    @Override
    public String getBotUsername() {
        return ConfigHelper.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return ConfigHelper.getBotToken();
    }
}
