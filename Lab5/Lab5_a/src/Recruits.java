import java.util.Random;

public class Recruits {
    private int numberOfRecruits;
    private Boolean[] recruits; //false = left, true = right
    private Boolean[] previousState;

    public Recruits(int numberOfRecruits) {
        this.numberOfRecruits = numberOfRecruits;
        this.recruits = new Boolean[numberOfRecruits];
        this.previousState = new Boolean[numberOfRecruits];
        initialStateOfRecruits();
        setPreviousState();
    }

    public void setPreviousState() {
        for(int i = 0;i<numberOfRecruits;++i){
            previousState[i] = recruits[i];
        }
    }

    public Boolean[] getRecruits() {
        return recruits;
    }

    public void initialStateOfRecruits() {
        Random random = new Random();
        for (int i = 0; i < numberOfRecruits; ++i) {
            recruits[i] = random.nextBoolean();
        }
    }

    public void printRecruits() {
        System.out.print("[ ");
        for (int i = 0; i < numberOfRecruits; ++i) {
            System.out.print(recruits[i] ? ">" : "<");
        }
        System.out.println(" ]");
    }

    //End index excluding
    public void partFixing(int begin, int end) {
        for (int i = begin; i < end - 1; ++i) {
            if (recruits[i] && !recruits[i + 1]) {
                recruits[i] = false;
                recruits[i + 1] = true;
            }
        }
        if (end != recruits.length) {
            if (recruits[end - 1] && !recruits[end]) {
                recruits[end - 1] = false;
                synchronized (recruits[end]){
                    recruits[end] = true;
                }
            }
        }
    }

    public synchronized boolean isStationary() {
        for(int i = 0;i<numberOfRecruits;++i){
            if(recruits[i] != previousState[i]){
                return false;
            }
        }
        return true;
    }
}