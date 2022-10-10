import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ThreadsManager extends Thread {
    private StringWithCounting string;
    private final StringModifier modifier;
    private final CyclicBarrier barrier;
    private final CyclicBarrier gate;

    public ThreadsManager(CyclicBarrier gate, CyclicBarrier barrier) {
        this.modifier = new StringModifier();
        this.barrier = barrier;
        this.gate = gate;

        int length_bound = (int) 1e+4;
        initializeString(5 * length_bound, 10 * length_bound);
    }

    private void initializeString(int minLength, int maxLength) {
        Random random = new Random();
        int length = 10;

        char[] stringChars = new char[length];

        int randomChar;
        for (int i = 0; i < length; ++i) {
            randomChar = random.nextInt(4);
            switch (randomChar) {
                case 0 -> stringChars[i] = 'A';
                case 1 -> stringChars[i] = 'B';
                case 2 -> stringChars[i] = 'C';
                case 3 -> stringChars[i] = 'D';
            }
        }
        System.out.println(stringChars);

        string = new StringWithCounting(String.valueOf(stringChars));
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is waiting for other threads to begin simultaneously.");
        try {
            gate.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " has started running.");

        while (!isInterrupted()) {
            modifier.modifyString(string);
            modifier.tryToJoinTheBarrier(string, barrier);
        }
    }
}
