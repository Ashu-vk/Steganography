package com.stego;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueExamle {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(2); // small capacity to see blocking

        Thread producer = new Thread(() -> {
            String[] items = {"A", "B", "C", "D", "E", "F", "G", "H"};
            try {
                for (String item : items) {
                    System.out.println("Producer trying to put: " + item);
                    queue.put(item); // blocks if queue is full
                    System.out.println("Producer put: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 4; i++) {
                    Thread.sleep(5000); // simulate slow consumption
                    System.out.println("Consumer waiting to take...");
                    String item = queue.take(); // blocks if queue empty
                    System.out.println("Consumer took: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
