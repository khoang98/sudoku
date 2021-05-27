# sudoku
COSC 273: Parallel and Distributed Computing Final Project

Connor Haugh, Kaitlin Hoang, Kyler Kopacz, Samantha Rydzewski.

## **OVERVIEW:**
Our project aims to build a parallel sudoku solver. Sudoku is a logic based, combinatorial number-placement puzzle.
A traditional sudoku board consists of a 9x9 grid with some numbers prefilled and the goal is to fill in numbers 1-9 such that each row, column, and 3x3 square contains each number. 
Our baseline, sequential solver solves the puzzle as follows:
  Select an empty place on the board, p 
  Select a value 1-9, x
  Determine if x at p violates any of the 1-9 exclusiveness for any row, column, or subgrid
    if no, add x to the grid at p and recur
    if yes, return to step 2 and select another value. If no values are valid, backtrack and remove the last value added.
  When the grid is full, return

We also built a baseline solution in Prolog.
Our program compares this two baseline performances to that of 3 parallel implementations we refer to as the Embarrassingly Parallel solution, Fork-Join solution, and Recursive solution.
We discuss the specifics of these solutions in the Functionality section.


## **HOW TO COMPLIE + RUN:**
Simply compile with javac*.java and then run "java RunSudoku". 

A GUI will appear where you can select which implementations you'd like to compare.
On the left hand side, input into the small white box how many boards to average over and choose a difficulty level from the drop down.
Then, click the run button and the results will appear in the large white box in the GUI.
If you do not have Prolog installed, do not select that option.
We give instructions on how to install Prolog below.

#### **With Prolog: ADD SWI PROLOG TO YOUR MACHINE** 
#### **Mac:**

PROLOG IS A LOGIC PROGRAMING LANGUAGE. TO ADD PROLOG-SWI, the bog standard version of Prolog, first download the application at https://www.swi-prolog.org/download/stable and copy it to your applicatons folder.

Then, to use swipl at the command line, add /Applications/SWI-Prolog.app/Contents/MacOS/ to your $PATH variable.

for those who forgot: 
```
sudo nano /etc/paths
``` 
is a fast way to do this on newer macs, just use ctrl+x to save and exit the file once you are done. confirm this works by running "echo $PATH" and looking for the path/ ALSO MAKE SURE TO RESTART YOUR TERMINAL TO MAKE THESE CHANGES TO YOUR PATH TAKE HOLD!!!!

Brew alternatives also exist, but scare me.

#### **Linux**:

Installing Prolog in Ubuntu Linux is super simple: 
```
sudo apt install swi-prolog-nox
```


## **FUNCTIONALITY**
To parallelize the sudoku solver, we saw two oppurtunities to parallelize.
  1) We can parallelize the board validation.
  2) Explore possible solutions in parallel tasks.

We refer to 1) as our Embarrassingly Parallel solution. For this implementation, we preform the same process as our sequential baseline solver but when doing the validation we do it in parallel. We create 3 tasks that we run in parallel-  (1) check row is valid, (2) check column is valid, (3) check that the sub 3x3 square is valid every time we check if a board is valid.

For 2), we took two different approaches with the implementation. 
The general algorithm for each works by creating a new task for each blank square in the board. This task then places valid values 1-9 in that place and spawns new tasks for the next blank square.Tasks with invalid solutions are terminated, while tasks with valid solutions can create new tasks.
We implemented this algorithm using a thread pool and a fork-join pool. The fork-join pool forks on each possible value on a square and whichever task produces a valid board passes the board back up in the recursion on join().
We refer to these solutions as the Recursive implementation and the Fork-Join solver implementation, respectively.


## **TESTING**
To test our performance, we randomly populate sudoku board with some set number of clues and time how long it takes for each implementation to solve them.
We also average over multiple boards to look at the average time it takes each solver to solve sudoku.
We also test our solvers on easy, medium, and hard boards. We rate the difficulty of the board based on how many clues it has.

Below is some of the results we got while testing.
Testing on a random 9x9 easy board with 9 prefilled values:
  The baseline Solver: 621ms 
  The Prolog Solver: 83ms 
  The Embarrasingly parralell Solver: 196ms 
  The Fork-Join Pool Solver: 13 ms 

Testing on a random 9x9 medium board with 7 prefilled values:
  Performance with 4 threads on local computer:
    The baseline Solver: 1274ms 
    The Prolog Solver: 154ms 
    The Embarrasingly Paralell Solver: 466ms 
    The Fork-Join Pool Solver: 27 ms 
    The Recurisve Solver: N/A ran out of memory

  Performance on Remus with 32 threads:
    The baseline Solver: 1063ms 
    The Embarrasingly parralell Solver: 348ms 
    The Fork-Join Pool Solver: 40 ms

Testing on a random 9x9 hard board with 5 prefilled values:
  The baseline Solver: 385ms 
  The Embarrasingly parralell Solver: 354ms 
  The Fork-Join Pool Solver: 5 ms 


We can see that that the Fork-Join Pool Solver outperforms both the baseline and Prolog solution. Note the Embarrasingly Paralell Solver also outperforms the baseline but not the Prolog Solver. This shows us that parallelizing did help our performance in solving sudoku. 
The Recursive Solver, on the other hand, did not improve the performance. We believe the reason for this is that the overhead of the ThreadPool is too much for how easily solved the program is sequentially. 
