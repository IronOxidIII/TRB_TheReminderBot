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

    /**
     * Поиск пользователя по id
     */

    public void notifyUser(long userId, long remindId) {
        try {
            Var user = userRepository.getUserById(userId);
            List<Remind> reminds = remindRepository.getRemindsById(remindId);
            if (reminds.isEmpty()) {
                throw new IllegalArgumentException("Напоминание не найдено");
            }
            Remind remind = reminds.get(0);
            System.out.println("Уведомление для пользователя " + user.getUserName() + ": " + remind.getText());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Вывод всех напоминаний пользователя
     */

    public void printUsersReminds(long userId) {
        try {
            Var user = userRepository.getUserById(userId);
            List<Remind> reminds = remindRepository.getRemindsByUser(user);
            if (reminds.isEmpty()) {
                System.out.println("У пользователя нет напоминаний");
                return;
            }
            System.out.println("Напоминания пользователя " + user.getUserName() + ":");
            for (Remind r : reminds) {
                System.out.println("ID: " + r.getId() + ", Текст: " + r.getText());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Передача напоминаний от одного пользователя к другому
     */

    public void shareReminds(long userIdFrom, long userIdTo) {
        try {
            Var fromUser = userRepository.getUserById(userIdFrom);
            Var toUser = userRepository.getUserById(userIdTo);
            List<Remind> fromReminds = remindRepository.getRemindsByUser(fromUser);
            if (fromReminds.isEmpty()) {
                System.out.println("У пользователя " + fromUser.getUserName() + " нет напоминаний для передачи");
                return;
            }
            for (Remind remind : fromReminds) {
                Remind copiedRemind = new Remind(remind, toUser.getUserId());
                remindRepository.addRemind(copiedRemind);
            }
            System.out.println("Напоминания от пользователя " + fromUser.getUserName() +
                    " успешно переданы пользователю " + toUser.getUserName());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Переключение меню
     */

    public void turnOnMenu(long userId, UserMenu userMenu) {
        try {
            Var user = userRepository.getUserById(userId);
            user.setCurrentMenuId(userMenu.ordinal());
            System.out.println("Пользователь " + user.getUserName() + " переключился в меню " + userMenu);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Вывод информации о пользователе
     */

    public void printUserInfo(long userId) {
        try {
            Var user = userRepository.getUserById(userId);
            System.out.println("Информация о пользователе:");
            System.out.println("ID: " + user.getUserId());
            System.out.println("Имя: " + user.getUserName());
            System.out.println("Часовой пояс: " + user.getTimeZoneOffset());
            System.out.println("Текущее меню: " + UserMenu.values()[user.getCurrentMenuId()]);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
