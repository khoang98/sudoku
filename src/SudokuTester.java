import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
  * Description: A class which handles the instanciation, execution, and gathering of results for each of the tests
  *@param: emb: test embarrasingly parallel solver?
  *@param: prolog: test prolog solver?
  *@param: par: test recursive parralell solver?
  *param: test fork join pool sovler?
  */
public class SudokuTester {

    public static HashMap<String,Result> test(boolean emb, boolean prolog,boolean par, boolean fjp, String difficulty){


        int dif = 0;

        switch (difficulty){
          case "EASY": dif=9;
          break;
          case "MEDIUM": dif=7;
          break;
          case "HARD": dif =5;
        }
        
        int[][] puzzle = genGrid(9, dif);

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


    public static int[][] genGrid(int size, int dif){
      int[][] toReturn = new int[size][size];
      int randomNum =0;
      int randVal =0;
      for (int i = 0; i < dif; i++){
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

    public static HashMap<String,Result> avg(List<HashMap<String,Result>> listOfMaps){
      int bline=0;
      int pro=0;
      int emb=0;
      int par=0;
      int fjp=0;

      int s = listOfMaps.size();
      System.out.println(s);


     
   

      for(int i =0; i < s; i++){
        HashMap<String,Result> r = listOfMaps.get(i);
          if(r.containsKey("Baseline")){
            bline = bline + Integer.valueOf(r.get("Baseline").time);
          }
          if(r.containsKey("Prolog")){
            pro = pro + Integer.valueOf(r.get("Prolog").time);
          }
          if(r.containsKey("emb")){
            emb = emb + Integer.valueOf(r.get("emb").time);
          }
          if(r.containsKey("Recursive")){
            par = par + Integer.valueOf(r.get("Recursive").time);
          }
          if(r.containsKey("fjp")){
            fjp = fjp + Integer.valueOf(r.get("fjp").time);
          }
      }
      HashMap<String,Result> avgs = new HashMap<String,Result>();


      if(listOfMaps.get(0).containsKey("Baseline")){
        avgs.put("Baseline",new Result(Long.valueOf(bline/s),listOfMaps.get(0).get("Baseline").array));
      }
      if(listOfMaps.get(0).containsKey("Prolog")){
        avgs.put("Prolog", new Result(Long.valueOf(pro/s),listOfMaps.get(0).get("Baseline").array));
      }
      if(listOfMaps.get(0).containsKey("emb")){
        avgs.put("emb", new Result(Long.valueOf(emb/s),listOfMaps.get(0).get("emb").array));
      }
      if(listOfMaps.get(0).containsKey("Recursive")){
        avgs.put("Recursive", new Result(Long.valueOf(par/s),listOfMaps.get(0).get("Recursive").array));
      }
      if(listOfMaps.get(0).containsKey("fjp")){
        avgs.put("fjp", new Result(Long.valueOf(fjp/s),listOfMaps.get(0).get("fjp").array));
      }
      return avgs;
    }
   

}
