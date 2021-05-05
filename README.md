# sudoku
COSC 273: Parallel and Distributed Computing Final Project

Connor Haugh, Kaitlin Hoang, Kyler Kopacz, Sam Rydzewski.

## Functionality:
Presently, the implementation contians the two solvers against which the parralel versions will test against. The first is a simple baseline implementation in java, the second is an implementation in prolog, which uses a logic programming approach to solve the same problem.


### HOW TO COMPLIE + RUN:

#### Without Prolog: 
Simply compile with javac*.java and then run "java SudokuTester.java" 
By default, not downloading prolog is an option as well, it just won't show up in the results. 

#### With Prolog: ADD SWI PROLOG TO YOUR MACHINE (Mac)

PROLOG IS A LOGIC PROGRAMING LANGUAGE. TO ADD PROLOG-SWI, the bog standard version of Prolog, first download the application at https://www.swi-prolog.org/download/stable and copy it to your applicatons folder.

Then, to use swipl at the command line, add /Applications/SWI-Prolog.app/Contents/MacOS/ to your $PATH variable.

for those who forgot: sudo nano /etc/paths is a fast way to do this on newer macs, just use ctrl+x to save and exit the file once you are done. confirm this works by running "echo $PATH" and looking for the path/ ALSO MAKE SURE TO RESTART YOUR TERMINAL TO MAKE THESE CHANGES TO YOUR PATH TAKE HOLD!!!!

Brew alternatives also exist, but scare me.

TO UTALIZE PROLOG, set PROLOG = true in tester.

## Future Functionality:

There are several scales at which we could parallelize the process. To check if a grid is “consistent” (contains 1-9 and nothing else) given its row, column, or boxes is an embarrassingly parallel task. 
The approach we will take, however, is how to search the state space of possible next “placements” of a number in an iterative fashion. Think about solving a 3x3 sudoku. From a partially filled grid, we can evaluate all the possible “next” grids from that grid. We can give each of these next grids to a particular thread to evaluate, and even iterate further down the “tree” in a breadth-first search approach. 

To test our performance, we will randomly populate sudoku boards with some set number of clues (aka numbers on the boards already). We will vary as well how easy and hard the boards are to solve. Then we will time how long it takes our different implementations to solve all the boards. We will also look at the average time it takes each implementation to solve a single board. We plan to compare the baseline implementation, prolog implementation, and different variants of parallel implementations. 
The program will also return a table about the relative times of each of the different solving approaches (baseline, parallel in different forms, constraint-method)
