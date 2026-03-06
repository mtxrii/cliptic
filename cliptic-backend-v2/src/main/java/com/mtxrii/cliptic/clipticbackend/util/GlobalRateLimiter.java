package com.mtxrii.cliptic.clipticbackend.util;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GlobalRateLimiter {
    private final int limit;
    private final long windowMillis;
    private final Deque<Long> timestamps = new ConcurrentLinkedDeque<>();

    public GlobalRateLimiter(int limit, long windowMillis) {
        this.limit = limit;
        this.windowMillis = windowMillis;
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        while (!this.timestamps.isEmpty() && now - this.timestamps.peekFirst() > this.windowMillis) {
            this.timestamps.pollFirst();
        }

        if (this.timestamps.size() >= this.limit) {
            return false;
        }

        this.timestamps.addLast(now);
        return true;
    }
}
