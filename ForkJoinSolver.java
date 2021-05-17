import java.util.concurrent.ForkJoinPool;
import java.util.Arrays;

public class ForkJoinSolver{

    Sudoku board;
    public static final int POOL_SIZE = 3;

    public ForkJoinSolver(Sudoku p) {
        this.board = p;
    }

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

    public static void main(String[] args) {
        int[][] puzzle = {
                {0,7,0,0,0,0,0,0,9},
                {5,1,0,4,2,0,6,0,0},
                {0,8,0,3,0,0,7,0,0},
                {0,0,8,0,0,1,3,7,0},
                {0,2,3,0,8,0,0,4,0},
                {4,0,0,9,0,0,1,0,0},
                {9,6,2,8,0,0,0,3,0},
                {0,0,0,0,1,0,4,0,0},
                {7,0,0,2,0,3,0,9,6}
        };

        int[][] nv = new int[puzzle.length][puzzle[0].length];
        for (int i = 0; i < nv.length; i++) nv[i] = Arrays.copyOf(puzzle[i], puzzle[i].length);
        Sudoku solveMe = new Sudoku(nv);
        ForkJoinSolver baseline = new ForkJoinSolver(solveMe);
        System.out.println("Puzzle to solve:");
        System.out.println(Arrays.deepToString(solveMe.puzzle) + "\n");

        long startBase = System.currentTimeMillis();

        boolean returned = baseline.solvePuzzle();
        System.out.println(returned);

        long endBase = System.currentTimeMillis();

        if(!returned){
            System.out.println("No Valid Solution Exists");
        }

        System.out.println("Baseline Solver finished in " + (endBase - startBase) + " ms.");
        System.out.print("Result:");
        System.out.println(Arrays.deepToString(baseline.board.puzzle) + "\n");

    }
}