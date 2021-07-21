package main.java.com.hasik.multithreading.controller;

import main.java.com.hasik.multithreading.threads.Consumer;
import main.java.com.hasik.multithreading.threads.Printer;
import main.java.com.hasik.multithreading.threads.Producer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author : Hasmik.Ghazaryan
 * 7/20/2021
 */
public class Process {

    public static final int QUEUE_SIZE = 101;
    public static boolean running = true;
    private final int numProducer;
    private final int numConsumer;
    private final List<Producer> producers = new ArrayList<>();
    private final List<Consumer> consumers = new ArrayList<>();
    private final BlockingQueue<Integer> queue;


    public Process(int numProducer, int numConsumer) {
        this.numProducer = numProducer;
        this.numConsumer = numConsumer;
        this.queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        for (int i = 0; i < numConsumer; i++) {
            consumers.add(new Consumer(queue));
        }
        for (int i = 0; i < numProducer; i++) {
            producers.add(new Producer(queue));
        }
    }

    public void execute(){
        final ExecutorService executor = Executors.newFixedThreadPool(numConsumer + numProducer + 2);
        consumers.forEach(executor::submit);
        producers.forEach(executor::submit);
        executor.submit(new Printer(queue));
        executor.submit(() -> {
            // TODO: to be implemented
            //stop(executor);
        });
    }

    // To be called later from UI or maybe read an input from console, with a separate thread?
    public void stop (ExecutorService executor) {
        running = false;
        executor.shutdown();
        System.exit(0);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of producers:");
        int n = sc.nextInt();
        System.out.println("Enter number of consumers:");
        int m = sc.nextInt();
        sc.close();
        File file = new File("data.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Process process = new Process(n, m);
        process.execute();
    }
}