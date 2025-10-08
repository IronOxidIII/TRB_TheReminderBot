package org.thereminderbot.domain;

import java.util.stream.IntStream;

public class Note {
    private String text;

    public Note() {
        this.text = "";
    }

    public Note(String text) {
        if (text == null)
            throw new IllegalArgumentException("Text не может быть null");

        this.text = text;
    }

    public Note(Note note) {
        this.text = note.getText();
    }

    public String getText() {
        return new String(text);
    }

    public void setText(String text) {
        if (text == null)
            throw new IllegalArgumentException("Text не может быть null");

        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Note other = (Note) obj;
        if ((this.text == null) ? other.text == null : !this.text.equals(other.text)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return IntStream.range(0, text.length()).map(i -> text.charAt(i)).sum();
    }
}
