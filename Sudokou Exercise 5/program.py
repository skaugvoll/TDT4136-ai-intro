from assignment5 import *
from time import time


def colorMain():
    program = create_map_coloring_csp()
    print(program.backtracking_search())




def sudokuMain():

    program = create_sudoku_csp("sudokus/worldshardest.txt")
    start = time()
    solution = program.backtracking_search()
    end = time()
    print_sudoku_solution(solution)
    print("\n" + "The backtrack search failed : " + str(program.backtrack_failure) + " times")
    print("\n" + "The backtrack search has been called recursively: " + str(program.backtrack_called) + " times")
    print("\n" + "The backtrack search used: " +
          str(end-start) + " sec, approximatly: " +
          str((end-start)/60) + " minutes");

#colorMain()

sudokuMain()


