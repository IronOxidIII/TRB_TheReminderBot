package org.thereminderbot.domain;

import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Доменный класс заметки.
 */
public class Remind {
    /**
     * Уникальный идентификатор заметки.
     */
    private long id;

    /**
     * Текст заметки.
     */
    private String text;

    /**
     * Уникальный идентификатор пользователя, которому принадлежит заметка.
     * @see User
     */
    private long userId;

    /**
     * Время, на которое назначено напоминание.
     */
    private OffsetDateTime time;

    /**
     * Частота повторения напоминания.
     */
    private LocalTime frequencyOfRepetition;

    /**
     * Статус заметки по {@link org.thereminderbot.enums.RemindStatus}.
     */
    private int status;

    public Remind(long id, String text, long userId, OffsetDateTime time, LocalTime frequencyOfRepetition) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.time = time;
        this.frequencyOfRepetition = frequencyOfRepetition;
    }

    /**
     * Конструктор копирования с возможностью заменить userId.
     */

    public Remind(Remind remind, long newUserId) {
        this.id = remind.id;
        this.text = remind.text;
        this.userId = newUserId;
        this.time = remind.time;
        this.frequencyOfRepetition = remind.frequencyOfRepetition;
    }

    /**
     * Получить id заметки.
     * @return Число - id заметки.
     */
    public long getId() {
        return id;
    }

    /**
     * Назначить новое id заметки.
     * @param id Число - новое id заметки.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Получить текст заметки.
     * @return Строка - текст заметки.
     */
    public String getText() {
        return new String(text);
    }

    /**
     * Назначить новый текст заметки.
     * @param text Строка - новый текст заметки.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Получить id пользователя, хозяина заметки.
     * @return Число - id пользователя.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Назначить новый id пользователя.
     * @param userId Число - id нового пользователя.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Получить время уведомления о заметке.
     * @return Время - время заметки.
     */
    public OffsetDateTime getTime() {
        return time;
    }

    /**
     * Назначить новое время заметки.
     * @param time Время - новое время заметки.
     */
    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    /**
     * Получить частоту повторения уведомления о заметке.
     * @return Время - частота повторения уведомления о заметке.
     */
    public LocalTime getFrequencyOfRepetition() {
        return frequencyOfRepetition;
    }

    /**
     * Назначить новую частоту повторения уведомления о заметке.
     * @param frequencyOfRepetition Время - новая частота повторения уведомления о заметке.
     */
    public void setFrequencyOfRepetition(LocalTime frequencyOfRepetition) {
        this.frequencyOfRepetition = frequencyOfRepetition;
    }

    /**
     * Получить статус заметки по {@link org.thereminderbot.enums.RemindStatus}.
     * @return Число - id статуса.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Назначить новый статус заметкипо {@link org.thereminderbot.enums.RemindStatus}.
     * @param status Число - id статуса.
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
