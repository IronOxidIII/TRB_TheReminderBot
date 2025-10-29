package org.thereminderbot.service;

import org.thereminderbot.domain.User;
import org.thereminderbot.enums.UserMenu;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.repository.UserRepository;
import org.thereminderbot.repository.RemindRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private RemindRepository remindRepository;

    public UserService(UserRepository userRepository, RemindRepository remindRepository) {
        this.userRepository = userRepository;
        this.remindRepository = remindRepository;
    }

    /**
     * Уведомление пользователя
     */
    public void notifyUser(long userId, long remindId) {
        try {
            User user = userRepository.getUserById(userId);
            Remind remind = remindRepository.getRemindById(remindId);
            log.info("Уведомление для пользователя {}: {}", user.getUserName(), remind.getText());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Вывод всех напоминаний пользователя
     */
    public void printUsersReminds(long userId) {
        try {
            User user = userRepository.getUserById(userId);
            List<Remind> reminds = remindRepository.getRemindsByUser(user.getUserId());
            if (reminds.isEmpty()) {
                log.info("У пользователя {} нет напоминаний.", user.getUserName());
                return;
            }
            log.info("Напоминания пользователя {}:", user.getUserName());
            for (Remind r : reminds) {
                log.info("ID: {}, Текст: {}", r.getId(), r.getText());
            }
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при получении напоминаний пользователя: {}", e.getMessage());
        }
    }

    /**
     * Передача напоминаний от одного пользователя к другому
     */
    public void shareReminds(long userIdFrom, long userIdTo) {
        try {
            User fromUser = userRepository.getUserById(userIdFrom);
            User toUser = userRepository.getUserById(userIdTo);
            List<Remind> fromReminds = remindRepository.getRemindsByUser(fromUser);
            if (fromReminds.isEmpty()) {
                log.info("У пользователя {} нет напоминаний для передачи", fromUser.getUserName());
                return;
            }
            for (Remind remind : fromReminds) {
                Remind copiedRemind = new Remind(remind, toUser.getUserId());
                remindRepository.addRemind(copiedRemind);
            }
            log.info("Напоминания от пользователя {} успешно переданы пользователю {}",
                    fromUser.getUserName(), toUser.getUserName());
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при передачи напоминаний: {}", e.getMessage());
        }
    }

    /**
     * Вывод информации о пользователе
     */
    public void printUserInfo(long userId) {
        try {
            String userInfo = """
            Информация о пользователе:
            ID: %d
            Имя: %s
            Часовой пояс: %d
            Текущее меню: %s
            """.formatted(
                    user.getUserId(),
                    user.getUserName(),
                    user.getTimeZoneOffset(),
                    UserMenu.values()[user.getCurrentMenuId()]
            );
            System.out.println(userInfo);
            log.info("Выведена информация о пользователе ID: {}", userId);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при получении информации о пользователе с ID: {}", userId);
        }
    }

    /**
     * Удаление пользователя и его напоминаний
     */
    public void deleteUserAndReminds(long userId) {
        if (userId <= 0) {
            log.warn("Попытка удаления с невалидным ID пользователя: {}", userId);
            throw new IllegalArgumentException("Невалидный ID пользователя");
        }
        try {
            remindRepository.deleteRemindsByUserId(userId);
            userRepository.deleteUser(userId);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при удалении пользователя"};
    }
}
