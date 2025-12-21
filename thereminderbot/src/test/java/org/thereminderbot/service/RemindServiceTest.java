package org.thereminderbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.enums.RemindStatus;
import org.thereminderbot.repository.RemindRepositoryInterface;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RemindServiceTest {

    private RemindRepositoryInterface remindRepository;
    private RemindService remindService;

    @BeforeEach
    void setUp() {
        remindRepository = mock(RemindRepositoryInterface.class);
        remindService = new RemindService(remindRepository);
    }

    @Test
    void switchOffRemind_shouldDeactivateExistingRemind() {
        Remind remind = new Remind(1L, "Test", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        List<Remind> reminds = new ArrayList<>();
        reminds.add(remind);

        when(remindRepository.getAll()).thenReturn(reminds);

        remindService.switchOffRemind(1L);

        assertEquals(RemindStatus.Deactivated, remind.getStatus());
        verify(remindRepository, times(1)).getAll();
    }

    @Test
    void switchOffRemind_shouldLogWarningIfRemindNotFound() {
        List<Remind> reminds = new ArrayList<>();
        when(remindRepository.getAll()).thenReturn(reminds);

        remindService.switchOffRemind(99L);

        assertTrue(reminds.isEmpty());
        verify(remindRepository, times(1)).getAll();
    }

    @Test
    void changeRemindText_shouldUpdateTextIfRemindExists() {
        Remind remind = new Remind(1L, "Old text", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        when(remindRepository.getRemindById(1L)).thenReturn(remind);

        remindService.changeRemindText(1L, "New text");

        assertEquals("New text", remind.getText());
        verify(remindRepository, times(1)).getRemindById(1L);
    }

    @Test
    void changeRemindText_shouldThrowExceptionIfRemindNotFound() {
        when(remindRepository.getRemindById(99L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> remindService.changeRemindText(99L, "Text"));

        verify(remindRepository, times(1)).getRemindById(99L);
    }

    @Test
    void deleteRemind_shouldCallRepositoryDelete() {
        doNothing().when(remindRepository).deleteRemindById(1L);

        remindService.deleteRemind(1L);

        verify(remindRepository, times(1)).deleteRemindById(1L);
    }

    @Test
    void deleteRemind_shouldHandleExceptionIfRemindNotFound() {
        doThrow(new IllegalArgumentException("Remind not found"))
                .when(remindRepository).deleteRemindById(99L);

        remindService.deleteRemind(99L);

        verify(remindRepository, times(1)).deleteRemindById(99L);
    }
}
