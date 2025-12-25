package org.thereminderbot.repository;

import org.thereminderbot.domain.UserSession;

import java.util.concurrent.ConcurrentHashMap;

public class UserSessionRepository {
    private final ConcurrentHashMap<Long, UserSession> userSessions;

    public UserSessionRepository() {
        this.userSessions = new ConcurrentHashMap<>();
    }

    /**
     * Вернуть UserSession по ChatId.
     * @param chatId - Id чата
     * @return UserSession, если пользовтель есть. Иначе выбрасывает исключение.
     */
    public UserSession getUserSession(long chatId) {
        UserSession foundUserSession = userSessions.get(chatId);
        if (foundUserSession == null) {
            throw new IllegalArgumentException(String.format("No UserSession found for chat id %d", chatId));
        }
        return foundUserSession;
    }

    public void addUserSession(long chatId, UserSession userSession) {
        userSessions.put(chatId, userSession);
    }

    public long getChatIdByUserId(long userId) {
        var values = userSessions.values();
        for (var userSession : values) {
            if (userSession.getUser().getUserId() == userId) {
                return userSession.getChatId();
            }
        }
        return 0;
    }
}
