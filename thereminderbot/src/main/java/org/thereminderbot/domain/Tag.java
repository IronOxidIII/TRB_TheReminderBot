package org.thereminderbot.domain;

/**
 * Domain class for Tags for Remind.
 * @see Remind
 */
public class Tag {
    /**
     * Строка названия.
     */
    private String name;

    /**
     * Число идентификатор.
     */
    private long id;

    /**
     * Число идентификатор заметки, к которой относится тэг.
     */
    private long remindId;

    public Tag(long id, String name, long remindId) {
        this.id = id;
        this.name = name;
        this.remindId = remindId;
    }

    /**
     * Получить имя тэга.
     * @return Строка с именем тэга.
     */
    public String getName() {
        return new String(name);
    }

    /**
     * Назначить имя тэга.
     * @param name Строка - новое имя тэга.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить id тэга.
     * @return Число - id тэга.
     */
    public long getId() {
        return id;
    }

    /**
     * Назначить id тэга.
     * @param id Число - новое id тэга.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Получить id заметки к которой относится тэг.
     * @return Число - id заметки.
     */
    public long getRemindId() {
        return remindId;
    }

    /**
     * Назначить id заметки, к которой будет относиться тэг.
     * @param remindId Число - id заметки, к которой будет относиться тэг.
     */
    public void setRemindId(long remindId) {
        this.remindId = remindId;
    }
}
