import java.util.concurrent.Semaphore;

public class Main {
    private static final int BUS_AMOUNT = 5;

    private static final int MAX_BUS = 2;
    private static final Semaphore SEMAPHORE = new Semaphore(MAX_BUS, true);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < BUS_AMOUNT; i++) {
            new Thread(new Bus(i + 1)).start();
            Thread.sleep(1000); //Bus go to station
        }
    }

    public static class Bus implements Runnable {
        private int busNumber;

        Bus(int busNumber) {
            this.busNumber = busNumber;
        }

        @Override
        public void run() {
            try {
                SEMAPHORE.acquire();

                System.out.printf("Bus come to station.\n", busNumber);
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                throw new Error();
            }

            SEMAPHORE.release();
            System.out.printf("Bus leave station.\n", busNumber);
        }
    }
}