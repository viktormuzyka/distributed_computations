import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) {
        new Barbershop().start();
    }
}
class Consumer implements Runnable {
    private final Barbershop barbershop;
    private final long frequency;
    private final int id;

    public Consumer(Barbershop barbershop, long frequency, int id) {
        this.barbershop = barbershop;
        this.frequency = frequency;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        while(true) {
            try {
                sleep(frequency);

                int currentCount = barbershop.customersCount();

                if(currentCount > 0) {
                    System.out.printf("Customer %d fell asleep... He is %d in queue%n", id, currentCount + 1);
                } else {
                    System.out.printf("Customer %d is waiting his turn. He is %d in queue%n", id, currentCount + 1);
                }

                barbershop.addCustomer(this);

                synchronized (this) {
                    wait();
                }

                System.out.printf("Customer %d will come back in %d ms%n", id, frequency);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
class Barbershop {
    private BlockingQueue<Consumer> consumers;

    public Barbershop() {
        this.consumers = new LinkedBlockingQueue<>();
    }

    public void start() {
        Thread[] threads = new Thread[5];

        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Consumer(this, 1000 * (i + 1) / threads.length, i));
        }

        Thread barber = new Thread(new Barber(this));

        for(Thread thread : threads) {
            thread.start();
        }

        barber.start();
    }

    public void addCustomer(Consumer consumer) throws InterruptedException {
        consumers.put(consumer);
    }

    public Consumer takeCustomer() throws InterruptedException {
        return consumers.take();
    }

    public int customersCount() {
        return consumers.size();
    }
}
class Barber implements Runnable {
    Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(barbershop.customersCount() == 0) {
                    System.out.println("Barber is sleeping...");
                }

                Consumer currentConsumer = barbershop.takeCustomer();
                System.out.printf("Barber started cutting off %d%n", currentConsumer.getId());

                sleep(1000);

                System.out.printf("Barber finished cutting off %d%n", currentConsumer.getId());
                synchronized (currentConsumer) {
                    currentConsumer.notifyAll();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}