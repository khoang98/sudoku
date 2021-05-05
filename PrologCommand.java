import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class PrologCommand {

    public static void runProlog(int[][] puzzle) throws java.io.IOException, InterruptedException{

        // format puzzle for the cli command to run prolog

        String grid = Arrays.deepToString(puzzle);
        String goal = "ssolve("+ puzzle+ ",Grid),halt.";
        Process p = Runtime.getRuntime().exec("swipl -l 'SudokuSolver.pl'-q -g"+ goal);
        System.out.println("Waiting for Prolog file ...");
        p.waitFor();
        System.out.println("Prolog file done.");
        
        System.out.println("The resultant solved suduku:");
        Scanner input = new Scanner(new File("output.txt"));

        while (input.hasNextLine()){
            System.out.println(input.nextLine());
        }

        return;
    }
}
