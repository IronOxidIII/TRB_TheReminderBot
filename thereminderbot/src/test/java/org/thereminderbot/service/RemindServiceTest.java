package org.thereminderbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.enums.RemindStatus;
import org.thereminderbot.repository.RemindRepository;

import java.time.Duration;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RemindServiceTest {

    private RemindRepository remindRepository;
    private RemindService remindService;

    @BeforeEach
    void setUp() {
        remindRepository = new RemindRepository();
        remindService = new RemindService(remindRepository);
    }

    @Test
    void switchOffRemind_shouldDeactivateExistingRemind() {
        Remind remind = new Remind(1L, "Test", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        remindRepository.addRemind(remind);

        remindService.switchOffRemind(1L);

        assertEquals(RemindStatus.Deactivated, remind.getStatus());
    }

    @Test
    void switchOffRemind_shouldLogWarningIfRemindNotFound() {
        assertDoesNotThrow(() -> remindService.switchOffRemind(99L));
    }

    @Test
    void changeRemindText_shouldUpdateTextIfRemindExists() {
        Remind remind = new Remind(1L, "Old text", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        remindRepository.addRemind(remind);

        remindService.changeRemindText(1L, "New text");

        assertEquals("New text", remind.getText());
    }

    @Test
    void changeRemindText_shouldThrowExceptionIfRemindNotFound() {
        assertThrows(IllegalArgumentException.class, () -> remindService.changeRemindText(99L, "Text"));
    }

    @Test
    void deleteRemind_shouldRemoveRemindIfExists() {
        Remind remind = new Remind(1L, "Test", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        remindRepository.addRemind(remind);

        remindService.deleteRemind(1L);

        assertTrue(remindRepository.getAll().isEmpty());
    }

    @Test
    void deleteRemind_shouldHandleExceptionIfRemindNotFound() {
        assertDoesNotThrow(() -> remindService.deleteRemind(99L));
    }
}
