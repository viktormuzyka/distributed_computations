public class RecruitsManager extends Thread {
    private int beginIndex, endIndex;
    private Recruits recruits;
    private ThreadManager manager;

    public RecruitsManager(int beginIndex, int endIndex, Recruits recruits, ThreadManager manager) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.recruits = recruits;
        this.manager = manager;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            recruits.partFixing(beginIndex, endIndex);
            System.out.println(getName()+" has reached barrier!");
            manager.incrementReachedBarrier();
        }
    }
}