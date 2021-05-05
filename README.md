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

for those who forgot: sudo nano /etc/paths is a fast way to do this on newer macs, just use ctrl+x to save and exit the file once you are done. ALSO MAKE SURE TO RESTART YOUR TERMINAL TO MAKE THESE CHANGES TO YOUR PATH TAKE HOLD!!!!

Brew alternatives also exist, but scare me.

TO UTALIZE PROLOG, set PROLOG = true in tester.

## Future Functionality:

We will add at least one parallel implementation which is essentialy a parallel implementation of tree search, but within that taks we can vary parameters of that tree search, which must use fork-join constructions to delegate tasks.

The program will also return a table about the relative times of each of the different solving approaches (baseline, parallel in different forms, constraint-method)
