package org.thereminderbot.repository;

import org.thereminderbot.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий пользователя
 */
public class UserRepositoryImpl implements UserRepositoryInterface {
    private final ArrayList<User> users;

    public UserRepositoryImpl() {
        this.users = new ArrayList<>();
    }

    /**
     * Получить пользователя по id
     */
    @Override
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
    @Override
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Удалить пользователя по id
     */
    @Override
    public void deleteUser(long id) {
        User user = getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException(String.format("User with id %d not found.", id));
        }
        users.remove(user);
    }

    /**
     * Получить всех пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
