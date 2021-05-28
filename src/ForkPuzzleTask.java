import java.util.concurrent.RecursiveTask;
import java.util.List;
import java.util.ArrayList;
/*
    Class for the tasks run by the fork-join solver.
*/
public class ForkPuzzleTask extends RecursiveTask<Boolean>{

    Sudoku board;
    int row;
    int col;

    public ForkPuzzleTask(Sudoku p, int row, int col){
        this.board = p;
        this.row = row;
        this.col = col;
    }

    //method to clone the sudoku board
    public int[][] cloneBoard() {
        int [][] newPuzzle = new int[this.board.puzzle.length][this.board.puzzle[0].length];
        for (int i = 0; i < this.board.puzzle.length; i++) {
            newPuzzle[i] = this.board.puzzle[i].clone();
        }
        return newPuzzle;
    }

    //this method fills in the (i,j) spot in the board with every available option and then forks new tasks
    //return true if it has solved the board
    @Override
    protected Boolean compute() {
    	int puzzleSize = this.board.puzzle.length;
    	List<ForkPuzzleTask> tasks = new ArrayList<>();
    	int[][] puzzle = this.board.puzzle;

    	// the (i,j) spot is already filled in
    	if (puzzle[this.row][this.col] != 0) {
            // try to fill in the (i, j+ 1) spot or (i+1, 0) spot 
    		if (this.col + 1 < puzzle[0].length) {
    			ForkPuzzleTask newTask = new ForkPuzzleTask(this.board, this.row, this.col + 1);
    			boolean solved = newTask.compute();
    			if (solved) {
    				this.board = newTask.board;
    			}
    			return solved;
    		}
    		else if (this.row + 1 < puzzle.length) {
    			ForkPuzzleTask newTask = new ForkPuzzleTask(this.board, this.row + 1, 0);
    			boolean solved = newTask.compute();
    			if (solved) {
    				this.board = newTask.board;
    			}
    			return solved;
    		}
    		// base case
    		else {
    			return true;
    		}

    	}
        // (i,j) spot is empty. Put every available option there and fork
        else{
            for(int option = 1; option <= puzzleSize; option++){
                // if this option is valid
            	if (this.board.isValid(option, this.row, this.col)) {
            		int[][] newPuzzle = cloneBoard();
            		newPuzzle[this.row][this.col] = option;
            		Sudoku newBoard = new Sudoku(newPuzzle);
                    //place option in (i,j) spot and create a task for (i, j+ 1) spot or (i+1, 0) spot 
            		if (this.col + 1 < puzzle[0].length) {
    					ForkPuzzleTask newTask = new ForkPuzzleTask(newBoard, this.row, this.col + 1);
    					tasks.add(newTask);
    				}
    				else if (this.row + 1 < puzzle.length) {
    					ForkPuzzleTask newTask = new ForkPuzzleTask(newBoard, this.row + 1, 0);
    					tasks.add(newTask);
    				}
    				//base case
    				else {
    					this.board.puzzle = newPuzzle;
    					return true;
    				}
            	}
            }
            //forking on all possible options
            for (ForkPuzzleTask task : tasks) {
            	task.fork();
            }
            for (ForkPuzzleTask task : tasks) {
                Boolean solved = task.join();
                //if one task has a solved board return true and pass the board back up
                if (solved) {
                    this.board = task.board;
                    return true;
                }

            }
            return false;
        }
    }

}