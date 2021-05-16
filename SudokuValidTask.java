public class SudokuValidTask implements Runnable {

    // Instance Variables
    private int[][] puzzle;
    private int toAdd;
    private int row;
    private int col;
    private boolean[] results;
    private Sudoku.Assignments assignment;

    /**
     * Constuctor for a SudokuValidTask
     * @param puzzle - the sudoku object that represents the board
     * @param toAdd - the value that is going to be added to the puzzle
     * @param row - the row that toAdd is going to be placed on the puzzle
     * @param col - the col that toAdd is going to be placed on the puzzle
     */
    public SudokuValidTask(int[][] puzzle, int toAdd, int row, int col, boolean[] results, Sudoku.Assignments assignment) {
        this.puzzle = puzzle;
        this.toAdd = toAdd;
        this.row = row;
        this.col = col;
        this.results = results;
        this.assignment = assignment;
    }
    
    @Override
    public void run() {
        switch (this.assignment) {
            case ROW:
                checkRow();
                break;
            case COL:
                checkCol();
                break;
            case BOX:
                checkBox();
                break;
            default:
                System.out.println("Error, valid check assignment invalid: " + this.assignment);
        }
    }

    /**
     * Checks the placement row for repeats of toAdd and writes result to array
     */
    public void checkRow() {
        // Check the row of the puzzle that is passed in when toAdd is added
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i][col] == toAdd) {
                results[0] = false;
                return;
            }
        }

        // Write true if there are no repeats
        results[0] = true;
    }

    /**
     * Checks the placement column for repeats of toAdd and writes result to array
     */
    public void checkCol() {
        // Check the column of the puzzle that is passed in when toAdd is added 
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[row][i] == toAdd) {
                results[1] = false;
                return;
            }
        }

        // Write true if there are no repeats
        results[1] = true;
    }

    /**
     * Checks the box that holds (row, col) for repeats of toAdd and writes result to array
     */
    public void checkBox() {
        // Check the sub-box for repeats
        int boxSize = (int) Math.sqrt(puzzle.length);
        int rowStartIdx = row - (row % boxSize);
        int colStartIdx = col - (col % boxSize);

        // Check the box that the value is being placed in
        for (int i = rowStartIdx; i < boxSize; i++) {
            for (int j = colStartIdx; j < boxSize; j++) {
                if (puzzle[i][j] == toAdd) {
                    results[2] = false;
                    break;
                }
            }
        }

        // Write true if there are no repeats
        results[2] = true;
    }
}
