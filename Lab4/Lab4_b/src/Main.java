import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Garden {
    private Boolean[][] conditions;
    private int length, width;
    public String fileName = "GardenConditionsDatabase.bin";
    private BufferedWriter output;
    private int counter = 0;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public Garden(int length, int width) {
        this.length = length;
        this.width = width;
        conditions = new Boolean[length][width];
        motherNature();
    }

    public void motherNature() {
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < width; ++j) {
                w.lock();
                r.lock();
                try {
                    conditions[i][j] = random.nextBoolean();
                } finally {
                    w.unlock();
                    r.unlock();
                }
            }
        }
    }

    public void printGarden() {
        r.lock();
        try {
            for (Boolean[] row : conditions) {
                System.out.print("| ");
                for (boolean element : row) {
                    System.out.print((element ? 1 : 0) + " ");
                }
                System.out.println("|");
            }
        } finally {
            r.unlock();
        }
        System.out.println();
    }

    public void pourPlants() {
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < width; ++j) {
                synchronized (conditions[i][j]) {
                    if (!conditions[i][j]){
                        w.lock();
                        try {
                            conditions[i][j] = true;
                        } finally {
                            w.unlock();
                        }
                    }
                }
            }
        }
    }

    public void writeToFile() {
        try {
            output = new BufferedWriter(new FileWriter(fileName, true));
            output.write("Garden condition #" + counter++ + ":\n");
            r.lock();
            try {
                for (Boolean[] row : conditions) {
                    output.write("| ");
                    for (boolean element : row) {
                        output.write((element ? 1 : 0) + " ");
                    }
                    output.write("|\n");
                }
                output.write("\n");
                output.close();
            } finally {
                r.unlock();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearFile() {
        try {
            output = new BufferedWriter(new FileWriter(fileName));
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Garden garden = new Garden(5, 5);
        garden.clearFile();

        Thread gardener = new Thread(() -> {
            for(int i = 0; i < 5; ++i){
                garden.pourPlants();
                System.out.println("Garden has been poured.\n");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread nature = new Thread(() -> {
            for(int i = 0; i < 10; ++i){
                garden.motherNature();
                System.out.println("Garden has been changed by Mother Nature.\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread monitor1 = new Thread(() -> {
            for(int i = 0; i < 5; ++i){
                garden.writeToFile();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread monitor2 = new Thread(() -> {
            for(int i = 0; i < 5; ++i){
                garden.printGarden();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        gardener.start();
        nature.start();
        monitor1.start();
        monitor2.start();

        gardener.join();
        nature.join();
        monitor1.join();
        monitor2.join();
    }
}