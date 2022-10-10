import java.util.ArrayList;

public class ThreadManager {
    private final int numberOfThreads;
    private ArrayList<Thread> threads;

    //number of threads that reached barrier on current iteration
    private int reachedBarrier;

    private Recruits recruits;

    public ThreadManager(Recruits recruits, int minimumNumberPerThread) {
        int recruitsNumber = recruits.getRecruits().length;
        this.numberOfThreads = recruitsNumber / minimumNumberPerThread;
        this.reachedBarrier = 0;
        this.recruits = recruits;
        int numberPerThread = (int) Math.ceil((double) recruitsNumber / numberOfThreads);
        this.threads = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; ++i) {
            int beginIndex = numberPerThread * i;
            int endIndex = Math.min(numberPerThread + beginIndex, recruitsNumber);
            Thread thread = new RecruitsManager(beginIndex, endIndex, recruits, this);
            threads.add(thread);
            thread.start();
        }
    }

    public synchronized void incrementReachedBarrier() {
        reachedBarrier++;
        if (reachedBarrier == numberOfThreads) {
            recruits.printRecruits();
            if (!recruits.isStationary()) {
                reachedBarrier = 0;
                recruits.setPreviousState();
                notifyAll();
            } else {
                System.out.println("Stationary state has been reached!");
                for (Thread thread : threads) {
                    thread.interrupt();
                }
            }
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}