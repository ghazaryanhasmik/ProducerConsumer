package main.java.com.hasik.multithreading.threads;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

import static main.java.com.hasik.multithreading.controller.Process.QUEUE_SIZE;
import static main.java.com.hasik.multithreading.controller.Process.running;

/**
 * Author : Hasmik.Ghazaryan
 * 7/20/2021
 */
public class Producer implements Runnable {

    private boolean blocked = false;
    private final Random random = new Random();
    private final BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        synchronized (queue) {
            while (running) {
                try {
                    produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void produce() throws InterruptedException {
        synchronized (queue) {
            Thread.sleep(random.nextInt(100));
            if (queue.size() >= QUEUE_SIZE - 1) {
                //System.out.println(Thread.currentThread().getName() + " : Queue is overloaded");
                blocked = true;
                queue.notifyAll();
                queue.wait();
            }
            while (blocked && queue.size() >= 80) {
                //System.out.println(Thread.currentThread().getName() + " : Queue is still overloaded");
                queue.notifyAll();
                queue.wait();
            }
            blocked = false;
            int producedNumber = random.nextInt(100);
            //System.out.println("Producer: " + Thread.currentThread().getName() + " - "+ producedNumber);
            queue.add(producedNumber);
            queue.notifyAll();
        }
    }
}

