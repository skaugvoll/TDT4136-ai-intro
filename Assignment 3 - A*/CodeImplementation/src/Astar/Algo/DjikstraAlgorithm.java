package Astar.Algo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sigveskaugvoll on 30.09.2016.
 */
public class DjikstraAlgorithm {

    /*
    * DIFFERENCE BETWEEN A* and Dijkstra:
    * A* also considers the heuristic costs from the current node, to the goal node. Therefor,
     * A* will never "walk" in the wrong direction.
     *
     * Dijkstra do not consider the heuristic, and will therefor consider neighbours that are in the
     * the wrong direction.
    *
    * */

    public List<Node> closedSet;
    public List<Node> openSet;
    public List<Node> cameFrom;

    public List algorithm(Node start, Node goal) {
        closedSet = new ArrayList<>(); //unvisited nodes
        openSet = new ArrayList<>(); //all known / visited nodes
        cameFrom = new ArrayList<>(); //the traversed path.

        openSet.add(start); //the start node is visited / knows


        //Gscore is to be calculate for each step? --> sat to DOUBLE_MAX when node is created
        //Fscore : gScore

        //Calculate the gScore for the startNode: The distance from start node to start node is always 0
        start.setgScore(0);

        //Calculate the fScore for the startNode: The distance from the start node to the goal node, through the start node
        //is completely heuristic, for all other node it is partly known, partly heuristic
        //start.setfScoore(start.calculateHeuristics(goal.getxCord(), goal.getyCord()));

        //While openset is not empty (we will remove the visited nodes)
        while (!openSet.isEmpty()) {
            Node current = openSet.get(0);
            //current is the node in the open set with the lowest fScore value
            for (Node n : openSet) {
                if (n.getFScore() < current.getFScore()) {
                    current = n;
                }
            }
            //Check if current node is goal node
            if (current.getType() == 'B') {
                reconstruct_path(current);
                return cameFrom;
            }


            openSet.remove(current);
            closedSet.add(current);

            //Find every neighbour of current, wich previously have not been visited
            List<Node> neig = current.getNeighbours();
            for (Node neighbor : neig) {
                if (closedSet.contains(neighbor)) {
                    continue; //ignore neighbours wicth previously have been visited
                }

                double tentative_gScore = current.getgScore() + dist_between(neighbor);

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                    neighbor.setPreNode(current);
                } else if (tentative_gScore >= neighbor.getgScore()) {//this is not a better path
                    continue;
                }

                //This is the best path until now. Record it!
                neighbor.setPreNode(current);
                neighbor.setgScore(tentative_gScore);
                neighbor.setfScoore(neighbor.getgScore());

            }

        }
        return null;

    }


    //return the cost between the current node, and the neighbour
    public double dist_between(Node neighbor) {
        return neighbor.getValue();

    }


    /*
    uses recursive calls to get the path from Goal to Start, checking the predecessor node of the goal node
    all the way back to start.
    */
    public void reconstruct_path(Node current) {
            if (current.getType() == 'A') {
                //cameFrom.add(current);
                return;
            } else {
                cameFrom.add(current);
                reconstruct_path(current.getPreNode());
            }
        }

    //returns the path from start to goal
    public List<Node> getCameFrom(){
        return this.cameFrom;
    }

    public List<Node> getClosedSet() { return this.closedSet; }

    public List<Node> getOpenSet() { return  this.openSet; }

}

