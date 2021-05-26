/**
  * Description: A data class which stores the results of a test: the solved puzzle in int[][] form, as well as the runtime in int form (milliseconds)
  */

public class Result {
    public int[][] array;
    public String time;

    public Result(long runtime, int[][] sudokugrid){
        time=Long.toString(runtime);
        array=sudokugrid; 
    }
}
