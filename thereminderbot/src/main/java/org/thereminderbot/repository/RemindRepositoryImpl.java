package org.thereminderbot.repository;

import org.thereminderbot.domain.Remind;

import java.util.List;
import java.util.ArrayList;

/**
 * Репозиторий заметок.
 */

public class RemindRepositoryImpl implements RemindRepositoryInterface {
    private final List<Remind> reminds;

    public RemindRepositoryImpl() {
        this.reminds = new ArrayList<>();
    }

    /**
     * Возвращает копию списка всех напоминаний, сохранённых в репозитории.
     *
     * @return новый список, содержащий все текущие напоминания.
     */
    @Override
    public List<Remind> getAll() {
        return new ArrayList<>(reminds);
    }

    /**
     * Получить одно напоминание по его идентификатору.
     * @param remindId напоминания.
     * @return объект {Remind}, если найден; иначе {null}.
     */
    @Override
    public Remind getRemindById(long remindId) {
        for (var r : reminds) {
            if (r.getId() == remindId) return r;
        }
        return null;
    }

    /**
     * Получить все напоминания, принадлежащие пользователю с указанным userId.
     */
    @Override
    public List<Remind> getRemindsByUser(long userId) {
        var result = new ArrayList<Remind>();
        for (var r : reminds) {
            if (r.getUserId() == userId) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Добавляет новое напоминание в репозиторий.
     * @param remind напоминание, которое нужно добавить.
     * @throws IllegalArgumentException если remind равен {null}.
     */
    @Override
    public void addRemind(Remind remind) {
        if (remind == null) {
            throw new IllegalArgumentException("Remind cannot be null");
        }
        reminds.add(remind);
    }

    /**
     * Удаляет напоминание по ID.
     * @param remindId напоминания, которое нужно удалить.
     * @throws IllegalArgumentException если напоминание с таким ID не найдено.
     */
    @Override
    public void deleteRemindById(long remindId) {
        boolean removed = reminds.removeIf(r -> r.getId() == remindId);
        if (!removed) {
            throw new IllegalArgumentException("Remind with id " + remindId + " not found");
        }
    }

    /**
     * Удаляет все напоминания, принадлежащие пользователю с указанным ID.
     * @param userId пользователя, чьи напоминания нужно удалить.
     * @throws IllegalArgumentException если у пользователя нет ни одного напоминания.
     */
    @Override
    public void deleteRemindsByUserId(long userId) {
        boolean removed = reminds.removeIf(r -> r.getUserId() == userId);
        if (!removed) {
            throw new IllegalArgumentException("No reminds found for userId " + userId);
        }
    }
}