package org.thereminderbot.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thereminderbot.domain.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest {

    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository = new TagRepository();
    }

    @Test
    void addTag_shouldAddTagSuccessfully() {
        Tag tag = new Tag(1L, "abc", 10L);

        tagRepository.addTag(tag);

        List<Tag> all = tagRepository.getAllTags();
        assertEquals(1, all.size());
        assertEquals(tag, all.get(0));
    }

    @Test
    void addTag_shouldIgnoreNullTag() {
        tagRepository.addTag(null);

        assertTrue(tagRepository.getAllTags().isEmpty());
    }

    @Test
    void deleteTagById_shouldReturnTrueIfTagExists() {
        Tag tag = new Tag(1L, "test", 10L);
        tagRepository.addTag(tag);

        boolean result = tagRepository.deleteTagById(1L);

        assertTrue(result);
        assertTrue(tagRepository.getAllTags().isEmpty());
    }

    @Test
    void deleteTagById_shouldReturnFalseIfTagNotFound() {
        boolean result = tagRepository.deleteTagById(99L);

        assertFalse(result);
    }

    @Test
    void deleteTagsByRemindId_shouldRemoveOnlyMatchingTags() {
        Tag tag1 = new Tag(1L, "a", 10L);
        Tag tag2 = new Tag(2L, "b", 20L);
        Tag tag3 = new Tag(3L, "c", 10L);

        tagRepository.addTag(tag1);
        tagRepository.addTag(tag2);
        tagRepository.addTag(tag3);

        tagRepository.deleteTagsByRemindId(10L);

        List<Tag> all = tagRepository.getAllTags();
        assertEquals(1, all.size());
        assertEquals(tag2, all.get(0));
    }

    @Test
    void deleteTagsByRemindId_shouldDoNothingIfNoTagsFound() {
        Tag tag = new Tag(1L, "test", 20L);
        tagRepository.addTag(tag);

        tagRepository.deleteTagsByRemindId(99L);

        assertEquals(1, tagRepository.getAllTags().size());
    }

    @Test
    void getTagsByRemindId_shouldReturnOnlyMatchingTags() {
        Tag tag1 = new Tag(1L, "x", 10L);
        Tag tag2 = new Tag(2L, "y", 10L);
        Tag tag3 = new Tag(3L, "z", 20L);

        tagRepository.addTag(tag1);
        tagRepository.addTag(tag2);
        tagRepository.addTag(tag3);

        List<Tag> result = tagRepository.getTagsByRemindId(10L);

        assertEquals(2, result.size());
        assertTrue(result.contains(tag1));
        assertTrue(result.contains(tag2));
    }

    @Test
    void getTagsByRemindId_shouldReturnEmptyListIfNoneFound() {
        List<Tag> result = tagRepository.getTagsByRemindId(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllTags_shouldReturnAllAddedTags() {
        Tag tag1 = new Tag(1L, "a", 10L);
        Tag tag2 = new Tag(2L, "b", 20L);

        tagRepository.addTag(tag1);
        tagRepository.addTag(tag2);

        List<Tag> result = tagRepository.getAllTags();

        assertEquals(2, result.size());
        assertTrue(result.contains(tag1));
        assertTrue(result.contains(tag2));
    }

    @Test
    void getTagsByName_shouldReturnMatchingTagsIgnoringCase() {
        Tag tag1 = new Tag(1L, "abc", 10L);
        Tag tag2 = new Tag(2L, "abc", 20L);
        Tag tag3 = new Tag(3L, "bca", 30L);

        tagRepository.addTag(tag1);
        tagRepository.addTag(tag2);
        tagRepository.addTag(tag3);

        List<Tag> result = tagRepository.getTagsByName("ABC");

        assertEquals(2, result.size());
        assertTrue(result.contains(tag1));
        assertTrue(result.contains(tag2));
    }

    @Test
    void getTagsByName_shouldReturnEmptyListIfNoMatches() {
        Tag tag = new Tag(1L, "test", 10L);
        tagRepository.addTag(tag);

        List<Tag> result = tagRepository.getTagsByName("abc");

        assertTrue(result.isEmpty());
    }
}
