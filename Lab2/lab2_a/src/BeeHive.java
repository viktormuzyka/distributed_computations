public class BeeHive implements Runnable {
    private final ForestScheduler scheduler;

    public BeeHive(ForestScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while (true) {
            int rowToCheck = scheduler.getTask();

            if (rowToCheck == -1) {
                System.out.println(Thread.currentThread().getName() + " finished");
                break;
            }
            System.out.println(Thread.currentThread().getName() + " started checking row " + rowToCheck);

            boolean[] row = scheduler.getRow(rowToCheck);
            for (int i = 0; i < row.length; ++i) {
                if (row[i]) {
                    System.out.println(Thread.currentThread().getName() + " found bear at " + rowToCheck + " pos " + i);
                    scheduler.notifyAboutBear();
                    break;
                }
            }
            System.out.println(Thread.currentThread().getName() + " checked row " + rowToCheck);
        }
    }
}