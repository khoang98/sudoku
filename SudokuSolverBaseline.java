import java.util.Arrays;

public class SudokuSolverBaseline {

    private Sudoku solveMe;

    public SudokuSolverBaseline(Sudoku s){
        this.solveMe = s;
    }

    void solvePuzzle(){

        int puzzleSize = this.solveMe.puzzle.length;
        for(int i = 0; i < puzzleSize; i++){
            for(int j = 0; j < puzzleSize; j++){

                if(this.solveMe.puzzle[i][j] == 0){
                    for(int option = 1; option <= puzzleSize; option++){


                        if(this.solveMe.isValid(option, i, j)){
                            this.solveMe.puzzle[i][j] = option;
                            solvePuzzle();
                        }

                        // at this point it could not be valid so backtrack
                        this.solveMe.puzzle[i][j] = 0;
                    }

                    // this is to prevent stackOverflow
                    return;
                }
            }
        }

        // store the solution so we can access later
        System.out.println("Solved Puzzle:");
        System.out.println(Arrays.deepToString(this.solveMe.puzzle) + "\n");
        return;
    }

}
