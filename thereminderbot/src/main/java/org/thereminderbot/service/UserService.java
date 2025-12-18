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
    private final UserRepository userRepository;
    private  final RemindRepository remindRepository;

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
            log.info("Notification for user {}: {}", user.getUserName(), remind.getText());
        } catch (IllegalArgumentException e) {
            log.warn("Failed to notify user {}: {}", userId, e.getMessage());
        }
    }

    /**
     * Вывод всех напоминаний пользователя
     */
    public void printUsersReminds(long userId) {
        try {
            List<Remind> reminds = remindRepository.getRemindsByUser(userId);
            if (reminds.isEmpty()) {
                log.info("У пользователя с id {} нет напоминаний.", userId);
                return;
            }
            log.info("Напоминания пользователя с id {}:", userId);
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
            List<Remind> fromReminds = remindRepository.getRemindsByUser(userIdFrom);
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
                    userRepository.getUserById(userId).getUserId(),
                    userRepository.getUserById(userId).getUserName(),
                    userRepository.getUserById(userId).getTimeZoneOffset(),
                    UserMenu.values()[userRepository.getUserById(userId).getCurrentMenuId()]
            );
            log.info("Информация о пользователе:\n{}", userInfo);
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
            throw new IllegalArgumentException("Invalid user ID");
        }
        try {
            remindRepository.deleteRemindsByUserId(userId);
            userRepository.deleteUser(userId);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при удалении пользователя");
        }
    }
}
