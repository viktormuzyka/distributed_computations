public class ForestScheduler {
    private final boolean[][] cells;
    private final int rows;
    private final int columns;
    private boolean bearFound;
    private int rowToPlan; //row for bee

    //make forest
    public ForestScheduler(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.rowToPlan = 0;
        this.bearFound = false;
        this.cells = new boolean[rows][columns];
        randomizePositionWinnieThePooh();
    }

    //set position for WinnieThePooh
    public void randomizePositionWinnieThePooh(){
        int randomRow = (int)(Math.random() * (rows - 1));
        int randomColumn = (int)(Math.random() * (columns - 1));
        cells[randomRow][randomColumn] = true;
    }

    public void notifyAboutBear() {
        bearFound = true;
    }

    public synchronized int getTask() {
        return bearFound ? -1 : ++rowToPlan % rows;
    }

    public boolean[] getRow(int i){
        return cells[i];
    }

    public int getRowsCount() {
        return rows;
    }
}
