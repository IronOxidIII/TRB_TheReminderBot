package org.thereminderbot.domain;

import org.junit.jupiter.api.Test;
import org.thereminderbot.enums.RemindStatus;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class RemindTest {

    @Test
    void constructor_shouldCreateRemindWithCorrectFields() {
        long id = 1L;
        String text = "Test remind";
        long userId = 10L;
        OffsetDateTime time = OffsetDateTime.now(ZoneOffset.UTC);
        Duration frequency = Duration.ofHours(2);

        Remind remind = new Remind(id, text, userId, time, frequency);

        assertEquals(id, remind.getId());
        assertEquals(text, remind.getText());
        assertEquals(userId, remind.getUserId());
        assertEquals(time, remind.getTime());
        assertEquals(frequency, remind.getFrequencyOfRepetition());
    }

    @Test
    void constructor_shouldSetStatusActiveByDefault() {
        Remind remind = new Remind(
                1L,
                "Default status test",
                1L,
                OffsetDateTime.now(),
                Duration.ofMinutes(30)
        );

        assertEquals(RemindStatus.Active, remind.getStatus());
    }

    @Test
    void setters_shouldUpdateFieldsCorrectly() {
        Remind remind = new Remind(
                1L,
                "Old text",
                1L,
                OffsetDateTime.now(),
                Duration.ofMinutes(10)
        );

        OffsetDateTime newTime = OffsetDateTime.now().plusDays(1);
        Duration newFrequency = Duration.ofHours(5);

        remind.setId(2L);
        remind.setText("New text");
        remind.setUserId(2L);
        remind.setTime(newTime);
        remind.setFrequencyOfRepetition(newFrequency);
        remind.setStatus(RemindStatus.Deactivated);

        assertEquals(2L, remind.getId());
        assertEquals("New text", remind.getText());
        assertEquals(2L, remind.getUserId());
        assertEquals(newTime, remind.getTime());
        assertEquals(newFrequency, remind.getFrequencyOfRepetition());
        assertEquals(RemindStatus.Deactivated, remind.getStatus());
    }

    @Test
    void copyConstructor_shouldCopyAllFieldsExceptUserId() {
        Remind original = new Remind(
                5L,
                "Text",
                1L,
                OffsetDateTime.now(),
                Duration.ofHours(1)
        );
        original.setStatus(RemindStatus.Deactivated);

        long newUserId = 99L;
        Remind copied = new Remind(original, newUserId);

        assertEquals(original.getId(), copied.getId());
        assertEquals(original.getText(), copied.getText());
        assertEquals(newUserId, copied.getUserId());
        assertEquals(original.getTime(), copied.getTime());
        assertEquals(original.getFrequencyOfRepetition(), copied.getFrequencyOfRepetition());
        assertEquals(original.getStatus(), copied.getStatus());
    }
}
