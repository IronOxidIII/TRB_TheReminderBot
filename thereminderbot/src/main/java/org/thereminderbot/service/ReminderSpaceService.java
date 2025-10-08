package org.thereminderbot.service;

import org.thereminderbot.domain.Note;
import org.thereminderbot.domain.User;
import org.thereminderbot.repository.UserRepository;

public class ReminderSpaceService {
    private final UserRepository userRepository;

    public ReminderSpaceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNote(User user, Note note) {
        user.addNote(note);
    }

    public void shareNotes(User userFrom, User userTo) {
        var notesToShare = userFrom.getNotes();

        for (var note : notesToShare) {
            userTo.addNote(note);
        }
    }
}
