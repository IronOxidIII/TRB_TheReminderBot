package org.thereminderbot.service;

import org.thereminderbot.domain.User;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.repository.UserRepository;

public class UserService {
    private UserRepository userRepository;

    public void notifyUser(long userId, long remindId) {
        //Вывести в консоль заметку.
        //TODO
        return;
    }

    public void printUsersReminds(long userId) {
        //TODO
        return;
    }

    public void shareReminds(long userIdFrom, long userIdTo) {
        //TODO
        return;
    }

    public void turnOnMenu(long userId, UserMenu userMenu) {

    }

    public void printUserInfo(long userId) {
        //TODO
        return;
    }
}
