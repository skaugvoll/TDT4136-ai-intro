package Astar.Algo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sigveskaugvoll on 30.09.2016.
 */
public class BreadthFirstSearchAlgorithm {

    /*
    * THE DIFFERENCE BETWEEN BFS and A*
    * A* picks the neighbour with the lowest tentative cost, and therefor also consider the heuristics.
    * This ensures that A* chooses the best path, with the lowest cost.
    *
    * BFS uses a FIFO queue, that means that the algorithm, checks all the nodes in the depth, before
    * changing to a new depth, this results in the fact that if the goal, is in a very high depth, (far away from start)
    * BFS, will traverse a lot of nodes, before it finds the goal, and will not be as fast as A*.
    *
    * BFS does'n consider weights either, so it will not fin the cheapest way, but it will find the "fastes" since it will,
    * find the fewest steps to the goal, considering it always expands it's depth with one, untile it finds the goal!
    *
    * */

    public List<Node> closedSet;
    public List<Node> openSet;
    public List<Node> cameFrom;

    public List algorithm(Node start) {
        closedSet = new ArrayList<>(); //unvisited nodes
        openSet = new ArrayList<>(); //all known / visited nodes
        cameFrom = new ArrayList<>(); //the traversed path.

        openSet.add(start); //the start node is visited / knows



        //Calculate the gScore for the startNode: The distance from start node to start node is always 0
        start.setgScore(0);


        //While openset is not empty (we will remove the visited nodes)
        while (!openSet.isEmpty()) {
            Node current = openSet.get(0);

            //Check if current node is goal node
            if (current.getType() == 'B') {
                reconstruct_path(current);
                return cameFrom;
            }


            openSet.remove(current); //remove the current node from the set of unvisited nodes,
            closedSet.add(current); //add the current node to the set of visited nodes

            //Find every neighbour of current, wich previously have not been visited
            List<Node> neig = current.getNeighbours();
            for (Node neighbor : neig) {
                if (closedSet.contains(neighbor)) {
                    continue; //ignore neighbours wicth previously have been visited
                }

                double tentative_gScore = current.getgScore() + dist_between(neighbor);

                if (!openSet.contains(neighbor)) { //If the neighbour is not in the unvisited set,
                    openSet.add(neighbor); //add the neighour to the set of unvisited, bur known.

                } else if (tentative_gScore >= neighbor.getgScore()) {
                    //this is not a better path
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

    //Find the distance between the current and neighbour, = weight of the neighbour.
    public double dist_between(Node neighbor) {
        return neighbor.getValue();

    }


    //This method uses recursive function to find the path!
    public void reconstruct_path(Node current) {
        if (current.getType() == 'A') {
            //cameFrom.add(current);
            return;
        } else {
            cameFrom.add(current);
            reconstruct_path(current.getPreNode());
        }
    }


    //returns the path from start to goal!
    public List<Node> getCameFrom(){
        return this.cameFrom;
    }

    public List<Node> getClosedSet() { return this.closedSet; }

    public List<Node> getOpenSet() { return  this.openSet; }









}
