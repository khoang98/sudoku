public class Result {
    public int[][] array;
    public String time;

    public Result(long runtime, int[][] sudokugrid){
        time=Long.toString(runtime);
        array=sudokugrid; 
    }
}
