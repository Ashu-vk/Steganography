package com.stego;

enum CounterSingleton {
    INSTANCE;   // The only instance

    private int count = 0;

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }
}

public class Test {
    public static void main(String[] args) {
        CounterSingleton s1 = CounterSingleton.INSTANCE;
        CounterSingleton s2 = CounterSingleton.INSTANCE;

        s1.increment();
        s1.increment();

        System.out.println("s1 count = " + s1.getCount()); // 2
        System.out.println("s2 count = " + s2.getCount()); // 2 (same instance!)

        s2.decrement();

        System.out.println("s1 count = " + s1.getCount()); // 1
        System.out.println("s2 count = " + s2.getCount()); // 1
    }
}
