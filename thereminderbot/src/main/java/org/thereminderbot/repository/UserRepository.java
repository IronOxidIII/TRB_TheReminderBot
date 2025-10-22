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

    /**
     * Получить пользователя по id
     */

    public User getUserById(long id) {
        for (User user : users) {
            if (user.getUserId() == id) {
                return user;
            }
        }
        throw new IllegalArgumentException("Пользователь с id " + id + " не найден.");
    }

    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Удалить пользователя по id
     */

    public void deleteUser(long id) {
        Var user = getUserById(id);

        ArrayList<Remind> userReminds = remindRepository.getRemindsByUser(user);

        System.out.println("Удаляем напоминания пользователя " + user.getUserName() + ":");

        for (Remind remind : userReminds) {
            boolean removed = remindRepository.deleteRemindById(remind.getId());
            if (removed) {
                System.out.println("Удалено напоминание ID " + remind.getId() + ": " + remind.getText());
            } else {
                System.out.println("Не удалось удалить напоминание ID " + remind.getId());
            }
        }

        users.remove(user);
        System.out.println("Пользователь " + user.getUserName() + " удалён.");
    }


}
