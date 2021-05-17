import java.util.concurrent.RecursiveTask;
import java.util.List;
import java.util.ArrayList;

public class ForkPuzzleTask extends RecursiveTask<Boolean>{

    Sudoku board;
    int row;
    int col;

    public ForkPuzzleTask(Sudoku p, int row, int col){
        this.board = p;
        this.row = row;
        this.col = col;
    }

    public int[][] cloneBoard() {
        int [][] newPuzzle = new int[this.board.puzzle.length][this.board.puzzle[0].length];
        for (int i = 0; i < this.board.puzzle.length; i++) {
            newPuzzle[i] = this.board.puzzle[i].clone();
        }
        return newPuzzle;
    }

    @Override
    protected Boolean compute() {
    	int puzzleSize = this.board.puzzle.length;
    	List<ForkPuzzleTask> tasks = new ArrayList<>();
    	int[][] puzzle = this.board.puzzle;

    	// already filled in
    	if (puzzle[this.row][this.col] != 0) {
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
        
        else{
            for(int option = 1; option <= puzzleSize; option++){
            	if (this.board.isValid(option, this.row, this.col)) {
            		int[][] newPuzzle = cloneBoard();
            		newPuzzle[this.row][this.col] = option;
            		Sudoku newBoard = new Sudoku(newPuzzle);

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
                if (solved) {
                    this.board = task.board;
                    return true;
                }

            }
            return false;
        }
    }

}