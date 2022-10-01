import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        FileInteractor interactor = new FileInteractor("WritersDatabase.bin");

        Thread managerThread = new Thread(() -> {
            interactor.writeToFile(new Writer("Vasya", "380504440000"));
            interactor.writeToFile(new Writer("Petya", "380504441111"));
            interactor.writeToFile(new Writer("Vova", "380505550000"));
            interactor.writeToFile(new Writer("Grysha", "380505551111"));
            interactor.writeToFile(new Writer("Semen", "380501234567"));
            interactor.writeToFile(new Writer("Fedor", "380509871890"));
            interactor.writeToFile(new Writer("Ivan", "380090123455"));
            interactor.writeToFile(new Writer("Alexandr", "380965858444"));
            interactor.removeByKey("Fedor", FileInteractor.Field.NAME);
            interactor.removeByKey("Semen", FileInteractor.Field.NAME);
            interactor.removeByKey("Vova", FileInteractor.Field.NAME);
            interactor.removeByKey("380504441111", FileInteractor.Field.PHONE);
            interactor.writeToFile(new Writer("Anton", "45684968896846"));
            interactor.removeByKey("380965858444", FileInteractor.Field.PHONE);
            interactor.removeByKey("380090123455", FileInteractor.Field.PHONE);
            interactor.writeToFile(new Writer("Stepan", "5745754685468"));
        });
        managerThread.start();
        Thread.sleep(2);

        Thread nameFinderThread = new Thread(() -> {
            System.out.println("By name Vova: " + interactor.findInFile("Vova", FileInteractor.Field.NAME));
            System.out.println("By name Grysha: " + interactor.findInFile("Grysha", FileInteractor.Field.NAME));
            System.out.println("By name Fedor: " + interactor.findInFile("Fedor", FileInteractor.Field.NAME));
            System.out.println("By name Alexandr: " + interactor.findInFile("Alexandr", FileInteractor.Field.NAME));
        });

        Thread phoneFinderThread = new Thread(() -> {
            System.out.println("By phone 380504441111: " + interactor.findInFile("380504441111", FileInteractor.Field.PHONE));
            System.out.println("By phone 380504440000: " + interactor.findInFile("380504440000", FileInteractor.Field.PHONE));
            System.out.println("By phone 380509871890: " + interactor.findInFile("380509871890", FileInteractor.Field.PHONE));
            System.out.println("By phone 380090123455: " + interactor.findInFile("380090123455", FileInteractor.Field.PHONE));
        });

        nameFinderThread.start();
        phoneFinderThread.start();
    }
}