/**
  * Description: a class which solves sudoku using the same backtracking method as the baseline, but uses a parralell version of isValid. solvePuzzle returns a false if no valid puzzle exists
  */
public class SudokuSolverEmb {
    
    public Sudoku solveMe;

    public SudokuSolverEmb(Sudoku s){
        this.solveMe = s;
    }

    public boolean solvePuzzle(){

        int puzzleSize = this.solveMe.puzzle.length;
        for(int i = 0; i < puzzleSize; i++){
            for(int j = 0; j < puzzleSize; j++){

                if(this.solveMe.puzzle[i][j] == 0){
                    for(int option = 1; option <= puzzleSize; option++){
                        if(this.solveMe.isValidParallel(option, i, j)){
                            this.solveMe.puzzle[i][j] = option;
                            if(solvePuzzle()){
                                return true;
                            }
                            else this.solveMe.puzzle[i][j] = 0;
                        }  
                    }
                    // at this point it could not be valid so backtrack
                    return false;
                }
            }
        }
        return true;
    }
}
