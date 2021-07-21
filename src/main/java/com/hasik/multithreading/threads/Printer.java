package main.java.com.hasik.multithreading.threads;

import main.java.com.hasik.multithreading.controller.Process;

import java.util.concurrent.BlockingQueue;

/**
 * Author : Hasmik.Ghazaryan
 * 7/21/2021
 */
public class Printer implements Runnable {

    private final BlockingQueue<Integer> queue;

    public Printer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        synchronized (queue) {
            while(Process.running) {
                try {
                    print();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void print() throws InterruptedException {
        synchronized(queue) {
            Thread.sleep(1000);
            System.out.println("Thread: " + Thread.currentThread().getName() + " Queue size: " + queue.size());
            queue.notifyAll();
            queue.wait();
        }
    }
}
