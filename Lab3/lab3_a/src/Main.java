import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) {
        Bucket pot = new Bucket(5); //set capacity
        Bear bear = new Bear(pot);

        Thread bearThread = new Thread(bear);
        Thread[] bees = new Thread[10]; //numbers of bees

        for(int i = 0; i < bees.length; i++) {
            bees[i] = new Thread(new Bee(pot));
        }

        bearThread.start();

        for(Thread thread : bees) {
            thread.start();
        }
    }
}
class Bucket {
    private final int maxCapacity;
    private int currCapacity;
    final Object fullMonitor = new Object();
    final Object emptyMonitor = new Object();

    public Bucket(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currCapacity = 0;
    }

    public void waitFull() {
        synchronized (fullMonitor) {
            try {
                fullMonitor.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyFull() {
        synchronized (fullMonitor) {
            fullMonitor.notifyAll();
        }
    }

    public void waitEmpty() {
        synchronized (emptyMonitor) {
            try {
                emptyMonitor.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyEmpty() {
        synchronized (emptyMonitor) {
            emptyMonitor.notifyAll();
        }
    }

    public void fill() {
        if(checkAndFill()) {
            System.out.println("Bee is waiting");
            waitEmpty();
            fill();
        }
    }

    private synchronized boolean checkAndFill() {
        if(!isFull()) {
            System.out.print("Current num of honey:");
            System.out.println(++currCapacity);

            if(isFull()) {
                notifyFull();
            }
        } else {
            return true;
        }

        return false;
    }

    private boolean isFull() {
        return currCapacity == maxCapacity;
    }

    public void eatHoney() {
        try {
            sleep((long)(1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        synchronized (this) {
            currCapacity = 0;
        }

        notifyEmpty();
    }
}
class Bee implements Runnable {
    private final Bucket bucket;
    public Bee(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000);
                bucket.fill();
                System.out.printf("Bee with id %d filled pot%n", Thread.currentThread().getId());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
class Bear implements Runnable {
    private final Bucket pot;

    public Bear(Bucket pot) {
        this.pot = pot;
    }

    @Override
    public void run() {
        while(true) {
            pot.waitFull();
            System.out.println("Bear woke up");
            pot.eatHoney();
            System.out.println("Bear ate honey");
        }
    }
}