package org.thereminderbot.components.service;

import org.thereminderbot.components.repository.RepositoryComponent;
import org.thereminderbot.service.RemindService;
import org.thereminderbot.service.UserService;

public class ServiceComponent {
    private final UserService userService;
    private final RemindService remindService;

    public ServiceComponent(RepositoryComponent repositoryComponent) {
        this.userService = new UserService(
                repositoryComponent.getUserRepository(),
                repositoryComponent.getRemindRepository()
        );
        this.remindService = new RemindService(
                repositoryComponent.getRemindRepository()
        );
    }

    public UserService getUserService() {
        return userService;
    }

    public RemindService getRemindService() {
        return remindService;
    }
}