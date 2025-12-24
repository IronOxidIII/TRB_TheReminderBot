package org.thereminderbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;
import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.UserRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserRepository userRepository;
    private RemindRepository remindRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        remindRepository = new RemindRepository();
        userService = new UserService(userRepository, remindRepository);
    }

    @Test
    void notifyUser_shouldLogInfoIfUserAndRemindExist() {
        User user = new User(1L, "Abc", ZoneId.of("UTC"), 0);
        Remind remind = new Remind(10L, "Test remind", 1L, OffsetDateTime.now(), Duration.ofHours(1));
        userRepository.addUser(user);
        remindRepository.addRemind(remind);

        assertDoesNotThrow(() -> userService.notifyUser(1L, 10L));
    }

    @Test
    void notifyUser_shouldHandleMissingUserOrRemind() {
        assertDoesNotThrow(() -> userService.notifyUser(99L, 1L));
        assertDoesNotThrow(() -> userService.notifyUser(1L, 99L));
    }

    @Test
    void printUsersReminds_shouldHandleExistingReminds() {
        Remind r1 = new Remind(1L, "Text1", 1L, OffsetDateTime.now(), Duration.ofHours(1));
        Remind r2 = new Remind(2L, "Text2", 1L, OffsetDateTime.now(), Duration.ofHours(2));
        remindRepository.addRemind(r1);
        remindRepository.addRemind(r2);

        assertDoesNotThrow(() -> userService.printUsersReminds(1L));
    }

    @Test
    void printUsersReminds_shouldHandleNoReminds() {
        userRepository.addUser(new User(1L, "User", ZoneId.of("UTC"), 0));
        assertDoesNotThrow(() -> userService.printUsersReminds(1L));
    }

    @Test
    void shareReminds_shouldCopyRemindsToAnotherUser() {
        User from = new User(1L, "UserFrom", ZoneId.of("UTC"), 0);
        User to = new User(2L, "UserTo", ZoneId.of("UTC"), 0);
        userRepository.addUser(from);
        userRepository.addUser(to);

        Remind remind = new Remind(1L, "Remind1", 1L, OffsetDateTime.now(), Duration.ofHours(1));
        remindRepository.addRemind(remind);

        assertDoesNotThrow(() -> userService.shareReminds(1L, 2L));

        List<Remind> toReminds = remindRepository.getRemindsByUser(2L);
        assertEquals(1, toReminds.size());
        Remind copied = toReminds.get(0);
        assertEquals(remind.getText(), copied.getText());
        assertEquals(2L, copied.getUserId());
        assertEquals(remind.getTime(), copied.getTime());
        assertEquals(remind.getFrequencyOfRepetition(), copied.getFrequencyOfRepetition());
        assertEquals(remind.getStatus(), copied.getStatus());
    }

    @Test
    void shareReminds_shouldHandleNoReminds() {
        User from = new User(1L, "UserFrom", ZoneId.of("UTC"), 0);
        User to = new User(2L, "UserTo", ZoneId.of("UTC"), 0);
        userRepository.addUser(from);
        userRepository.addUser(to);

        assertDoesNotThrow(() -> userService.shareReminds(1L, 2L));
        assertTrue(remindRepository.getRemindsByUser(2L).isEmpty());
    }

    @Test
    void printUserInfo_shouldNotThrow() {
        User user = new User(1L, "Abc", ZoneId.of("UTC"), 0);
        userRepository.addUser(user);

        assertDoesNotThrow(() -> userService.printUserInfo(1L));
    }

    @Test
    void deleteUserAndReminds_shouldRemoveUserAndTheirReminds() {
        User user = new User(1L, "Abc", ZoneId.of("UTC"), 0);
        userRepository.addUser(user);

        Remind r1 = new Remind(1L, "R1", 1L, OffsetDateTime.now(), Duration.ofHours(1));
        Remind r2 = new Remind(2L, "R2", 1L, OffsetDateTime.now(), Duration.ofHours(2));
        remindRepository.addRemind(r1);
        remindRepository.addRemind(r2);

        assertDoesNotThrow(() -> userService.deleteUserAndReminds(1L));

        assertThrows(IllegalArgumentException.class, () -> userRepository.getUserById(1L));
        assertTrue(remindRepository.getRemindsByUser(1L).isEmpty());
    }

    @Test
    void deleteUserAndReminds_shouldThrowExceptionForInvalidId() {
        IllegalArgumentException exZero = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUserAndReminds(0)
        );
        IllegalArgumentException exNegative = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUserAndReminds(-5)
        );
    }
}
