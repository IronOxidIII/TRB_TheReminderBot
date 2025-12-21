package org.thereminderbot.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.User;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepositoryInterface userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    void addUser_shouldAddUserSuccessfully() {
        User user = new User(1L, "Abc", ZoneId.of("UTC"), 0);
        userRepository.addUser(user);

        User returned = userRepository.getUserById(1L);
        assertEquals(user, returned);
    }

    @Test
    void getUserById_shouldThrowExceptionIfUserNotFound() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.getUserById(99L)
        );

        assertTrue(exception.getMessage().contains("not found."));
    }

    @Test
    void deleteUser_shouldRemoveUserSuccessfully() {
        User user = new User(1L, "Abc", ZoneId.of("UTC"), 0);
        userRepository.addUser(user);

        userRepository.deleteUser(1L);

        assertThrows(IllegalArgumentException.class, () -> userRepository.getUserById(1L));
    }

    @Test
    void deleteUser_shouldThrowExceptionIfUserNotFound() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> userRepository.deleteUser(99L)
        );

        assertTrue(exception.getMessage().contains("not found"));
    }
}
