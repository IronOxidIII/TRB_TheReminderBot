package org.thereminderbot.components.service;
import org.thereminderbot.domain.Remind;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NotificationBuffer {

    private final Queue<Remind> queue = new ConcurrentLinkedQueue<>();

    /**
     * Добавить напоминание в очередь уведомлений
     */
    public void push(Remind remind) {
        queue.add(remind);
    }

    /**
     * Получить следующее уведомление.
     */
    public Remind poll() {
        return queue.poll();
    }

    /**
     * Проверка, есть ли ожидающие уведомления.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Получить следующее уведомление из очереди.
     */
    public Remind take() {
        Remind result;
        while ((result = queue.poll()) == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return result;
    }
}