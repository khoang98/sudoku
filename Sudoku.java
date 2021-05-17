import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sudoku{

    int[][] puzzle;

    public Sudoku(int[][] p){
        this.puzzle = p;
    }

    public enum Assignments {
        ROW, COL, BOX
    }

    boolean isValid(int toAdd, int row, int col){

        int puzzleSize = this.puzzle.length;

        // check row and col for repeat
        for(int i = 0; i < puzzleSize; i++){
            if(this.puzzle[row][i] == toAdd) return false;
            if(this.puzzle[i][col] == toAdd) return false;
        }

        // check smaller sub-boxes for repeat
        int boxSize = (int)Math.sqrt(puzzleSize);
        int rowStart = row - (row % boxSize);
        int colStart = col - (col % boxSize);

        for(int k = rowStart; k < rowStart + boxSize; k++){
            for(int l = colStart; l < colStart + boxSize; l++){
                if(this.puzzle[k][l] == toAdd) return false;
            }
        }

        return true;
    }

    boolean isValidParallel(int toAdd, int row, int col) {
        
        // Spin up a thread for a row check, column check, and box check. Also make shared DS
        // for each of the threads to communicate in
        Thread[] threads = new Thread[3];

        // Create a shared data structure that holds the booleans for each of the threads
        boolean[] results = new boolean[3];
        
        // Inialize each thread and give assignment
        threads[0] = new Thread(new SudokuValidTask(this.puzzle, toAdd, row, col, results, Assignments.ROW));
        threads[1] = new Thread(new SudokuValidTask(this.puzzle, toAdd, row, col, results, Assignments.COL));
        threads[2] = new Thread(new SudokuValidTask(this.puzzle, toAdd, row, col, results, Assignments.BOX));

        // Start each of the threads
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        // Wait for all the threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {}
        }

        // Check the results
        for (int i = 0; i < results.length; i++) {
            if (!results[i]) {
                return false;
            }
        }
        
        // if we have reached this point then we have all 3 trues, and we can return true 
        return true;
    }

}