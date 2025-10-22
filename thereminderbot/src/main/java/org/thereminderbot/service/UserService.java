package org.thereminderbot.service;

import org.thereminderbot.domain.User;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.repository.RemindRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;
    private RemindRepository remindRepository;

    public UserService(UserRepository userRepository, RemindRepository remindRepository) {
        this.userRepository = userRepository;
        this.remindRepository = remindRepository;
    }

    public void notifyUser(long userId, long remindId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            System.out.println("Пользователь не найден");
            return;
        }
        List<Remind> reminds = remindRepository.getRemindsById(remindId);
        if (reminds == null || reminds.isEmpty()) {
            System.out.println("Напоминание не найдено");
            return;
        }
        Remind remind = reminds.get(0);
        System.out.println("Уведомление для пользователя " + user.getUserName() + ": " + remind.getText());
    }

    public void printUsersReminds(long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            System.out.println("Пользователь не найден");
            return;
        }
        List<Remind> reminds = remindRepository.getRemindsByUser(user);
        if (reminds == null || reminds.isEmpty()) {
            System.out.println("У пользователя нет напоминаний");
            return;
        }
        System.out.println("Напоминания пользователя " + user.getUserName() + ":");
        for (Remind r : reminds) {
            System.out.println("ID: " + r.getId() + ", Текст: " + r.getText());
        }
    }

    public void shareReminds(long userIdFrom, long userIdTo) {
        User fromUser = userRepository.getUserById(userIdFrom);
        User toUser = userRepository.getUserById(userIdTo);
        if (fromUser == null || toUser == null) {
            System.out.println("Один из пользователей не найден");
            return;
        }
        List<Remind> fromReminds = remindRepository.getRemindsByUser(fromUser);
        if (fromReminds == null || fromReminds.isEmpty()) {
            System.out.println("У пользователя " + fromUser.getUserName() + " нет напоминаний для передачи");
            return;
        }
        for (Remind remind : fromReminds) {
            Remind copiedRemind = new Remind(
                    remind.getId(),
                    remind.getText(),
                    toUser.getUserId(),
                    remind.getTime(),
                    remind.getFrequencyOfRepetition());
            remindRepository.addRemind(copiedRemind);
        }
        System.out.println("Напоминания от пользователя " + fromUser.getUserName() +
                " успешно переданы пользователю " + toUser.getUserName());
    }

    public void turnOnMenu(long userId, UserMenu userMenu) {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            user.setCurrentMenuId(userMenu.ordinal());
            System.out.println("Пользователь " + user.getUserName() + " переключился в меню " + userMenu);
        } else {
            System.out.println("Пользователь не найден");
        }
    }

    public void printUserInfo(long userId) {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            System.out.println("Информация о пользователе:");
            System.out.println("ID: " + user.getUserId());
            System.out.println("Имя: " + user.getUserName());
            System.out.println("Часовой пояс: " + user.getTimeZoneOffset());
            System.out.println("Текущее меню: " + UserMenu.values()[user.getCurrentMenuId()]);
        } else {
            System.out.println("Пользователь не найден");
        }
    }
}
