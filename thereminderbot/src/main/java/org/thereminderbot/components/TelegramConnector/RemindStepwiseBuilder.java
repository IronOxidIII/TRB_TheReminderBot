package org.thereminderbot.components.TelegramConnector;

import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;

import java.time.Duration;
import java.time.OffsetDateTime;

public class RemindStepwiseBuilder {
    private String text;

    private long userId;

    private OffsetDateTime time;

    private Duration frequencyOfRepetition;

    public void setText(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        this.text = text;
    }

    public void setTime(OffsetDateTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
        this.time = time;
    }

    public void setFrequencyOfRepetition(Duration frequencyOfRepetition) {
        if (frequencyOfRepetition == null) {
            throw new IllegalArgumentException("Frequency of repetition cannot be null");
        }
        this.frequencyOfRepetition = frequencyOfRepetition;
    }

    public Remind CreateRemind(long newRemindId, long userId) {
        if ((newRemindId < 0) ||
            (text == null) || text.isEmpty() ||
            (userId < 0) ||
            (time == null) ||
            (frequencyOfRepetition == null)) {
            throw new IllegalArgumentException("All parameters must be set");
        }
        return new Remind(newRemindId, text, userId, time, frequencyOfRepetition);
    }

    public String getText() {
        return text;
    }

    public void clear() {
        text = "";
        userId = 0;
        time = null;
        frequencyOfRepetition = null;
    }
}
