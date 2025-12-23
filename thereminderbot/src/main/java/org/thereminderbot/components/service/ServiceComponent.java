package org.thereminderbot.components.service;

import org.thereminderbot.repository.RemindRepository;
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
        userService = new UserService();
        remindService = new RemindService(new RemindRepository());
    }

    public UserService getUserService() {
        return userService;
    }

    public RemindService getRemindService() {
        return remindService;
    }
}
