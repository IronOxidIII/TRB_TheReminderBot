package org.thereminderbot.repository;

import org.thereminderbot.domain.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Репозиторий тегов.
 */
public class TagRepository {

    private final List<Tag> tags;
    private static final Logger log = LoggerFactory.getLogger(TagRepository.class);

    public TagRepository() {
        this.tags = new ArrayList<>();
    }

    /**
     * Добавить тег.
     */
    public void addTag(Tag tag) {
        if (tag == null) {
            log.warn("Попытка добавить null тег.");
            return;
        }
        tags.add(tag);
        log.info("Тег '{}' добавлен к заметке с ID {}.", tag.getName(), tag.getRemindId());
    }

    /**
     * Удалить тег по его id.
     */
    public boolean deleteTagById(long tagId) {
        for (Tag tag : tags) {
            if (tag.getId() == tagId) {
                tags.remove(tag);
                log.info("Тег с ID {} успешно удалён.", tagId);
                return true;
            }
        }
        log.warn("Тег с ID {} не найден.", tagId);
        return false;
    }

    /**
     * Удаляет все теги, связанные с определённой заметкой.
     */
    public void deleteTagsByRemindId(long remindId) {
        boolean removed = false;

        for (Tag tag : new ArrayList<>(tags)) {
            if (tag.getRemindId() == remindId) {
                tags.remove(tag);
                removed = true;
            }
        }

        if (removed) {
            log.info("Все теги, связанные с заметкой ID {}, успешно удалены.", remindId);
        } else {
            log.warn("Теги, связанные с заметкой ID {}, не найдены.", remindId);
        }
    }

    /**
     * Получает все теги для указанной заметки.
     */
    public List<Tag> getTagsByRemindId(long remindId) {
        List<Tag> result = new ArrayList<>();
        for (Tag t : tags) {
            if (t.getRemindId() == remindId) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Получить все теги.
     */
    public List<Tag> getAllTags() {
        return tags;
    }

    /**
     * Получить все теги с заданным именем.
     */
    public List<Tag> getTagsByName(String tagName) {
        List<Tag> result = new ArrayList<>();
        for (Tag t : tags) {
            if (t.getName().equalsIgnoreCase(tagName)) {
                result.add(t);
            }
        }
        return result;
    }
}