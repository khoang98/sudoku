import java.util.Arrays;

public class SudokuTester {
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

        Sudoku solveMe = new Sudoku(puzzle);
        SudokuSolverBaseline baseline = new SudokuSolverBaseline(solveMe);
        System.out.println("Puzzle to solve:");
        System.out.println(Arrays.deepToString(solveMe.puzzle) + "\n");

        long startBase = System.nanoTime();

        baseline.solvePuzzle();

        long endBase = System.nanoTime();

        System.out.println("Baseline Solver finished in " + (endBase - startBase)/1000 + " ms.");
    }
}
