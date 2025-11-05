package org.thereminderbot.components.repository;

import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.UserRepository;

/**
 * Компонент репозиториев.
 */
public class RepositoryComponent {
    private final RemindRepository remindRepository;
    private final UserRepository userRepository;

    /**
     * Конструктор по умолчанию.
     */
    public RepositoryComponent() {
        remindRepository = new RemindRepository();
        userRepository = new UserRepository();
    }

    /**
     * Получить репозиторий напоминаний.
     */
    public RemindRepository getRemindRepository() {
        return remindRepository;
    }

    /**
     * Получить репозиторий пользователей.
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }
}
