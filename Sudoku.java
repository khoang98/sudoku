public class Sudoku{

    int[][] puzzle;

    public Sudoku(int[][] p){
        this.puzzle = p;
    }

    boolean isValid(int toAdd, int row, int col){

        int puzzleSize = this.puzzle.length;

        // check row and col for repeat
        for(int i = 0; i < puzzleSize; i++){
            if(this.puzzle[row][i] == toAdd) return false;
            if(this.puzzle[i][col] == toAdd) return false;
        }

        // check smaller sub-boxes for repeat
        int boxSize = (int)Math.sqrt(puzzleSize);
        int rowStart = row - (row % boxSize);
        int colStart = col - (col % boxSize);

        for(int k = rowStart; k < rowStart + boxSize; k++){
            for(int l = colStart; l < colStart + boxSize; l++){
                if(this.puzzle[k][l] == toAdd) return false;
            }
        }

        return true;
    }

}