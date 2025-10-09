package org.thereminderbot.repository;

import org.thereminderbot.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final List<User> users = new ArrayList<User>();

    public void loginUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User не может быть null");
        }

        if (users.contains(user)) {
            throw new IllegalArgumentException("Такой пользователь уже есть в коллекции.");
        }

        users.add(user);
    }

    public User findUserByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name не должно быть null");
        }

        for (var user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }

        return null;
    }
}
