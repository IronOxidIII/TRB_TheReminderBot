package org.thereminderbot.repository;

import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.User;

import java.util.ArrayList;

/**
 * Репозиторий заметок.
 */
public class RemindRepository {
    private ArrayList<Remind> reminds;

    public RemindRepository() {
        //TODO
        return;
    }

    public ArrayList<Remind> getRemindById(long id) {
        //TODO
        return null;
    }

    public ArrayList<Remind> getRemindsByUser(User user) {
        //TODO
        return null;
    }

    public void addRemind(Remind remind) {
        //TODO
        return;
    }
}
