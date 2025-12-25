package org.thereminderbot.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.Remind;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RemindRepositoryTest {

    private RemindRepository repository;

    @BeforeEach
    void setUp() {
        repository = new RemindRepository();
    }

    @Test
    void addRemind_shouldAddRemindSuccessfully() {
        Remind remind = new Remind(1L, "Test", 10L,
                OffsetDateTime.now(ZoneOffset.UTC), Duration.ofHours(1));
        repository.addRemind(remind);

        List<Remind> all = repository.getAll();
        assertEquals(1, all.size());
        assertEquals(remind, all.get(0));
    }

    @Test
    void addRemind_shouldThrowExceptionIfNull() {
        assertThrows(IllegalArgumentException.class, () -> repository.addRemind(null));
    }

    @Test
    void getRemindById_shouldReturnCorrectRemind() {
        Remind remind1 = new Remind(1L, "A", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        Remind remind2 = new Remind(2L, "B", 20L, OffsetDateTime.now(), Duration.ofHours(2));
        repository.addRemind(remind1);
        repository.addRemind(remind2);

        Remind result = repository.getRemindById(2L);
        assertEquals(remind2, result);

        Remind notFound = repository.getRemindById(99L);
        assertNull(notFound);
    }

    @Test
    void getRemindsByUser_shouldReturnOnlyUserReminds() {
        Remind r1 = new Remind(1L, "A", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        Remind r2 = new Remind(2L, "B", 20L, OffsetDateTime.now(), Duration.ofHours(2));
        Remind r3 = new Remind(3L, "C", 10L, OffsetDateTime.now(), Duration.ofHours(3));

        repository.addRemind(r1);
        repository.addRemind(r2);
        repository.addRemind(r3);

        List<Remind> user10 = repository.getRemindsByUser(10L);
        assertEquals(2, user10.size());
        assertTrue(user10.contains(r1));
        assertTrue(user10.contains(r3));

        List<Remind> user99 = repository.getRemindsByUser(99L);
        assertTrue(user99.isEmpty());
    }

    @Test
    void deleteRemindById_shouldRemoveRemind() {
        Remind r = new Remind(1L, "A", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        repository.addRemind(r);

        repository.deleteRemindById(1L);
        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    void deleteRemindById_shouldThrowExceptionIfNotFound() {
        assertThrows(IllegalArgumentException.class, () -> repository.deleteRemindById(99L));
    }

    @Test
    void deleteRemindsByUserId_shouldRemoveOnlyUserReminds() {
        Remind r1 = new Remind(1L, "A", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        Remind r2 = new Remind(2L, "B", 20L, OffsetDateTime.now(), Duration.ofHours(2));
        Remind r3 = new Remind(3L, "C", 10L, OffsetDateTime.now(), Duration.ofHours(3));

        repository.addRemind(r1);
        repository.addRemind(r2);
        repository.addRemind(r3);

        repository.deleteRemindsByUserId(10L);

        List<Remind> all = repository.getAll();
        assertEquals(1, all.size());
        assertEquals(r2, all.get(0));
    }

    @Test
    void deleteRemindsByUserId_shouldThrowExceptionIfNoReminds() {
        assertThrows(IllegalArgumentException.class, () -> repository.deleteRemindsByUserId(99L));
    }
}
