import javax.swing.*;

public class Main {
    private static final JSlider slider = new JSlider();
    private static TThread thread1 = new TThread(slider, -1);
    private static TThread thread2 = new TThread(slider, 1);
    private static void setFrame() {
        JFrame frame = new JFrame("Main");

        slider.setBounds(40, 40 ,420, 40);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(10);

        JButton buttonStart = new JButton("Start");
        buttonStart.setBounds(200, 250,100, 50);
        buttonStart.addActionListener(e -> {
            thread1.start();
            thread2.start();
            buttonStart.setEnabled(false);
        });

        SpinnerModel firstSpinnerModel = new SpinnerNumberModel(0, 0, 5, 1);
        JSpinner firstSpinner = new JSpinner(firstSpinnerModel);
        firstSpinner.setBounds(40, 130, 150, 80);
        firstSpinner.addChangeListener(e -> {
            int priority = (int) firstSpinner.getValue();
            thread1.setPriority(priority);
        });

        SpinnerModel secondSpinnerModel = new SpinnerNumberModel(0, 0, 5, 1);
        JSpinner secondSpinner = new JSpinner(secondSpinnerModel);
        secondSpinner.setBounds(300, 130, 150, 80);
        secondSpinner.addChangeListener(e -> {
            int priority = (int) secondSpinner.getValue();
            thread2.setPriority(priority);
        });

        frame.add(slider);
        frame.add(buttonStart);
        frame.add(firstSpinner);
        frame.add(secondSpinner);

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
    public TThread(JSlider slider, int step) {
        this.slider = slider;
        this.step = step;
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            synchronized (slider) {
                try {
                    sleep(100);
                    int sliderValue = slider.getValue();
                    if(sliderValue > 10 && sliderValue < 90) {
                        slider.setValue(sliderValue + step);
                    } else {
                        interrupt();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interrupted thread with id : " + getId());
                }
            }
        }
        System.out.println("Finished thread with id : " + getId());
    }
}
