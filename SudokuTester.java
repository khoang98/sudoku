import java.util.Arrays;

public class SudokuTester {
    public static final boolean PROLOG = false;


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
        SudokuSolverBaseline baseline = new SudokuSolverBaseline(solveMe);
        System.out.println("Puzzle to solve:");
        System.out.println(Arrays.deepToString(solveMe.puzzle) + "\n");

        long startBase = System.currentTimeMillis();

        boolean returned = baseline.solvePuzzle();

        long endBase = System.currentTimeMillis();

        if(!returned){
            System.out.println("No Valid Solution Exists");
        }

        System.out.println("Baseline Solver finished in " + (endBase - startBase) + " ms.");
        System.out.print("Result:");
        System.out.println(Arrays.deepToString(baseline.solveMe.puzzle) + "\n");

        int[][] nv2 = new int[puzzle.length][puzzle[0].length];
        for (int i = 0; i < nv2.length; i++) nv2[i] = Arrays.copyOf(puzzle[i], puzzle[i].length);

        Sudoku solveMe2 = new Sudoku(nv2);
        SudokuSolverParallelTasks parallelSolver = new SudokuSolverParallelTasks(solveMe2);

        long startBase2 = System.currentTimeMillis();

        parallelSolver.solvePuzzle();

        long endBase2 = System.currentTimeMillis();

        System.out.println("Parallel Task Solver finished in " + (endBase2 - startBase2) + " ms.");
        System.out.print("Result:");
        System.out.println(Arrays.deepToString(parallelSolver.solveMe.puzzle) + "\n");


        if (PROLOG){
          try {
            long ptime = PrologCommand.runProlog(puzzle);
            System.out.println("Prolog Solver finished in " + ptime + " ms.");

          } catch (Exception e) {
              System.out.println("ERROR IN PROLOG CODE");
          }
        }

    }
}
