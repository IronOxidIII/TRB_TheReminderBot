package org.thereminderbot.service;
import org.thereminderbot.enums.RemindStatus;
import org.thereminderbot.repository.RemindRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Сервис для работы с напоминаниями.
 */
public class RemindService {
    private final RemindRepository remindRepository;

    private static final Logger log = LoggerFactory.getLogger(RemindService.class);

    public RemindService(RemindRepository remindRepository) {
        this.remindRepository = remindRepository;
    }

    /**
     * Отключить напоминание (изменить статус).
     */
    public void switchOffRemind(long remindId) {
        var reminds = remindRepository.getAll();
        for (var remind : reminds) {
            if (remind.getId() == remindId) {
                remind.setStatus(RemindStatus.Deactivated);
                log.warn("Напоминание с ID {} отключено.", remindId);
                return;
            }
        }
        log.warn("Напоминание с ID {} не найдено.", remindId);
    }

    /**
     * Изменить текст напоминания.
     * @param remindId - id напоминания.
     * @param newText - новый текст напоминания.
     * @throws IllegalArgumentException если напоминание с таким ID не найдено.
     */
    public void changeRemindText(long remindId, String newText) {
        var remind = remindRepository.getRemindById(remindId);

        if (remind == null) {
            log.warn("Напоминание с ID {} не найдено.", remindId);
            throw new IllegalArgumentException("Напоминание с ID " + remindId + " не найдено.");
        }
        remind.setText(newText);
        log.info("Текст напоминания для ID {} успешно изменён.", remindId);
    }

    public void deleteRemind(long remindId) {
        try {
            remindRepository.deleteRemindById(remindId);
            log.info("Напоминание с ID {} удалено.", remindId);
        } catch (IllegalArgumentException e) {
            log.warn("Не удалось удалить напоминание: {}", e.getMessage());
        }
    }
}