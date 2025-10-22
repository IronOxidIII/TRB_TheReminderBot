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
}