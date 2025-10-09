package org.thereminderbot;

import org.thereminderbot.domain.Note;
import org.thereminderbot.domain.User;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.service.ReminderSpaceService;

public class Main {
    public static void main(String[] args) {
        var userRepository = new UserRepository();

        var user1 = new User("Антон");
        var user2 = new User("Боупо");

        userRepository.loginUser(user1);
        userRepository.loginUser(user2);

        var reminderSpaceService = new ReminderSpaceService(userRepository);

        var note1 = new Note("Покормить собаку.");
        var note2 = new Note("Выспаться перед работой(.");
        var note3 = new Note("Покормить мою кошку.");

        reminderSpaceService.addNote(user1, note1);
        reminderSpaceService.addNote(user1, note2);
        reminderSpaceService.addNote(user2, note3);

        reminderSpaceService.shareNotes(user2, user1);

        System.out.println(user1);
        System.out.println(user2);

        userRepository.
                findUserByName("Антон").
                findNoteByText("Покормить мою кошку.").
                setText("Другое напоминание.");

        System.out.println("----------------");

        System.out.println(user1);
        System.out.println(user2);
    }
}
