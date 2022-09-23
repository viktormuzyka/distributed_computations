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
    private final Object monitoringFull = new Object();
    private final Object monitoringEmpty = new Object();

    public Bucket(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currCapacity = 0;
    }

    public void waitOnFull() {
        synchronized (monitoringFull) {
            try {
                monitoringFull.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyOnFull() {
        synchronized (monitoringFull) {
            monitoringFull.notifyAll();
        }
    }

    public void waitOnEmpty() {
        synchronized (monitoringEmpty) {
            try {
                monitoringEmpty.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyOnEmpty() {
        synchronized (monitoringEmpty) {
            monitoringEmpty.notifyAll();
        }
    }

    public void fill() {
        if(checkAndFill()) {
            System.out.println("Bee is waiting");
            waitOnEmpty();
            fill();
        }
    }

    private synchronized boolean checkAndFill() {
        if(!isFull()) {
            System.out.print("Current num of honey:");
            System.out.println(++currCapacity);

            if(isFull()) {
                notifyOnFull();
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

        notifyOnEmpty();
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
            pot.waitOnFull();
            System.out.println("Bear woke up");
            pot.eatHoney();
            System.out.println("Bear ate honey");
        }
    }
}