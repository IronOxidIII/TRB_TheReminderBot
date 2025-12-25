package org.thereminderbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.User;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.repository.UserRepository;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class MenuServiceTest {

    private UserRepository userRepository;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        menuService = new MenuService(userRepository);
    }

    @Test
    void turnOnMenu_shouldUpdateCurrentMenuId() {
        User user = new User(1L, "TestUser", ZoneId.of("UTC"), UserMenu.MainPage.ordinal());
        userRepository.addUser(user);

        menuService.turnOnMenu(1L, UserMenu.Settings);

        assertEquals(UserMenu.Settings.ordinal(), user.getCurrentMenuId());
    }

    @Test
    void turnOnMenu_shouldThrowExceptionIfUserNotFound() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> menuService.turnOnMenu(99L, UserMenu.Help)
        );

        assertTrue(exception.getMessage().contains("not found"));
    }
}
