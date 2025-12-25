package org.thereminderbot.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void constructor_shouldCorrectlyInitializeFields() {
        long id = 1L;
        String name = "important";
        long remindId = 10L;

        Tag tag = new Tag(id, name, remindId);

        assertEquals(id, tag.getId());
        assertEquals(name, tag.getName());
        assertEquals(remindId, tag.getRemindId());
    }

    @Test
    void setName_shouldChangeTagName() {
        Tag tag = new Tag(1L, "old", 5L);

        tag.setName("new");

        assertEquals("new", tag.getName());
    }

    @Test
    void setId_shouldChangeTagId() {
        Tag tag = new Tag(1L, "tag", 5L);

        tag.setId(2L);

        assertEquals(2L, tag.getId());
    }

    @Test
    void setRemindId_shouldChangeRemindId() {
        Tag tag = new Tag(1L, "tag", 5L);

        tag.setRemindId(20L);

        assertEquals(20L, tag.getRemindId());
    }

    @Test
    void getName_shouldReturnNewStringInstance() {
        String name = "test";
        Tag tag = new Tag(1L, name, 5L);

        String returnedName = tag.getName();

        assertEquals(name, returnedName);
        assertNotSame(name, returnedName);
    }
}
