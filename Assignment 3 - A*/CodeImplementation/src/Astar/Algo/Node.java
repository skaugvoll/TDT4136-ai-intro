package Astar.Algo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sigveskaugvoll on 27.09.2016.
 */
public class Node {

    private int value;
    private double gScore = Double.MAX_VALUE;  //the actuall distance from the startNode to this node
    private double fScoore = Double.MAX_VALUE;  // Gscore + Heuaristics.
    private char type; //the type of the node, given by the board-file.
    private int xCord; // x-position of the node, on the board, used to represent 'row'
    private int yCord; //y-position of the node, on the board, used to represnet 'column'

    private Node[] naboer = new Node[4]; //one node, will always have atmost 4 neighbours
    private Node preNode; //one node, will only have one predecessor.




    //Initiate the node, with the information know.
    public Node(int xCord, int yCord) {
        preNode = null;
        this.xCord = xCord;
        this.yCord = yCord;
    }


    //Set the type and value of the type, of this node.
    public void setType(char type){
        if(type == 'w'){
            this.value = 100;
        }
        else if(type == 'm'){
            this.value = 50;
        }
        else if(type =='f'){
            this.value =  10;
        }
        else if(type == 'g'){
            this.value = 5;
        }
        else if(type == '#'){
            this.value = Integer.MAX_VALUE;
        }
        else{
            this.value = 1;
        }
        this.type = type;
    }

    //return the type of the node
    public char getType(){
        return this.type;
    }

    //set the neighbours of this node, unless it's a wall or 'null'.
    public void giveNaboer(Node n, Node s, Node w, Node e){
        if(n != null && n.getType() != '#'){
            naboer[0] = n;
        }
        if(s != null && s.getType() != '#'){
            naboer[1] = s;
        }
        if(w != null && w.getType() != '#'){
            naboer[2] = w;
        }
        if(e != null && e.getType() != '#'){
            naboer[3] = e;
        }
    }


    //set the g-score of the node
    public void setgScore(double score){
        this.gScore = score;
    }





    public double calculateHeuristics(int goalPosx, int goalPosy) {
        double score = 0;

        if (type != '#') {
            score = Math.pow((xCord - goalPosx), 2) + Math.pow((yCord - goalPosy), 2); //Euclian method SLD.
            score = Math.sqrt(score);
            //System.out.printf("%.3f\n", score);
            return score;
        }
        return -1;
    }




    public List<Node> getNeighbours() {
        List<Node> n = new ArrayList<>();

        for (int i = 0; i < naboer.length; i++) {
            if (naboer[i] != null) {
                n.add(naboer[i]);

            }
        }
        return n;
    }










    //set the f-score of the node
    public void setfScoore(double score){
        this.fScoore = score;
    }

    //set the predecessor of this node
    public void setPreNode(Node preNode){
        this.preNode = preNode;
    }

    //get the f-score of this node
    public double getFScore() {
        return fScoore;
    }

    //get the g-score of this node
    public double getgScore() {
        return gScore;
    }

    //get the predecessor of this node
    public Node getPreNode(){
        return this.preNode;
    }

    //get the x-coordinate of this node;
    public int getxCord(){
        return xCord;
    }

    //get the y-coordinate of this node,
    public int getyCord(){ return yCord; }

    //get the value of this node;
    public int getValue(){
        return this.value;
    }


    //This returns a String representation of the Node-object.
    public String toString(){
        return getxCord() + "," + getyCord() + "\tType: " + getType() + "||";
    }


}
