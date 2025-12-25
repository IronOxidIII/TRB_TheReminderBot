package org.thereminderbot.domain;

import org.thereminderbot.components.TelegramConnector.RemindStepwiseBuilder;

public class UserSession extends RemindStepwiseBuilder {
    private User user;

    private long chatId;

    private int remindPage;

    private long remindId;

    public void setRemindId(long remindId) {
        this.remindId = remindId;
    }

    public long getRemindId() {
        return remindId;
    }

    public UserSession(User user, long chatId) {
        this.user = user;
        this.chatId = chatId;
        remindPage = 0;
    }

    public User getUser() {
        return user;
    }

    public long getChatId() {
        return chatId;
    }

    public int getRemindPage() {
        return remindPage;
    }

    public void incRemindPage() {
        remindPage++;
    }

    public void decRemindPage() {
        if (remindPage == 0) {
            throw new IllegalStateException("No remind page available");
        }
        remindPage--;
    }

    @Override
    public void clear() {
        super.clear();
        remindPage = 0;
        remindId = 0;
    }
}
