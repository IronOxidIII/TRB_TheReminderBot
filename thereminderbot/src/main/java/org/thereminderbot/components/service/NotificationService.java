package org.thereminderbot.components.service;

import java.time.OffsetDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.thereminderbot.domain.Remind;
import org.thereminderbot.enums.RemindStatus;
import org.thereminderbot.repository.RemindRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService implements AutoCloseable {
    private static final Logger log =
            LoggerFactory.getLogger(NotificationService.class);
    private final ScheduledExecutorService scheduler;
    private final RemindRepository remindRepository;

    private ScheduledFuture<?> scheduledTask;
    private final AtomicBoolean started = new AtomicBoolean(false);

    public NotificationService(RemindRepository remindRepository) {
        this.remindRepository = remindRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        log.info("NotificationService started");
        if (!started.compareAndSet(false, true)) return;

        scheduledTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                checkAllReminds();
            } catch (Exception e) {
                log.error("Error in reminder check task", e);
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
                scheduler.awaitTermination(10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException ie) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            started.set(false);
        }
    }

    public void close() {
        stop();
    }

    private void checkAllReminds() {
        OffsetDateTime now = OffsetDateTime.now();

        for (Remind remind : remindRepository.getAll()) {
            if (remind.getStatus() == RemindStatus.Active &&
                    (now.isAfter(remind.getTime()) || now.isEqual(remind.getTime()))) {

                NotificationBuffer.push(remind);
                remind.setStatus(RemindStatus.Sent);
            }
        }
    }
}
