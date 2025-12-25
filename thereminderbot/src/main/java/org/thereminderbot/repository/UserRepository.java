package org.thereminderbot.repository;

import org.thereminderbot.domain.User;
import org.thereminderbot.domain.Remind;

import java.util.ArrayList;

/**
 * Репозиторий пользователя
 */
public class UserRepository {
    private ArrayList<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    /**
     * Получить пользователя по id
     */
    public User getUserById(long id) {
        for (User user : users) {
            if (user.getUserId() == id) {
                return user;
            }
        }
        throw new IllegalArgumentException(String.format("User with id %d not found.", id));
    }

    /**
     * Добавить пользователя
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Удалить пользователя по id
     */
    public void deleteUser(long id) {
        User user = getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException(String.format("User with id %d not found.", id));
        }
        users.remove(user);
    }
}