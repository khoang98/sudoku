import java.util.concurrent.ForkJoinPool;
import java.util.Arrays;
/*
    Class to solve a sudoku board using fork join pools to parallelize the search
*/
public class ForkJoinSolver{

    Sudoku board;
    public static final int POOL_SIZE = 3;

    public ForkJoinSolver(Sudoku p) {
        this.board = p;
    }

    //method to solve the given sudoku board
    //returns true if solved, false if not
    public boolean solvePuzzle(){
        ForkJoinPool pool = new ForkJoinPool(POOL_SIZE);
        int puzzleSize = this.board.puzzle.length;
        ForkPuzzleTask task = new ForkPuzzleTask(this.board, 0, 0);
        boolean solved = task.compute();
        if (solved) {
            this.board = task.board;
        }
        return solved;
    }
}