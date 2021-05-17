import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SudokuSolverParallelTasks{

    public Sudoku solveMe;

    public SudokuSolverParallelTasks(Sudoku s){ this.solveMe = s; }

    public void solvePuzzle(){

        // create thread pool
        int nThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(nThreads);

        // find first blank space and create threads
        int puzzleSize = solveMe.puzzle.length;
        outerLoop:
        for(int r = 0; r < puzzleSize; r++){
            for(int c = 0; c < puzzleSize; c++){
                if(solveMe.puzzle[r][c] == 0){
                    for(int option = 1; option <= puzzleSize; option++){
                        int[][] nv = new int[puzzleSize][puzzleSize];
                        for (int i = 0; i < nv.length; i++) nv[i] = Arrays.copyOf(solveMe.puzzle[i], solveMe.puzzle[i].length);
                        nv[r][c] = option;
                        Thread first = new Thread(new SudokuSolverTask(pool,solveMe,nv,r,c));
                        if(!pool.isShutdown()) { pool.execute(first); }
                    }
                    // break out of double loop
                    break outerLoop;
                }
            }
        }
        // wait for pool to shutdown for method to terminate
        while(!pool.isShutdown());
    }

}

class SudokuSolverTask implements Runnable{

    ExecutorService threadPool;
    private Sudoku solveMe;
    private int[][] currPuzzle;
    int currRow;
    int currCol;
    static volatile boolean solved = false;

    public SudokuSolverTask(ExecutorService t, Sudoku s, int[][] cP, int r, int c){
        this.threadPool = t;
        this.solveMe = s;
        this.currPuzzle = cP;
        this.currRow = r;
        this.currCol = c;
    }

    public void run(){
        int puzzleSize = currPuzzle.length;
        if(!solved && !threadPool.isShutdown()){
            if(isValid(currPuzzle,currPuzzle[currRow][currCol],currRow,currCol)){
                // find next open spot and fill only that spot
                for(int r = 0; r < puzzleSize; r++){
                    for(int c = 0; c < puzzleSize; c++){
                        if(currPuzzle[r][c] == 0){
                            // found empty slot
                            for(int option = 1; option <= puzzleSize; option++){
                                int[][] nv = new int[currPuzzle.length][currPuzzle[0].length];
                                for (int i = 0; i < nv.length; i++) nv[i] = Arrays.copyOf(currPuzzle[i], currPuzzle[i].length);
                                nv[r][c] = option;
                                Thread newT = new Thread(new SudokuSolverTask(threadPool,solveMe,nv,r,c));
                                // check one more time to avoid rejected execution exception error
                                if(!solved && !threadPool.isShutdown()){
                                    threadPool.execute(newT);
                                }
                            }
                            return;
                        }
                    }
                }
                // at this point, the puzzle must be solved
                solveMe.puzzle = currPuzzle;
                solved = true;
                threadPool.shutdownNow();
            }
        }
    }

    boolean isValid(int[][] puzzle, int toAdd, int row, int col){

        int puzzleSize = puzzle.length;

        // check row and col for repeat
        for(int i = 0; i < puzzleSize; i++){
            if(puzzle[row][i] == toAdd && (i!=col)) return false;
            if(puzzle[i][col] == toAdd && (i!=row)) return false;
        }

        // check smaller sub-boxes for repeat
        int boxSize = (int)Math.sqrt(puzzleSize);
        int rowStart = row - (row % boxSize);
        int colStart = col - (col % boxSize);

        for(int k = rowStart; k < rowStart + boxSize; k++){
            for(int l = colStart; l < colStart + boxSize; l++){
                if(puzzle[k][l] == toAdd && (k!=row) && (l!=col)) return false;
            }
        }

        return true;
    }
}
