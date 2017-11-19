# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util
from decimal import Decimal

from game import Agent

class ReflexAgent(Agent):
    """
      A reflex agent chooses an action at each choice point by examining
      its alternatives via a state evaluation function.

      The code below is provided as a guide.  You are welcome to change
      it in any way you see fit, so long as you don't touch our method
      headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {North, South, West, East, Stop}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        return successorGameState.getScore()

def scoreEvaluationFunction(currentGameState):
    """
      This default evaluation function just returns the score of the state.
      The score is the same one displayed in the Pacman GUI.

      This evaluation function is meant for use with adversarial search agents
      (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
      This class provides some common elements to all of your
      multi-agent searchers.  Any methods defined here will be available
      to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

      You *do not* need to make any changes here, but you can if you want to
      add functionality to all your adversarial search agents.  Please do not
      remove anything, however.

      Note: this is an abstract class: one that should not be instantiated.  It's
      only partially specified, and designed to be extended.  Agent (game.py)
      is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
      Your minimax agent (question 2)
    """




    def getAction(self, gameState):
        """
          Returns the minimax action from the current gameState using self.depth
          and self.evaluationFunction.

          Here are some method calls that might be useful when implementing minimax.

          gameState.getLegalActions(agentIndex):
            Returns a list of legal actions for an agent
            agentIndex=0 means Pacman, ghosts are >= 1

          gameState.generateSuccessor(agentIndex, action):
            Returns the successor game state after an agent takes an action

          gameState.getNumAgents():
            Returns the total number of agents in the game
        """
        "*** YOUR CODE HERE ***"
        #HUSK AT DETTE ER FORSTE LEDD I MINMAX (ROOT max)
        bestValue = Decimal("-Infinity")
        bestAction = None
        for action in gameState.getLegalActions(0):
            v = self.minMax(gameState.generateSuccessor(0, action), 1) #siden dette er root (dybde 0), oensker vi a sjekke neste dybde derfor  aa sjekke dybde 1
            if v > bestValue:
                bestValue = v
                bestAction = action
        return bestAction #Vi oensker aa returnere best action pacman kan ta



    def minMax(self,gameState, depth):
        posinf = Decimal("Infinity")
        neginf = Decimal("-Infinity")

        # Vi maa vite hvilken "agent" vi jobber med naa: 0 = pacman, > 0 er spoekelser
        agent = depth % gameState.getNumAgents()  # getNumAgents() --> antall spoekelser + pacman


        if depth == self.depth * gameState.getNumAgents() or gameState.isLose() or gameState.isWin():
            '''
            Naar vi har naatt en loevnode, fyres denne, denne returnerer "score" som pacman har oppnaatt. jo hoeyere
            score, jo bedre loesning! saa det er denne som blir bestValue som returneres (den bygges opp rekursivt)
            '''
            return self.evaluationFunction(gameState)


        if agent == 0:
            bestValue = neginf
            for action in gameState.getLegalActions(agent):
                v = self.minMax(gameState.generateSuccessor(agent, action), depth + 1)
                if v > bestValue:
                    bestValue = v
            return bestValue

        else:
            bestValue = posinf
            for action in gameState.getLegalActions(agent):
                v = self.minMax(gameState.generateSuccessor(agent, action), depth + 1)
                if v < bestValue:
                    bestValue = v

            return bestValue








class AlphaBetaAgent(MultiAgentSearchAgent):
    """
      Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
          Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"

        bestAction = None
        alfa = Decimal("-Infinity")
        beta = Decimal("Infinity")
        for action in gameState.getLegalActions(0):
            value = self.alfaBeta(gameState.generateSuccessor(0, action), 1, alfa, beta)
            if value > alfa:
                bestAction = action
            alfa = max(alfa, value)
        return bestAction


    def alfaBeta(self, gameState, depth, alfa, beta):
        oneTurn = self.depth * gameState.getNumAgents()

        if depth == oneTurn or gameState.isLose() or gameState.isWin():
            return self.evaluationFunction(gameState)

        agent = depth % gameState.getNumAgents()

        if(agent == 0):
            value = Decimal("-Infinity")
            for action in gameState.getLegalActions(agent):
                value = max(value, self.alfaBeta(gameState.generateSuccessor(agent, action), depth + 1, alfa, beta))
                if value > beta:
                    return value
                alfa = max(alfa, value)
            return value

        else:
            value = Decimal("Infinity")
            for action in gameState.getLegalActions(agent):
                value = min(value, self.alfaBeta(gameState.generateSuccessor(agent, action), depth + 1, alfa, beta))
                if value < alfa:
                    return value
                beta = min(beta, value)
            return value




class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
          Returns the expectimax action using self.depth and self.evaluationFunction

          All ghosts should be modeled as choosing uniformly at random from their
          legal moves.
        """
        "*** YOUR CODE HERE ***"
        util.raiseNotDefined()

def betterEvaluationFunction(currentGameState):
    """
      Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
      evaluation function (question 5).

      DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()

# Abbreviation
better = betterEvaluationFunction

