package main.java.com.hasik.multithreading.threads;

import main.java.com.hasik.multithreading.controller.Process;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Author : Hasmik.Ghazaryan
 * 7/20/2021
 */
public class Consumer implements Runnable {

    private final BlockingQueue<Integer> queue ;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(Process.running) {
            try {
                consume();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void consume() throws InterruptedException, IOException {
        synchronized(queue) {
            Thread.sleep(new Random().nextInt(100));
            while(queue.isEmpty()) {
                //System.out.println(Thread.currentThread().getName() + ": Queue is empty.");
                queue.notifyAll();
                queue.wait();
            }
            Files.write(Paths.get("data.txt"), (queue.take() + ",").getBytes(), StandardOpenOption.APPEND);
            queue.notifyAll();
        }
    }
}
