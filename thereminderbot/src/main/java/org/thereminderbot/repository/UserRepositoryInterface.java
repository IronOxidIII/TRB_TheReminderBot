// UserRepositoryInterface.java
package org.thereminderbot.repository;

import org.thereminderbot.domain.User;

import java.util.List;

public interface UserRepositoryInterface {
    User getUserById(long id);
    void addUser(User user);
    void deleteUser(long id);
    List<User> getAllUsers();
}
