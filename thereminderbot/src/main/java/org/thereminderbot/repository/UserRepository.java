package org.thereminderbot.repository;

import org.thereminderbot.domain.User;
import org.thereminderbot.domain.Remind;

import java.util.ArrayList;

/**
 * Репозиторий пользователя.
 */
public class UserRepository {
    private ArrayList<User> users;

    private RemindRepository remindRepository;

    public UserRepository(RemindRepository remindRepository) {
        // Инициализация коллекции пользователей
        this.users = new ArrayList<>();
        this.remindRepository = remindRepository;
    }

    public User getUserById(long id) {
        for (User user : users) {
            if (user.getUserId() == id) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        users.add(user);
        return;
    }

    public void deleteUser(long id) {
        User user = getUserById(id);
        if (user != null) {
            // Удаляем все напоминания пользователя одним вызовом
            int removedCount = remindRepository.deleteRemindsByUserId(user.getUserId());
            System.out.println("Удалено " + removedCount + " напоминаний пользователя " + user.getUserName());
            users.remove(user);
        }
    }

}
