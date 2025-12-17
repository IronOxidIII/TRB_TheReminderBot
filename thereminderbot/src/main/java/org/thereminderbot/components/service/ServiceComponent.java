package org.thereminderbot.components.service;

import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.repository.TagRepository;
import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.service.RemindService;
import org.thereminderbot.service.UserService;
import org.thereminderbot.service.TagService;

/**
 * Компонент сервисов.
 */
public class ServiceComponent {
    private final UserService userService;
    private final RemindService remindService;
    private final TagService tagService;

    /**
     * Конструктор по умолчанию.
     */
    public ServiceComponent(RepositoryComponent repositoryComponent) {

        userService = new UserService(
                repositoryComponent.getUserRepository(),
                repositoryComponent.getRemindRepository());
        remindService = new RemindService(repositoryComponent.getRemindRepository());
        tagService = new TagService(
                repositoryComponent.getTagRepository(),
                repositoryComponent.getRemindRepository());
    }

    /**
     * Получить сервис пользователей.
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Получить сервис напоминаний.
     */
    public RemindService getRemindService() {
        return remindService;
    }

    /**
     * Получить сервис тегов.
     */
    public TagService getTagService() {
        return tagService;
    }
}
