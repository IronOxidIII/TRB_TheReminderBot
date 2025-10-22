package org.thereminderbot.service;

import org.thereminderbot.domain.Remind;
import org.thereminderbot.repository.RemindRepository;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Сервис для работы с напоминаниями.
 */
public class RemindService {
    private final RemindRepository remindRepository;

    public RemindService(RemindRepository remindRepository) {
        this.remindRepository = remindRepository;
    }

    /**
     * Вывод всех напоминаний в консоль.
     */
    public void printAllReminds() {
        ArrayList<Remind> reminds = remindRepository.getAll();
        if (reminds.isEmpty()) {
            System.out.println("Нет ни одного напоминания.");
            return;
        }

        System.out.println("Список всех напоминаний:");
        for (Remind remind : reminds) {
            System.out.println(remind);
        }
    }

    /**
     * Отключить напоминание (изменить статус).
     */
    public void switchOffRemind(long remindId) {
        ArrayList<Remind> reminds = remindRepository.getAll();
        for (Remind remind : reminds) {
            if (remind.getId() == remindId) {
                remind.setStatus(0); //
                System.out.println("Напоминание ID " + remindId + " отключено.");
                return;
            }
        }
        System.out.println("Напоминание с ID " + remindId + " не найдено.");
    }

    /**
     * Изменить текст напоминания.
     */
    public void changeRemindText(long remindId) {
        ArrayList<Remind> reminds = remindRepository.getAll();
        for (Remind remind : reminds) {
            if (remind.getId() == remindId) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Введите новый текст для напоминания: ");
                String newText = scanner.nextLine();
                remind.setText(newText);
                System.out.println("Текст напоминания успешно изменён.");
                return;
            }
        }
        System.out.println("Напоминание с ID " + remindId + " не найдено.");
    }

    /**
     * Вывести информацию о напоминании.
     */
    public void printRemindInfo(long remindId) {
        ArrayList<Remind> reminds = remindRepository.getAll();
        for (Remind remind : reminds) {
            if (remind.getId() == remindId) {
                System.out.println("Информация о напоминании:");
                System.out.println(remind);
                return;
            }
        }
        System.out.println("Напоминание с ID " + remindId + " не найдено.");
    }
    public void deleteRemind(long remindId) {
        boolean removed = remindRepository.deleteRemindById(remindId);
        if (removed) {
            System.out.println("Напоминание с ID " + remindId + " удалено.");
        } else {
            System.out.println("Напоминание с ID " + remindId + " не найдено.");
        }
    }
}