package org.thereminderbot.domain;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor_shouldCreateUserWithCorrectFields() {
        long userId = 1L;
        String userName = "testUser";
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        int menuId = 0;

        User user = new User(userId, userName, zoneId, menuId);

        assertEquals(userId, user.getUserId());
        assertEquals(userName, user.getUserName());
        assertEquals(zoneId, user.getTimeZoneOffset());
        assertEquals(menuId, user.getCurrentMenuId());
    }

    @Test
    void setters_shouldUpdateFieldsCorrectly() {
        User user = new User(1L, "oldName", ZoneId.systemDefault(), 0);

        user.setUserId(2L);
        user.setUserName("newName");
        user.setTimeZoneOffset(ZoneId.of("UTC"));
        user.setCurrentMenuId(3);

        assertEquals(2L, user.getUserId());
        assertEquals("newName", user.getUserName());
        assertEquals(ZoneId.of("UTC"), user.getTimeZoneOffset());
        assertEquals(3, user.getCurrentMenuId());
    }

    @Test
    void getters_shouldReturnCorrectValues() {
        User user = new User(
                10L,
                "Abc",
                ZoneId.of("Asia/Yekaterinburg"),
                2
        );

        assertEquals(10L, user.getUserId());
        assertEquals("Abc", user.getUserName());
        assertEquals(ZoneId.of("Asia/Yekaterinburg"), user.getTimeZoneOffset());
        assertEquals(2, user.getCurrentMenuId());
    }
}
