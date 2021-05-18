import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;


public class SudokuTester {

    public static HashMap<String,Result> test(boolean emb, boolean prolog,boolean par, boolean fjp){
        
        int[][] puzzle = genGrid(9);

        HashMap<String,Result> results = new HashMap<String,Result>();
        results.put("Baseline",testBaseline(copyGrid(puzzle)));
        
        if(prolog){
          results.put("Prolog",testProlog(copyGrid(puzzle)));
        }
        if(emb){
          results.put("emb",testEmb(copyGrid(puzzle)));
        }
        if(par){
          results.put("Recursive",testRecursive(copyGrid(puzzle)));
        }
        if(fjp){
          results.put("fjp",testFJP(copyGrid(puzzle)));
        }
        return results;
    }


    public static int[][] genGrid(int size){
      int[][] toReturn = new int[size][size];
      int randomNum =0;
      int randVal =0;
      for (int i = 0; i < 7; i++){
        randomNum = ThreadLocalRandom.current().nextInt(0, size*size);
        randVal = ThreadLocalRandom.current().nextInt(1, size);
        

        if( new Sudoku(toReturn).isValid(randVal, ((randomNum/9)), randomNum%9)){
          toReturn[(randomNum/9)][randomNum%9]= randVal;
        }
      }
      return toReturn;
    }
    public static int[][] copyGrid(int[][] puzzle){
      int[][] nv = new int[puzzle.length][puzzle[0].length];
      for (int i = 0; i < nv.length; i++) nv[i] = Arrays.copyOf(puzzle[i], puzzle[i].length);
      return nv;
    }

    public static Result testBaseline( int[][] puzzle){
        Sudoku solveMe = new Sudoku(puzzle);
        SudokuSolverBaseline baseline = new SudokuSolverBaseline(solveMe);
        long startBase = System.nanoTime();
        boolean returned = baseline.solvePuzzle();
        long endBase = System.nanoTime();
        if(!returned){
            System.out.println("No Valid Solution Exists");
        }
        long baselineTime = (endBase - startBase)/1000;
        int[][] baselineArray = baseline.solveMe.puzzle;
      return new Result(baselineTime, baselineArray);
    }

    public static Result testProlog(int[][] puzzle){
      long ptime = 0;
        String prologArray = "";
        int[][] pArray = new int[puzzle.length][puzzle.length];
        try {
          ptime= PrologCommand.runProlog(puzzle);
            try{
              File f = new File("grid.txt");
              Scanner scanner = new Scanner(f);
              prologArray= scanner.next();
              scanner.close();
            
              int elems =0;
              for (char c :prologArray.toCharArray()){
                if(c != ']' & c!= '[' & c!= ','){
                  pArray[elems/9][elems%9]= Character.getNumericValue(c);
                }
              } 
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } 
          } catch (Exception e) {
              System.out.println("ERROR IN PROLOG CODE");
          }
      return new Result(ptime,pArray);
    }
    public static Result testFJP(int[][] puzzle){
      Sudoku solveMe3 = new Sudoku(puzzle);
      ForkJoinSolver forkJoinSolver = new ForkJoinSolver(solveMe3);

      long startBase3 = System.currentTimeMillis();
      forkJoinSolver.solvePuzzle();
      long endBase3 = System.currentTimeMillis();

      return new Result(endBase3 - startBase3,forkJoinSolver.board.puzzle);
    }

    public static Result testRecursive(int[][] puzzle){
      Sudoku solveMe2 = new Sudoku(puzzle);
      SudokuSolverParallelTasks parallelSolver = new SudokuSolverParallelTasks(solveMe2);
      long startBase2 = System.currentTimeMillis();
      parallelSolver.solvePuzzle();
      long endBase2 = System.currentTimeMillis();
      return new Result(endBase2 - startBase2,parallelSolver.solveMe.puzzle);
    }

    public static Result testEmb(int[][] puzzle){
      Sudoku solveMe2 = new Sudoku(puzzle);
      SudokuSolverEmb embSolver = new SudokuSolverEmb(solveMe2);
      long startBase2 = System.currentTimeMillis();
      embSolver.solvePuzzle();
      long endBase2 = System.currentTimeMillis();
      return new Result(endBase2 - startBase2,embSolver.solveMe.puzzle);

    }

}

