package org.thereminderbot.components.repository;

import org.thereminderbot.domain.UserSession;
import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.repository.TagRepository;
import org.thereminderbot.repository.UserSessionRepository;

/**
 * Компонент репозиториев.
 */
public class RepositoryComponent {
    private final RemindRepository remindRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final UserSessionRepository userSessionRepository;

    /**
     * Конструктор по умолчанию.
     */
    public RepositoryComponent() {
        remindRepository = new RemindRepository();
        userRepository = new UserRepository();
        tagRepository = new TagRepository();
        userSessionRepository = new UserSessionRepository();
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

    /**
     * Получить репозиторий тегов.
     */
    public TagRepository getTagRepository() {
        return tagRepository;
    }

    public UserSessionRepository getUserSessionRepository() {
        return userSessionRepository;
    }
}
