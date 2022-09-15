
public class Main {
    static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) {
        ForestScheduler scheduler = new ForestScheduler(100, 10);
        Thread[] threads = startThreads(scheduler);

        for(Thread t : threads) {
            t.start();
        }
    }

    private static Thread[] startThreads(ForestScheduler scheduler) {
        Thread[] threads = new Thread[NUMBER_OF_THREADS];

        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            threads[i] = new Thread(new BeeHive(scheduler));
            threads[i].setName("BeeHive " + i);
        }

        return threads;
    }
}