package org.thereminderbot.components.repository;

import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.repository.TagRepository;

/**
 * Компонент репозиториев.
 */
public class RepositoryComponent {
    private final RemindRepository remindRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    /**
     * Конструктор по умолчанию.
     */
    public RepositoryComponent() {
        remindRepository = new RemindRepository();
        userRepository = new UserRepository();
        tagRepository = new TagRepository();
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
}
