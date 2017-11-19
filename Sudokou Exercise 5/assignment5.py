#!/usr/bin/python

import copy
import itertools


class CSP:
    def __init__(self):
        # self.variables is a list of the variable names in the CSP
        self.variables = []

        # self.domains[i] is a list of legal values for variable i
        self.domains = {}

        # self.constraints[i][j] is a list of legal value pairs for
        # the variable pair (i, j)
        self.constraints = {}

        #  This variable gets incremented for each time the backtrack function is called recursively
        self.backtrack_called = 0

        #  This variable gets incremented for each time the backtrack functions returned failure
        self. backtrack_failure = 0

    def add_variable(self, name, domain):
        """Add a new variable to the CSP. 'name' is the variable name
        and 'domain' is a list of the legal values for the variable.
        """
        self.variables.append(name)
        self.domains[name] = list(domain)
        self.constraints[name] = {}

    def get_all_possible_pairs(self, a, b):
        """Get a list of all possible pairs (as tuples) of the values in
        the lists 'a' and 'b', where the first component comes from list
        'a' and the second component comes from list 'b'.
        """
        return itertools.product(a, b)

    def get_all_arcs(self):
        """Get a list of all arcs/constraints that have been defined in
        the CSP. The arcs/constraints are represented as tuples (i, j),
        indicating a constraint between variable 'i' and 'j'.
        """
        return [(i, j) for i in self.constraints for j in self.constraints[i]]

    def get_all_neighboring_arcs(self, var):
        """Get a list of all arcs/constraints going to/from variable
        'var'. The arcs/constraints are represented as in get_all_arcs().
        """
        return [(i, var) for i in self.constraints[var]]

    def add_constraint_one_way(self, i, j, filter_function):
        """Add a new constraint between variables 'i' and 'j'. The legal
        values are specified by supplying a function 'filter_function',
        that returns True for legal value pairs and False for illegal
        value pairs. This function only adds the constraint one way,
        from i -> j. You must ensure that the function also gets called
        to add the constraint the other way, j -> i, as all constraints
        are supposed to be two-way connections!
        """
        if not j in self.constraints[i]:
            # First, get a list of all possible pairs of values between variables i and j
            self.constraints[i][j] = self.get_all_possible_pairs(self.domains[i], self.domains[j])

        # Next, filter this list of value pairs through the function
        # 'filter_function', so that only the legal value pairs remain
        self.constraints[i][j] = filter(lambda value_pair: filter_function(*value_pair), self.constraints[i][j])

    def add_all_different_constraint(self, variables):
        """Add an Alldiff constraint between all of the variables in the
        list 'variables'.
        """
        for (i, j) in self.get_all_possible_pairs(variables, variables):
            if i != j:
                self.add_constraint_one_way(i, j, lambda x, y: x != y)

    def backtracking_search(self):
        """This functions starts the CSP solver and returns the found
        solution.
        """
        # Make a so-called "deep copy" of the dictionary containing the
        # domains of the CSP variables. The deep copy is required to
        # ensure that any changes made to 'assignment' does not have any
        # side effects elsewhere.
        assignment = copy.deepcopy(self.domains)

        # Run AC-3 on all constraints in the CSP, to weed out all of the
        # values that are not arc-consistent to begin with
        self.inference(assignment, self.get_all_arcs())

        # Call backtrack with the partial assignment 'assignment'
        return self.backtrack(assignment)

    def backtrack(self, assignment):
        """The function 'Backtrack' from the pseudocode in the
        textbook.

        The function is called recursively, with a partial assignment of
        values 'assignment'. 'assignment' is a dictionary that contains
        a list of all legal values for the variables that have *not* yet
        been decided, and a list of only a single value for the
        variables that *have* been decided.

        When all of the variables in 'assignment' have lists of length
        one, i.e. when all variables have been assigned a value, the
        function should return 'assignment'. Otherwise, the search
        should continue. When the function 'inference' is called to run
        the AC-3 algorithm, the lists of legal values in 'assignment'
        should get reduced as AC-3 discovers illegal values.

        IMPORTANT: For every iteration of the for-loop in the
        pseudocode, you need to make a deep copy of 'assignment' into a
        new variable before changing it. Every iteration of the for-loop
        should have a clean slate and not see any traces of the old
        assignments and inferences that took place in previous
        iterations of the loop.
        """
        # TODO: IMPLEMENT THIS
        self.backtrack_called += 1
        complete = True
        # Check if backtracking is finished, because every variable has a domain consisting of only one legal value
        for var in assignment:
            if len(assignment[var]) != 1:
                complete = False

        # If finished return assignment
        if complete:
            return assignment

        '''
        Get a unsigned variable from the problem, unsigned = has a domain of legal values with more than one value
        '''
        unasVar = self.select_unassigned_variable(assignment)


        '''
        For each of the values in the domain of considered legal values for the unsigned variable
        try and reduce/revise the domain, to only one value and make the variable signed.
        '''
        for value in self.domains[unasVar]:  # for each value in the unassigned var's domain
            if value in assignment[unasVar]:  # if the val of the unassigned var's domain is a legal value for the unassigned var in the "game"
                assignment_copy = copy.deepcopy(assignment)  # make a deep copy of the games legal val for the var
                assignment_copy[unasVar] = [value]  # set the 'val' to the value of the var, removing other values from its domain

                inference = self.inference(assignment_copy, self.get_all_arcs())  # check if assigning is valid/legal

                if inference:  # if the inference is true, we can check if it helps generate a solution
                    result = self.backtrack(assignment_copy)  # having set this variables value, try and set another var's value
                    if result:  # if all variables values has been set, and there is a solution
                        return result  # return the solution
        self.backtrack_failure += 1
        return None

    def select_unassigned_variable(self, assignment):
        """The function 'Select-Unassigned-Variable' from the pseudocode
        in the textbook. Should return the name of one of the variables
        in 'assignment' that have not yet been decided, i.e. whose list
        of legal values has a length greater than one.
        """
        # TODO: IMPLEMENT THIS

        # Returns the value if the list is not 1.
        sorted_list = assignment
        sorted(sorted_list)
        for var in sorted_list:  # get all variables
            if len(sorted_list[var]) != 1:  # if the variable has a domain with more than one legal value
                return var


    def inference(self, assignment, queue):
        """The function 'AC-3' from the pseudocode in the textbook.
        'assignment' is the current partial assignment, that contains
        the lists of legal values for each undecided variable. 'queue'
        is the initial queue of arcs that should be visited.
        """
        # TODO: IMPLEMENT THIS
        # print(queue)
        while queue:
            '''
            Get the tuple / pair of variables in the queue, what tuple to pick, makes no difference,
            just pick a random tuple.
            '''
            arc = queue.pop()
            xi = arc[0]  # get the variable i from the tuple arc
            xj = arc[1]  # get the variable j from the tuple arc

            domain = assignment[xi]  # get the domain of legal values for var i

            if self.revise(assignment, xi, xj):  # check if the domain of legal values for var i, can be reduced.
                if not len(domain):  # if the domain of var i, is 0 or null
                    return False  # return False, because there is no solution!
                neighbors = self.get_all_neighboring_arcs(xi)  # get the neighbors to var i
                # neighbors.pop(0)  remove xj from the neighbors
                for xk in neighbors:
                    if xk != xj:  # remove xj from the neighbors
                        queue.append(xk)
        return True

    def revise(self, assignment, i, j):
        """The function 'Revise' from the pseudocode in the textbook.
        'assignment' is the current partial assignment, that contains
        the lists of legal values for each undecided variable. 'i' and
        'j' specifies the arc that should be visited. If a value is
        found in variable i's domain that doesn't satisfy the constraint
        between i and j, the value should be deleted from i's list of
        legal values in 'assignment'.
        """
        # TODO: IMPLEMENT THIS
        revised = False  # initial assumption is that there is no revision of var i's domain
        di = assignment[i]  # get domain of var i
        dj = assignment[j]  # get domain of var j

        constraints = self.constraints[i][j]  # get the constraints between i and j

        for x in di:  # for each value in variable i's domain
            foundY = False  # for each value in di, we want to check if it can satisfy a constraint
            for y in dj:  # for each value in variable j's domain
                tempTup = (x, y)  # create a temp tuple to check if it satisfy the constraints

                if tempTup in constraints:  # if (x,y) satisfy a constraint:
                    foundY = True  # there is a value in dj that satisfy a x's constraint on dj
                    break

            if not foundY:  # if there is no value y in dj that satisfy the constraints, rem val x from  var i's domain
                di.remove(x)
                revised = True  # make sure to return that the domain of var i is revised.

        return revised


def create_map_coloring_csp():
    """Instantiate a CSP representing the map coloring problem from the
    textbook. This can be useful for testing your CSP solver as you
    develop your code.
    """
    csp = CSP()
    states = ['WA', 'NT', 'Q', 'NSW', 'V', 'SA', 'T']
    edges = {'SA': ['WA', 'NT', 'Q', 'NSW', 'V'], 'NT': ['WA', 'Q'], 'NSW': ['Q', 'V']}
    colors = ['red', 'green', 'blue']
    for state in states:
        csp.add_variable(state, colors)
    for state, other_states in edges.items():
        for other_state in other_states:
            csp.add_constraint_one_way(state, other_state, lambda i, j: i != j)
            csp.add_constraint_one_way(other_state, state, lambda i, j: i != j)
    return csp


def create_sudoku_csp(filename):
    """Instantiate a CSP representing the Sudoku board found in the text
    file named 'filename' in the current directory.
    """
    csp = CSP()
    board = map(lambda x: x.strip(), open(filename, 'r'))

    for row in range(9):
        for col in range(9):
            if board[row][col] == '0':
                csp.add_variable('%d-%d' % (row, col), map(str, range(1, 10)))
            else:
                csp.add_variable('%d-%d' % (row, col), [board[row][col]])

    for row in range(9):
        csp.add_all_different_constraint(['%d-%d' % (row, col) for col in range(9)])
    for col in range(9):
        csp.add_all_different_constraint(['%d-%d' % (row, col) for row in range(9)])
    for box_row in range(3):
        for box_col in range(3):
            cells = []
            for row in range(box_row * 3, (box_row + 1) * 3):
                for col in range(box_col * 3, (box_col + 1) * 3):
                    cells.append('%d-%d' % (row, col))
            csp.add_all_different_constraint(cells)

    return csp


def print_sudoku_solution(solution):
    """Convert the representation of a Sudoku solution as returned from
    the method CSP.backtracking_search(), into a human readable
    representation.
    """

    for row in range(9):
        for col in range(9):
            print solution['%d-%d' % (row, col)][0],
            if col == 2 or col == 5:
                print '|',
        print
        if row == 2 or row == 5:
            print '------+-------+------'


