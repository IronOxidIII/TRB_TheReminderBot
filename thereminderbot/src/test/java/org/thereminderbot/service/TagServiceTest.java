package org.thereminderbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.Tag;
import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.TagRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagServiceTest {

    private TagRepository tagRepository;
    private RemindRepository remindRepository;
    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagRepository = new TagRepository();
        remindRepository = new RemindRepository();
        tagService = new TagService(tagRepository, remindRepository);
    }

    @Test
    void assignTagToRemind_shouldAddTagIfRemindExists() {
        Remind remind = new Remind(
                1L,
                "Test",
                10L,
                OffsetDateTime.now(),
                Duration.ofHours(1)
        );
        remindRepository.addRemind(remind);

        tagService.assignTagToRemind(1L, "abc");

        List<Tag> tags = tagRepository.getTagsByRemindId(1L);
        assertEquals(1, tags.size());
        assertEquals("abc", tags.get(0).getName());
        assertEquals(1L, tags.get(0).getRemindId());
    }

    @Test
    void assignTagToRemind_shouldNotAddTagIfRemindNotFound() {
        tagService.assignTagToRemind(99L, "abc");

        assertTrue(tagRepository.getAllTags().isEmpty());
    }

    @Test
    void getUserRemindsByTagName_shouldReturnOnlyUserReminds() {
        Remind r1 = new Remind(1L, "A", 10L, OffsetDateTime.now(), Duration.ofHours(1));
        Remind r2 = new Remind(2L, "B", 20L, OffsetDateTime.now(), Duration.ofHours(1));
        Remind r3 = new Remind(3L, "C", 10L, OffsetDateTime.now(), Duration.ofHours(1));

        remindRepository.addRemind(r1);
        remindRepository.addRemind(r2);
        remindRepository.addRemind(r3);

        tagRepository.addTag(new Tag(1L, "work", 1L));
        tagRepository.addTag(new Tag(2L, "work", 2L));
        tagRepository.addTag(new Tag(3L, "work", 3L));

        List<Remind> result = tagService.getUserRemindsByTagName(10L, "work");

        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r3));
        assertFalse(result.contains(r2));
    }

    @Test
    void getUserRemindsByTagName_shouldReturnEmptyListIfNoTags() {
        Remind remind = new Remind(
                1L,
                "Test",
                10L,
                OffsetDateTime.now(),
                Duration.ofHours(1)
        );
        remindRepository.addRemind(remind);

        List<Remind> result = tagService.getUserRemindsByTagName(10L, "unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void removeTagsByRemind_shouldRemoveAllTagsForRemind() {
        tagRepository.addTag(new Tag(1L, "a", 10L));
        tagRepository.addTag(new Tag(2L, "b", 10L));
        tagRepository.addTag(new Tag(3L, "c", 20L));

        tagService.removeTagsByRemind(10L);

        List<Tag> remaining = tagRepository.getAllTags();
        assertEquals(1, remaining.size());
        assertEquals(20L, remaining.get(0).getRemindId());
    }
}
