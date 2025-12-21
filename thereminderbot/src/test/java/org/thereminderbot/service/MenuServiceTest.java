package org.thereminderbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.User;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.repository.UserRepository;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private UserRepository userRepository;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        menuService = new MenuService(userRepository);
    }

    @Test
    void turnOnMenu_shouldUpdateCurrentMenuId() {
        User user = new User(1L, "TestUser", ZoneId.of("UTC"), UserMenu.MainPage.ordinal());
        when(userRepository.getUserById(1L)).thenReturn(user);

        menuService.turnOnMenu(1L, UserMenu.Settings);

        assertEquals(UserMenu.Settings.ordinal(), user.getCurrentMenuId());
        verify(userRepository, times(1)).getUserById(1L);
    }

    @Test
    void turnOnMenu_shouldThrowExceptionIfUserNotFound() {
        when(userRepository.getUserById(99L)).thenThrow(new IllegalArgumentException("User not found"));

        assertThrows(IllegalArgumentException.class, () -> menuService.turnOnMenu(99L, UserMenu.Help));
        verify(userRepository, times(1)).getUserById(99L);
    }
}
