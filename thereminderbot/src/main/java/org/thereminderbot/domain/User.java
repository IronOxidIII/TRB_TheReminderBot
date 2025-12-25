package org.thereminderbot.domain;

import org.thereminderbot.enums.UserMenu;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * Доменный класс пользователя.
 */
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    private long userId;

    /**
     * Имя пользователя.
     */
    private String userName;

    /**
     * Часовой пояс пользователя.
     */
    private ZoneId timeZoneOffset;

    /**
     * Меню, в котором находится пользователь.
     * @see org.thereminderbot.enums.UserMenu
     */
    private UserMenu currentMenu;

    public static UserMenu defaultMenu = UserMenu.MainPage;

    public User(long userId, String userName, ZoneId timeZoneOffset, UserMenu currentMenu) {
        this.userId = userId;
        this.userName = userName;
        this.timeZoneOffset = timeZoneOffset;
        this.currentMenu = currentMenu;
    }

    /**
     * Получить id пользователя.
     * @return Число - id пользователя.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Назначить новое id пользователю.
     * @param userId Число - новое id пользователя.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Получить имя пользователя.
     * @return Строка - имя пользователя.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Назначить новое имя пользователю.
     * @param userName Строка - новое имя пользователя.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Получить часовой пояс пользователя.
     * @return ZoneId - Часовой пояс пользователя.
     */
    public ZoneId getTimeZoneOffset() {
        return timeZoneOffset;
    }

    /**
     * Назначить часовой пояс пользователю.
     * @param timeZoneOffset Новый часовой пояс пользователя.
     */
    public void setTimeZoneOffset(ZoneId timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    /**
     * Получить id меню пользователя по {@link org.thereminderbot.enums.UserMenu}.
     * @return Меню пользователя.
     */
    public UserMenu getCurrentMenu() {
        return currentMenu;
    }

    /**
     * Назначить новое меню пользователю по {@link org.thereminderbot.enums.UserMenu}.
     * @param menu Новое меню пользователя.
     */
    public void setCurrentMenu(UserMenu menu) {
        this.currentMenu = menu;
    }
}
