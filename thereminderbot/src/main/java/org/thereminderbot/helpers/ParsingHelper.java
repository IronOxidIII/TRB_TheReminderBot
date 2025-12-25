package org.thereminderbot.helpers;

import java.text.ParseException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ParsingHelper {
    public static boolean isId(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Строка не может быть пустой.");
        }

        try {
            Long.parseLong(input);
        }
        catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static Duration parseDuration(String input)
            throws ParseException {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Стока не может быть пустой.");
        }
        int hours = Integer.parseInt(input.substring(0, 2));
        int minutes = Integer.parseInt(input.substring(3, input.length() - 1));

        String rightDuration = String.format("PT%dH%dM", hours, minutes);

        return Duration.parse(rightDuration);
    }

    public static Long parseLong(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Строка не может быть пустой.");
        }

        try {
            return Long.parseLong(input);
        } catch (Exception e) {
            throw new NumberFormatException("Не удалось распарсить input в long.");
        }
    }
}