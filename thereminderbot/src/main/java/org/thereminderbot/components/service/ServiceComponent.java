package org.thereminderbot.components.service;

import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.service.RemindService;
import org.thereminderbot.service.UserService;

/**
 * Компонент сервисов.
 */
public class ServiceComponent {
    private final UserService userService;
    private final RemindService remindService;

    /**
     * Конструктор по умолчанию.
     */
    public ServiceComponent() {
        RemindRepository remindRepository = new RemindRepository();
        UserRepository userRepository = new UserRepository();

        userService = new UserService(userRepository, remindRepository);
        remindService = new RemindService(remindRepository);
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
}
