import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class Main {
    private static ArrayList<Thread> threads = new ArrayList<>();
    final private static CyclicBarrier gate = new CyclicBarrier(5);

    public static void main(String[] args) {
        final CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("\n3 threads have equal A and B symbols number");

            for (Thread thread : threads) {
                thread.interrupt();
            }

            System.out.println();
        });
        initializeThreads(barrier);
    }

    public static void initializeThreads(CyclicBarrier barrier) {
        IntStream.range(0, 4).forEach(i -> {
            threads.add(new ThreadsManager(gate, barrier));
            threads.get(i).start();
        });

        try {
            gate.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
class StringModifier {
    public synchronized void modifyString(StringWithCounting string) {
        Random random = new Random();

        for (int i = 0; i < string.getString().length(); ++i) {
            if (string.areABEqual()) {
                break;
            }
            switch (string.getCharAtPos(i)) {
                case 'A' -> {
                    if (random.nextBoolean()) {
                        string.setCharAtPos('C', i);
                    }
                }
                case 'B' -> {
                    if (random.nextBoolean()) {
                        string.setCharAtPos('D', i);
                    }
                }
                case 'C' -> {
                    if (random.nextBoolean()) {
                        string.setCharAtPos('A', i);
                    }
                }
                case 'D' -> {
                    if (random.nextBoolean()) {
                        string.setCharAtPos('B', i);
                    }
                }
            }
        }
    }

    public synchronized void tryToJoinTheBarrier(StringWithCounting string, CyclicBarrier barrier) {
        try {
            if (string.areABEqual() && barrier.getParties() != barrier.getNumberWaiting()) {
                System.out.println("\n" + Thread.currentThread().getName() + " has reached barrier.\nnumberA = " +
                        string.getNumberA() + "; numberB = " +
                        string.getNumberB() + "\n" +
                        "waiting: " + barrier.getNumberWaiting());

                barrier.await();
                System.out.println(Thread.currentThread().getName() + " has finished its work.");
            }

        } catch (InterruptedException |
                 BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }
}
class StringWithCounting {
    private String string;
    private int numberA = 0;
    private int numberB = 0;

    StringWithCounting(String string) {
        this.string = string;

        setNumbers();
    }

    public boolean areABEqual() {
        return numberA == numberB;
    }

    public void setCharAtPos(char ch, int index) {
        char[] charArray = string.toCharArray();

        if (charArray[index] == 'A')
            numberA--;
        else if (charArray[index] == 'B')
            numberB--;

        if (ch == 'A')
            numberA++;
        else if (ch == 'B')
            numberB++;

        charArray[index] = ch;
        string = String.valueOf(charArray);
    }

    public char getCharAtPos(int index) {
        return string.toCharArray()[index];
    }

    private void setNumbers() {
        numberA = numberB = 0;

        for (char ch : string.toCharArray()) {
            if (ch == 'A') numberA++;
            else if (ch == 'B') numberB++;
        }
    }

    public int getNumberA() {
        return numberA;
    }

    public int getNumberB() {
        return numberB;
    }

    public String getString() {
        return string;
    }
}