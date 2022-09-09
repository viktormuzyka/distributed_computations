import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final JSlider slider = new JSlider();
    private static AtomicInteger semaphore = new AtomicInteger(0);
    private static TThread thread1;
    private static TThread thread2;
    private static void setFrame() {
        JFrame frame = new JFrame("Main");

        slider.setBounds(40, 40 ,420, 40);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(10);


        JButton buttonStartFirstThread = new JButton("Start 1");
        buttonStartFirstThread.setBounds(60, 150,100, 50);

        JButton buttonStopFirstThread = new JButton("Stop 1");
        buttonStopFirstThread.setBounds(60, 210,100, 50);
        buttonStopFirstThread.setEnabled(false);

        JButton buttonStartSecondThread = new JButton("Start 2");
        buttonStartSecondThread.setBounds(335, 150,100, 50);

        JButton buttonStopSecondThread = new JButton("Stop 2");
        buttonStopSecondThread.setBounds(335, 210,100, 50);
        buttonStopSecondThread.setEnabled(false);

        JLabel label = new JLabel("Critical region is occupied!");
        label.setBounds(170, 300, 150,15);
        label.setVisible(false);

        buttonStartFirstThread.addActionListener(e -> {
            buttonStopFirstThread.setEnabled(true);
            buttonStartFirstThread.setEnabled(false);
            buttonStartSecondThread.setEnabled(false);
            buttonStopSecondThread.setEnabled(false);
            label.setVisible(true);

            thread1 = new TThread(slider, -1, semaphore);
            thread1.start();
        });

        buttonStartSecondThread.addActionListener(e -> {
            buttonStopSecondThread.setEnabled(true);
            buttonStartSecondThread.setEnabled(false);
            buttonStartFirstThread.setEnabled(false);
            buttonStopFirstThread.setEnabled(false);
            label.setVisible(true);

            thread2 = new TThread(slider, 1, semaphore);
            thread2.start();
        });

        buttonStopFirstThread.addActionListener(e -> {
            buttonStopFirstThread.setEnabled(false);
            buttonStartFirstThread.setEnabled(true);
            buttonStartSecondThread.setEnabled(true);
            label.setVisible(false);

            thread1.interrupt();
        });

        buttonStopSecondThread.addActionListener(e -> {
            buttonStopSecondThread.setEnabled(false);
            buttonStartFirstThread.setEnabled(true);
            buttonStartSecondThread.setEnabled(true);
            label.setVisible(false);

            thread2.interrupt();
        });

        frame.add(slider);
        frame.add(buttonStartFirstThread);
        frame.add(buttonStopFirstThread);
        frame.add(buttonStartSecondThread);
        frame.add(buttonStopSecondThread);
        frame.add(label);

        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        setFrame();
    }
}
class TThread extends Thread {
    private final JSlider slider;
    private final int step;
    private final AtomicInteger semaphore;
    public TThread(JSlider slider, int step, AtomicInteger semaphore) {
        this.slider = slider;
        this.step = step;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        if (!semaphore.compareAndSet(0, 1)){
            return;
        }
        boolean flag = (slider.getValue() == 10 && step > 0) || (slider.getValue() == 90 && step<0);

        while(!isInterrupted()) {
            try {
                sleep(200);
                int sliderValue = slider.getValue();
                if ((sliderValue > 10 && sliderValue < 90) || flag) {
                    slider.setValue(sliderValue + step);
                }
            } catch (InterruptedException e) {
                //interrupt();
                System.out.println("Interrupted thread with id : " + getId());
                break;
            }
        }
        semaphore.set(0);
    }
}