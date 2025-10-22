package org.thereminderbot.repository;

import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;

import java.util.ArrayList;

public class RemindRepository {
    private final ArrayList<Remind> reminds;

    public ArrayList<Remind> getAll() {
        return new ArrayList<>(reminds);
    }

    public RemindRepository() {
        // Инициализация пустого хранилища
        this.reminds = new ArrayList<>();
    }

    /**
     * Получить все напоминания по userId.
     * @param id идентификатор пользователя, для которого нужно найти напоминания
     * @return список напоминаний, принадлежащих указанному пользователю
     */
    public ArrayList<Remind> getRemindsById(long id) {
        var result = new ArrayList<Remind>();
        for (var r : reminds) {
            if (r.getUserId() == id) result.add(r);
        }
        return result;
    }

    /**
     * Получить все напоминания по userId.
     * @param id идентификатор пользователя, для которого нужно найти напоминания
     * @return список напоминаний, принадлежащих указанному пользователю
     */
    public ArrayList<Remind> getRemindsByUser(User user) {
        if (user == null) return new ArrayList<>();
        return getRemindsById(user.getId());
    }

    /**
     * Добавить напоминание
     * @param remind объект напоминания, который нужно добавить;
     * если значение {null}, добавление не выполняется
     */
    public void addRemind(Remind remind) {
        if (remind != null) {
            reminds.add(remind);
        }
    }

    /**
     * Удалить одно напоминание по его ID.
     * @param remindId идентификатор напоминания, которое нужно удалить
     * @return {true}, если напоминание было найдено и удалено;
     * {false}, если напоминание с таким ID не найдено
     */
    public boolean deleteRemindById(long remindId) {
        return reminds.removeIf(r -> r.getId() == remindId);
    }


    /**
     * Удалить все напоминания, принадлежащие пользователю с указанным идентификатором.
     * @param userId идентификатор пользователя, чьи напоминания нужно удалить
     * @return количество удалённых напоминаний
     */
    public int deleteRemindsByUserId(long userId) {
        int before = reminds.size();
        reminds.removeIf(r -> r.getUserId() == userId);
        return before - reminds.size();
    }
}