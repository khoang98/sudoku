import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
  * Description: A class which handles input and output of puzzles into a prolog solver, obtaining back both the solved puzzle and the time it took to solve.
  */


public class PrologCommand {

    public static long runProlog(int[][] puzzle) throws java.io.IOException, InterruptedException{

        // format puzzle for the cli command to run prolog

        String grid = Arrays.deepToString(puzzle) ;
        grid = grid.replaceAll("\\s", "");
        String command = "swipl -l SudokuSolver.pl -g ssolve(" +grid+ ",Grid),halt. ";
        System.out.println(command);
        Process p = Runtime.getRuntime().exec(command);

        System.out.println("Waiting for Prolog file ...");

        String line = "";
        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        BufferedReader rdr = new BufferedReader(isr);
        while((line = rdr.readLine()) != null) {
          System.out.println(line);
        }

        isr = new InputStreamReader(p.getErrorStream());
        rdr = new BufferedReader(isr);
        while((line = rdr.readLine()) != null) {
          System.out.println(line);
        }
        p.waitFor();
        Scanner input = new Scanner(new File("grid.txt"));
        while (input.hasNextLine()){
           input.nextLine();
        }
        Scanner timescanner = new Scanner(new File("time.txt"));

        long time = (long)(timescanner.nextFloat() * 1000);

        return time;
    }
}
