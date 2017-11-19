package Astar.Algo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sigveskaugvoll on 27.09.2016.
 */
public class AstarAlgorithm {

    public List<Node> closedSet; //unvisited nodes
    public List<Node> openSet; //all known / visited nodes
    public List<Node> cameFrom; //the traversed path.

    //This method, calculates the best "road" from a given node, to a goal node, using A* algorithm.
    public List algorithm(Node start, Node goal) {
        closedSet = new ArrayList<>();
        openSet = new ArrayList<>();
        cameFrom = new ArrayList<>();

        openSet.add(start); //the start node is visited / knows


        //Gscore is to be calculate for each step? --> sat to DOUBLE_MAX when node is created
        //heuristics - the "estimated" distance from the given node, to the goalnode
        //Fscore - gScore + heauristicst --> sat to DOUBLE_MAX when node is created

        //Calculate the gScore for the startNode: The distance from start node to start node is always 0
        start.setgScore(0);

        //Calculate the fScore for the startNode: The distance from the start node to the goal node, through the start node
        //is completely heuristic, for all other node it is partly known, partly heuristic
        start.setfScoore(start.calculateHeuristics(goal.getxCord(), goal.getyCord()));

        //While openset is not empty (we will remove the visited nodes)
        while (!openSet.isEmpty()) {
            Node current = openSet.get(0);
            //current is the node in the open set with the lowest fScore value
            for (Node n : openSet) {
                if (n.getFScore()  < current.getFScore()) { //check if neighbours fScore is lower than current nodes
                    current = n; //set current node to the neighour with the lowest fScore
                }
            }
            //Check if current node is goal node
            if (current.getType() == 'B') {
                reconstruct_path(current); // if current node is goal node, find path!
                return cameFrom; //return found path
            }

            //this happens if the current node, is not the goal node!
            openSet.remove(current);// remove the current node from the set of unvisited,
            closedSet.add(current); //add the current node to the set of visited nodes.

            //Find every neighbour of current node, wich previously have not been visited
            List<Node> neig = current.getNeighbours();
            for (Node neighbor : neig) {
                if (closedSet.contains(neighbor)) {
                    continue; //ignore neighbours wicth previously have been visited
                }

                double tentative_gScore = current.getgScore() + dist_between(current, neighbor) ; //calculate the gScore to neighbour!

                if (!openSet.contains(neighbor)) { //if the set of unvisited nodes, doesn't contain the neihbour, add it!
                    openSet.add(neighbor); // add the neighour to the set of unvisited nodes!
                } else if (tentative_gScore >= neighbor.getgScore()) {//this is not a better path
                    continue; //just chill, and precede with the algorithm, nothing to do.
                }

                //This is the best path until now. Record it!
                neighbor.setPreNode(current); //set the predecessor of the neighbour to the current node, so we can trace the path!
                neighbor.setgScore(tentative_gScore); // set the gScore of the neighbour, which is the tentative g_score, calculated earlier.
                neighbor.setfScoore(neighbor.getgScore() + neighbor.calculateHeuristics(goal.getxCord(), goal.getyCord())); //set the neighours fScore, which is gScore + heuristics

            }

        }
        return null; //return null, if we can't find the best road from start to goal!

    }


    //Calculates the distance between current node, and neighbour!
    public double dist_between(Node current, Node neighbor) {
        return neighbor.getValue();

    }

    /*
    * This function is recursive, and uses the predecessor of the current node given
    * to add it to the cameFrom list. So the Camefrom list is just the nodes traversed to goal node.
    * It stops recursing when we hit the start node, with type A.
    * */
    public void reconstruct_path(Node current) {
        if (current.getType() == 'A') {
            //cameFrom.add(current); //If we want the path to containe the start node.
            return;
        } else {
            cameFrom.add(current); //add the current node to the cameFrom list / path.
            reconstruct_path(current.getPreNode()); //call the function again with the predecessor!
        }
    }

    //This method, returns the list cameFrom, it contains all the nodes in the path!
    public List<Node> getCameFrom(){
        return this.cameFrom;
    }


    public List<Node> getClosedSet() { return this.closedSet; }

    public List<Node> getOpenSet() { return  this.openSet; }




/*                    <----         DEBUG         ---->                 */
    //This function is used to generate the path, in a terminal view!
    public void cameFromToString() {      //change to void
        //String path = "";                 //comment out
        for (Node n : cameFrom) {
            if (n.getType() != 'A' && n.getType() != 'B') {
                n.setType('o');
            }
           // path += n.getxCord() + "," + n.getyCord() + "\n";   //comment out
        }

       // return path;  //comment out,
    }


    //This function is used to debug, to find all nodes, that are known to the algorithm, in a terminal view!
    public String openSetToString() {
        String path = "";
        for (Node n : openSet) {
            if (n.getType() != 'A' && n.getType() != 'B') {
                n.setType('K');
            }
            path += n.getxCord() + "," + n.getyCord() + "\n";
        }

        return path;
    }

    //This function is used to debug, to find all nodes, that are visited in the algorithm, in a terminal view!
     public String closedSetToString() {
         String path = "";
         for (Node n : closedSet) {
             if (n.getType() != 'A' && n.getType() != 'B') {
                 n.setType('C');
             }
             path += n.getxCord() + "," + n.getyCord() + "\n";
         }
        return path;
     }


}