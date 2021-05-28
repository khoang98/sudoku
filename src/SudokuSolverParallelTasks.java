import java.util.List;
import java.util.ArrayList;
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

        Thread firstThread = new Thread(new SudokuSolverTask(pool, solveMe, solveMe, 0, 0));
        pool.execute(firstThread);
        
        // wait for pool to shutdown for method to terminate
        while(!pool.isShutdown());
    }

}

class SudokuSolverTask implements Runnable{

    ExecutorService threadPool;
    public Sudoku solveMe;
    public Sudoku toReturn;
    int currRow;
    int currCol;
    static volatile boolean solved = false;

    public SudokuSolverTask(ExecutorService t, Sudoku s, Sudoku tR, int r, int c){
        this.threadPool = t;
        this.solveMe = s;
        this.toReturn = tR;
        this.currRow = r;
        this.currCol = c;
    }

    public int[][] cloneBoard(){
        int[][] newPuzzle = new int[solveMe.puzzle.length][solveMe.puzzle[0].length];
        for (int i = 0; i < newPuzzle.length; i++) newPuzzle[i] = Arrays.copyOf(solveMe.puzzle[i], solveMe.puzzle[i].length);
        return newPuzzle;
    }

    @Override
    public void run(){
        int puzzleSize = this.solveMe.puzzle.length;
        List<Thread> tasks = new ArrayList<>();

        // the (i,j) spot is already filled in
        if(this.solveMe.puzzle[currRow][currCol] != 0){
            if(currCol + 1 < puzzleSize){
                Thread newThread = new Thread(new SudokuSolverTask(threadPool, solveMe, toReturn, currRow, currCol+1));
                tasks.add(newThread);
            }
            else if(currRow + 1 < puzzleSize){
                Thread newThread = new Thread(new SudokuSolverTask(threadPool, solveMe, toReturn, currRow+1, 0));
                tasks.add(newThread);
            }
            else{
                this.toReturn.puzzle = solveMe.puzzle;
                solved = true;
                threadPool.shutdownNow();
            }
        }
        else{
            for(int option = 1; option <= puzzleSize; option++){
                if(this.solveMe.isValid(option, currRow, currCol)){
                    int[][] newPuzzle = cloneBoard();
                    newPuzzle[currRow][currCol] = option;
                    Sudoku newBoard = new Sudoku(newPuzzle);
                    if(currCol + 1 < puzzleSize){
                        Thread newThread = new Thread(new SudokuSolverTask(threadPool, newBoard, toReturn, currRow, currCol+1));
                        tasks.add(newThread);
                    }
                    else if(currRow + 1 < puzzleSize){
                        Thread newThread = new Thread(new SudokuSolverTask(threadPool, newBoard, toReturn, currRow+1, 0));
                        tasks.add(newThread);
                    }
                    else{
                        this.toReturn.puzzle = newPuzzle;
                        solved = true;
                        threadPool.shutdownNow();
                    }
                }
            }
        }
        for(Thread t : tasks){
            if(!solved && !threadPool.isShutdown()){
                threadPool.execute(t);
            }    
        }
    }
}
