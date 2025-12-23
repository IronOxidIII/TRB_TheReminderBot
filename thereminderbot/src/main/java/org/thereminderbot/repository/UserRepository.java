package org.thereminderbot.repository;

import org.thereminderbot.domain.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Репозиторий пользователя.
 */
public class UserRepository {
    private ArrayList<User> users;

    public UserRepository() {
        //TODO
        return;
    }

    public User getUserById(long id) {
        //TODO
        return null;
    }

    public void addUser(User user) {
        //TODO
        return;
    }

    public void deleteUser(long id) {
        //TODO
        // Прошу обратить внимание, при удалении пользователя нам нужно удалить все его заметки.
        return;
    }
}
