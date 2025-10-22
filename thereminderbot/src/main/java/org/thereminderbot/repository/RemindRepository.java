package org.thereminderbot.repository;

import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;

import java.util.ArrayList;

/**
 * Репозиторий заметок.
 */

public class RemindRepository {
    /**
     * Список всех напоминаний, хранящихся в репозитории.
     * Используется как внутренняя коллекция для хранения данных в памяти.
     */

    private final ArrayList<Remind> reminds;

    /**
     * Возвращает копию списка всех напоминаний, сохранённых в репозитории.
     *
     * @return новый список, содержащий все текущие напоминания.
     */

    public RemindRepository() {
        // Инициализация пустого хранилища
        this.reminds = new ArrayList<>();
    }

    public ArrayList<Remind> getAll() {
        return new ArrayList<>(reminds);
    }

    /**
     * Получить одно напоминание по его идентификатору.
     * @param ID напоминания.
     * @return объект {Remind}, если найден; иначе {null}.
     */
    public Remind getRemindById(long remindId) {
        for (var r : reminds) {
            if (r.getId() == remindId) return r;
        }
        return null;
    }

    /**
     * Получить все напоминания, принадлежащие пользователю с указанным userId.
     * @param ID пользователя.
     * @return список напоминаний, принадлежащих этому пользователю.
     */

    public ArrayList<Remind> getRemindsByUser(long userId) {
        var result = new ArrayList<Remind>();
        for (var r : reminds) {
            if (r.getUserId() == userId) result.add(r);
        }
        return result;
    }

    /**
     * Добавить напоминание.
     * @param remind напоминание, которое нужно добавить;
     * если значение {null}, добавление не выполняется.
     */
    /**
     * Добавляет новое напоминание в репозиторий.
     * @param remind напоминание, которое нужно добавить.
     * @throws IllegalArgumentException если remind равен {null}.
     */
    public void addRemind(Remind remind) {
        if (remind == null) {
            throw new IllegalArgumentException("Remind cannot be null");
        }
        reminds.add(remind);
    }

    /**
     * Удаляет напоминание по ID.
     * @param ID напоминания, которое нужно удалить.
     * @throws IllegalArgumentException если напоминание с таким ID не найдено.
     */
    public void deleteRemindById(long remindId) {
        boolean removed = reminds.removeIf(r -> r.getId() == remindId);
        if (!removed) {
            throw new IllegalArgumentException("Remind with id " + remindId + " not found");
        }
    }

    /**
     * Удаляет все напоминания, принадлежащие пользователю с указанным ID.
     * @param ID пользователя, чьи напоминания нужно удалить.
     * @throws IllegalArgumentException если у пользователя нет ни одного напоминания.
     */
    public void deleteRemindsByUserId(long userId) {
        boolean removed = reminds.removeIf(r -> r.getUserId() == userId);
        if (!removed) {
            throw new IllegalArgumentException("No reminds found for userId " + userId);
        }
    }
}