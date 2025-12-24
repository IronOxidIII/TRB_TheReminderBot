package org.thereminderbot.components.service;

import org.thereminderbot.domain.Remind;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class NotificationBuffer {

    private static final BlockingQueue<Remind> queue = new LinkedBlockingQueue<>();

    private NotificationBuffer() {}

    public static void push(Remind remind) {
        queue.offer(remind);
    }

    public static Remind poll() {
        return queue.poll();
    }

    public static boolean isEmpty() {
        return queue.isEmpty();
    }

    public static Remind take() throws InterruptedException {
        return queue.take();
    }
}