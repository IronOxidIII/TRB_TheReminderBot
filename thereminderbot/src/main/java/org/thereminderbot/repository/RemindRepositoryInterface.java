package org.thereminderbot.repository;

import org.thereminderbot.domain.Remind;

import java.util.List;

/**
 * Интерфейс репозитория напоминаний.
 */
public interface RemindRepositoryInterface {

    /**
     * Возвращает копию списка всех напоминаний.
     */
    List<Remind> getAll();

    /**
     * Получить одно напоминание по его идентификатору.
     */
    Remind getRemindById(long remindId);

    /**
     * Получить все напоминания пользователя.
     */
    List<Remind> getRemindsByUser(long userId);

    /**
     * Добавляет новое напоминание.
     */
    void addRemind(Remind remind);

    /**
     * Удаляет напоминание по ID.
     */
    void deleteRemindById(long remindId);

    /**
     * Удаляет все напоминания пользователя по ID.
     */
    void deleteRemindsByUserId(long userId);
}
