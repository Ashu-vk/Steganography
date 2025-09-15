package com.stego;

import java.util.concurrent.*;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        // Core = 2, Max = 4, Queue = 5
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,        // core pool size
                4,        // max pool size
                10,       // keep-alive time for extra threads
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5), // queue with capacity 5
                new ThreadPoolExecutor.AbortPolicy() // rejection policy
        );

        // Submit 10 tasks
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            try {
                executor.execute(() -> {
                    System.out.println("Task " + taskId +
                            " is running on " + Thread.currentThread().getName());
                    try { Thread.sleep(2000); } catch (InterruptedException e) {}
                });
            } catch (RejectedExecutionException e) {
                System.out.println("Task " + taskId + " was REJECTED!");
            }
        }

        executor.shutdown();
    }
}

