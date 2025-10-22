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
     * Получить все напоминания по userId
     */
    public ArrayList<Remind> getRemindsById(long id) {
        ArrayList<Remind> result = new ArrayList<>();
        for (Remind r : reminds) {
            if (r.getUserId() == id) result.add(r);
        }
        return result;
    }

    /**
     * Получить все напоминания по User
     */
    public ArrayList<Remind> getRemindsByUser(User user) {
        if (user == null) return new ArrayList<>();
        return getRemindsById(user.getId());
    }

    /**
     * Добавить напоминание
     */
    public void addRemind(Remind remind) {
        if (remind != null) {
            reminds.add(remind);
        }
    }

    /**
     * Удалить одно напоминание по его ID
     */
    public boolean deleteRemindById(long remindId) {
        return reminds.removeIf(r -> r.getId() == remindId);
    }

    /**
     * Удалить все напоминания, принадлежащие пользователю с указанным userId.
     */
    public int deleteRemindsByUserId(long userId) {
        int before = reminds.size();
        reminds.removeIf(r -> r.getUserId() == userId);
        return before - reminds.size();
    }
}