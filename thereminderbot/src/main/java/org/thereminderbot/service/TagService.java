package org.thereminderbot.service;

import org.thereminderbot.domain.Remind;
import org.thereminderbot.domain.Tag;
import org.thereminderbot.repository.RemindRepository;
import org.thereminderbot.repository.TagRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TagService {

    private final TagRepository tagRepository;
    private final RemindRepository remindRepository;

    private static final Logger log = LoggerFactory.getLogger(TagService.class);

    public TagService(TagRepository tagRepository, RemindRepository remindRepository) {
        this.tagRepository = tagRepository;
        this.remindRepository = remindRepository;
    }

    /**
     * Назначает тег указанной заметке.
     */
    public void assignTagToRemind(long remindId, String tagName) {
        if (remindRepository.getRemindById(remindId) == null) {
            log.warn("Заметка с ID {} не найдена, тег не назначен.", remindId);
            return;
        }

        long tagId = System.currentTimeMillis();
        Tag tag = new Tag(tagId, tagName, remindId);

        tagRepository.addTag(tag);
        log.info("Тег '{}' назначен заметке с ID {}.", tagName, remindId);
    }

    /**
     * Получает список напоминаний пользователя по имени тега.
     */
    public List<Remind> getUserRemindsByTagName(long userId, String tagName) {
        List<Tag> tags = tagRepository.getTagsByName(tagName);
        List<Remind> result = new ArrayList<>();

        for (Tag tag : tags) {
            Remind remind = remindRepository.getRemindById(tag.getRemindId());
            if (remind != null && remind.getUserId() == userId) {
                result.add(remind);
            }
        }

        log.info("Найдено {} напоминаний пользователя {} с тегом '{}'.", result.size(), userId, tagName);
        return result;
    }

    /**
     * Удаляет все теги, связанные с указанной заметкой.
     */
    public void removeTagsByRemind(long remindId) {
        tagRepository.deleteTagsByRemindId(remindId);
    }
}