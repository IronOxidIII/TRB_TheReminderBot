package org.thereminderbot.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    private String name;
    private List<Note> notes;

    public User() {

    }

    public User(String name) {
        this.name = name;
        notes = new ArrayList<Note>();
    }

    public void setName(String name) {
        if (name.isEmpty() || name == null)
            throw new IllegalArgumentException("Имя должно содержать хотя бы один символ.");

        this.name = name;
    }

    public String getName() {
        return new String(name);
    }

    public ArrayList<Note> getNotes() {
        return new ArrayList<>(notes);
    }

    public Note findNoteByText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text не может быть null");
        }

        for (var note : notes) {
            if (note.getText().equals(text)) {
                return note;
            }
        }

        // Не нашли запись с таким текстом.
        return null;
    }

    public Note addNote(Note note) {
        if (note == null)
            throw new IllegalArgumentException("Напоминание не может быть null.");

        if (notes.contains(note))
            throw new IllegalArgumentException("У пользователя уже есть такое напоминание.");

        var noteToAdd = new Note(note);
        notes.add(noteToAdd);

        return noteToAdd;
    }

    @Override
    public String toString() {
        var notesStringBulider = new StringBuilder();

        for (var note : notes) {
            notesStringBulider.append(note.getText());
            notesStringBulider.append(" ");
        }

        return String.format("Name = %s, Notes = [%s]", name, notesStringBulider.toString());
    }
}
