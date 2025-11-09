package org.thereminderbot.domain;

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
    private int currentMenuId;

    public User(long userId, String userName, ZoneId timeZoneOffset, int currentMenuId) {
        this.userId = userId;
        this.userName = userName;
        this.timeZoneOffset = timeZoneOffset;
        this.currentMenuId = currentMenuId;
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
     * @return Число - id меню пользователя.
     */
    public int getCurrentMenuId() {
        return currentMenuId;
    }

    /**
     * Назначить новое меню пользователю по {@link org.thereminderbot.enums.UserMenu}.
     * @param menuId Число - новое id меню пользователя.
     */
    public void setCurrentMenuId(int menuId) {
        this.currentMenuId = menuId;
    }
}
