package com.example.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class FlashSaleSimulator {

    private static int inventory = 100; // 初始库存
    private static final ReentrantLock lock = new ReentrantLock();

    public static void attemptPurchase() {
        lock.lock();
        try {
            if (inventory > 0) {
                inventory--; // 减少库存
                System.out.println("Purchase successful. Remaining inventory: " + inventory);
            } else {
                System.out.println("Purchase failed. No inventory left.");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(20); // 创建线程池

        for (int i = 0; i < 200; i++) {
            executor.submit(FlashSaleSimulator::attemptPurchase);
        }

        executor.shutdown();
    }
}
