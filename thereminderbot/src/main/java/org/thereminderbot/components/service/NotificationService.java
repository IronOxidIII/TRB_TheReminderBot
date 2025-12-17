package org.thereminderbot.components.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.OffsetDateTime;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.enums.RemindStatus;
import org.thereminderbot.repository.RemindRepository;

public class NotificationService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final RemindRepository remindRepository;
    private final NotificationBuffer buffer;

    public NotificationService(RemindRepository remindRepository, NotificationBuffer buffer) {
        this.remindRepository = remindRepository;
        this.buffer = buffer;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkAllReminds();
            } catch (Exception e) {
                System.err.println("Error in reminder check task");
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private void checkAllReminds() {
        OffsetDateTime now = OffsetDateTime.now();

        for (Remind remind : remindRepository.getAll()) {
            if (remind.getStatus() == RemindStatus.Active &&
                    (now.isAfter(remind.getTime()) || now.isEqual(remind.getTime()))) {

                buffer.push(remind);
                remind.setStatus(RemindStatus.Sent);
            }
        }
    }
}