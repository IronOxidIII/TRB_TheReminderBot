package org.thereminderbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;
import org.thereminderbot.repository.RemindRepositoryInterface;
import org.thereminderbot.repository.UserRepositoryInterface;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepositoryInterface userRepository;
    private RemindRepositoryInterface remindRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryInterface.class);
        remindRepository = mock(RemindRepositoryInterface.class);
        userService = new UserService(userRepository, remindRepository);
    }



    @Test
    void notifyUser_shouldLogInfoIfUserAndRemindExist() {
        User user = new User(1L, "Abc", ZoneId.of("UTC"), 0);
        Remind remind = new Remind(10L, "Test remind", 1L, OffsetDateTime.now(), Duration.ofHours(1));

        userRepository.addUser(user);
        remindRepository.addRemind(remind);

        assertDoesNotThrow(() -> userService.notifyUser(1L, 10L));

        verify(userRepository, times(1)).getUserById(1L);
        verify(remindRepository, times(1)).getRemindById(10L);
    }



    @Test
    void notifyUser_shouldHandleExceptionIfUserNotFound() {
        doThrow(new IllegalArgumentException("User not found"))
                .when(userRepository).getUserById(99L);

        assertDoesNotThrow(() -> userService.notifyUser(99L, 1L));

        verify(userRepository, times(1)).getUserById(99L);
        verify(remindRepository, never()).getRemindById(anyLong());
    }



    @Test
    void printUsersReminds_shouldPrintRemindsIfExist() {
        Remind remind1 = new Remind(1L, "Text1", 1L, OffsetDateTime.now(), Duration.ofHours(1));
        Remind remind2 = new Remind(2L, "Text2", 1L, OffsetDateTime.now(), Duration.ofHours(2));
        List<Remind> reminds = List.of(remind1, remind2);

        when(remindRepository.getRemindsByUser(1L)).thenReturn(reminds);

        assertDoesNotThrow(() -> userService.printUsersReminds(1L));

        verify(remindRepository, times(1)).getRemindsByUser(1L);
    }


    @Test
    void printUsersReminds_shouldHandleEmptyReminds() {
        when(remindRepository.getRemindsByUser(1L)).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> userService.printUsersReminds(1L));

        verify(remindRepository, times(1)).getRemindsByUser(1L);
    }


    @Test
    void shareReminds_shouldCopyRemindsToAnotherUser() {
        User userFrom = new User(1L, "Abc", ZoneId.of("UTC"), 0);
        User userTo = new User(2L, "Bca", ZoneId.of("UTC"), 0);
        Remind remind = new Remind(10L, "Remind to share", 1L, OffsetDateTime.now(), Duration.ofHours(1));
        List<Remind> fromReminds = List.of(remind);

        when(userRepository.getUserById(1L)).thenReturn(userFrom);
        when(userRepository.getUserById(2L)).thenReturn(userTo);
        when(remindRepository.getRemindsByUser(1L)).thenReturn(fromReminds);

        assertDoesNotThrow(() -> userService.shareReminds(1L, 2L));

        verify(remindRepository, times(1)).getRemindsByUser(1L);
        verify(remindRepository, times(1)).addRemind(argThat(r ->
                r.getText().equals(remind.getText()) &&
                        r.getUserId() == 2L &&
                        r.getTime().equals(remind.getTime()) &&
                        r.getFrequencyOfRepetition().equals(remind.getFrequencyOfRepetition()) &&
                        r.getStatus() == remind.getStatus()
        ));
    }

    @Test
    void shareReminds_shouldHandleNoReminds() {
        User userFrom = new User(1L, "UserFrom", ZoneId.of("UTC"), 0);
        User userTo = new User(2L, "UserTo", ZoneId.of("UTC"), 0);

        when(userRepository.getUserById(1L)).thenReturn(userFrom);
        when(userRepository.getUserById(2L)).thenReturn(userTo);

        when(remindRepository.getRemindsByUser(1L)).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> userService.shareReminds(1L, 2L));

        verify(userRepository, times(1)).getUserById(1L);
        verify(userRepository, times(1)).getUserById(2L);
        verify(remindRepository, times(1)).getRemindsByUser(1L);
        verify(remindRepository, never()).addRemind(any());
    }


    @Test
    void printUserInfo_shouldCallRepository() {
        User user = new User(1L, "Abc", ZoneId.of("UTC"), 0);
        when(userRepository.getUserById(1L)).thenReturn(user);

        assertDoesNotThrow(() -> userService.printUserInfo(1L));

        verify(userRepository, times(1)).getUserById(1L);
    }


    @Test
    void deleteUserAndReminds_shouldCallRepositories() {
        doNothing().when(remindRepository).deleteRemindsByUserId(1L);
        doNothing().when(userRepository).deleteUser(1L);

        assertDoesNotThrow(() -> userService.deleteUserAndReminds(1L));

        verify(remindRepository, times(1)).deleteRemindsByUserId(1L);
        verify(userRepository, times(1)).deleteUser(1L);
    }


    @Test
    void deleteUserAndReminds_shouldThrowExceptionIfInvalidId() {
        IllegalArgumentException exZero = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUserAndReminds(0)
        );
        assertEquals("Invalid user ID", exZero.getMessage());

        IllegalArgumentException exNegative = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUserAndReminds(-5)
        );
        assertEquals("Invalid user ID", exNegative.getMessage());

        verify(userRepository, never()).deleteUser(anyLong());
        verify(remindRepository, never()).deleteRemindsByUserId(anyLong());
    }
}
