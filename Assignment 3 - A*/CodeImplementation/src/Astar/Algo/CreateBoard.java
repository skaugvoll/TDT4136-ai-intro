package Astar.Algo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by sigveskaugvoll on 26.09.2016.
 */
public class CreateBoard {

    public File boardFile; //File object of the given file, that contains the board!
    public Node[][] board; //a 2D-representation of the board


    public int numberColumns, numberRows; //variables to keep track of the size of the board


    public int goalPosx, goalPosy;  //variables to keep track of the position of the goal node
    public Node goalNode; //the goal node

    public int startPosx, startPosy; //variables to keep track of the position of the goal node
    public Node startNode; //the goal node



    //This function takes in a filepath, creates a new file objeckt of the filepath.
    public CreateBoard (String filepath){
        this.boardFile = new File(filepath); //create file objeckt
        findColumnsAndRows(); //find the size of the board
        board = new Node[numberRows][numberColumns]; //empty board in correct RxC size.

    }

    //This function finds the number of columns and rows of the board
    public void findColumnsAndRows(){
        try {
            Scanner scanner = new Scanner(this.boardFile);
            int tempRow = 1;
            // we know that a board must have atleast one column,
            // and that the length og the first line, determines the number of columns
            this.numberColumns = scanner.nextLine().length();
            while (scanner.hasNextLine()) {  // run through the file
                tempRow++; //for each new line, there is a new row in our board
                scanner.nextLine(); //read next line
            }
            this.numberRows = tempRow; //after read through the file, we set the number of rows varaialbe equal to the number of rows read (tempRow)
            scanner.close(); //close the scanner ;)
        }
        catch(Exception e){
            System.out.println("Feil ved findColumnsAndRows");
        }

    }

    //This function, fills the 2D Array representation of the board with the correct dataset.
    public void fillData(){
        try {
            Scanner scanner = new Scanner(this.boardFile);
            int row = 0;

            while (scanner.hasNext()) { //read through the file, character by character.
                String x = scanner.next(); // x is a row in the board
                for(int c = 0; c < numberColumns; c++){ //go through every character on the row
                    Node node = new Node(row,c); // create a node for the given position on the board,
                    char type = x.charAt(c); //type is the current charater on the board, on this row
                    if(type == 'A'){ //check if type == A (startNode)
                        startPosx = row;
                        startPosy = c;
                        startNode = node;
                    }
                    else if(type == 'B'){ //check if type == B (goalNode)
                        goalPosx = row;
                        goalPosy = c;
                        goalNode = node;
                    }
                    node.setType(type); //set the type of the node to the correct type, and give it it's correct value.
                    board[row][c] = node; //insert the node, in the 2D Array representation of the board
                }
                row++; //next row!
            }
            //When we have read the entire board, and created the 2D representaion,
            scanner.close(); //close the scanner
            findNeighbors(); //go through the board once more, and assign neighbours to every node!

        }
        catch (IOException e){
            System.out.println("Something happend in fillData()");
        }
    }


    //This method, gives every node it's neighbours, and there is no neighour, it sets the value to   'null'.
    public void findNeighbors(){
        for(int r = 0; r < numberRows;r++){
            for(int c = 0; c < numberColumns; c++){
                Node current = board[r][c];
                if(c == 0 && r == 0){ // No neighbours at N and E
                    current.giveNaboer(null,board[r+1][c],null,board[r][c+1]);
                }
                else if(c == numberColumns-1 && r == numberRows-1){ // No neighbours at S and W
                    current.giveNaboer(board[r-1][c],null,board[r][c-1],null);
                }
                else if(c == 0 && r == numberRows -1){ // No neighbours at S and E
                    current.giveNaboer(board[r-1][c],null,null,board[r][c+1]);
                }
                else if(c == numberColumns-1 && r == 0){ // No neighbours at N and E
                    current.giveNaboer(null,board[r+1][c],board[r][c-1],null);
                }
                else if(c == 0 && r != numberRows-1){ //No neighbours at E
                    current.giveNaboer(board[r-1][c],board[r+1][c],null,board[r][c+1]);
                }
                else if(c == numberColumns-1 && r!= 0 && r != numberRows-1){ // No neighbours at W
                    current.giveNaboer(board[r-1][c],board[r+1][c],board[r][c-1],null);
                }
                else if(c != 0 && c != numberColumns-1 && r == 0){ // No neighbours at N
                    current.giveNaboer(null,board[r+1][c],board[r][c-1],board[r][c+1]);
                }
                else if(c != 0 && c != numberColumns-1 && r == numberRows-1){ // No neighbours at S
                    current.giveNaboer(board[r-1][c],null,board[r][c-1],board[r][c+1]);
                }
                else{ // Neighbours at all sides
                    current.giveNaboer(board[r-1][c],board[r+1][c],board[r][c-1],board[r][c+1]);
                }
            }
        }
    }

    //Returns a terminal-view friendly representation of the 2D-array board representation
    public void getBoard(){
        String representation ="";
        for(int r = 0; r < numberRows;r++){
            for(int c = 0; c < numberColumns; c++){
                representation += board[r][c].getType();
            }
            representation +="\n";
        }
        System.out.println(representation);
    }


    //Method to create a board, run the Astar algorithm on the board, and present the solution in a terminal-view.
    public static void main(String[] args){
        //create and fill the 2D representation of the board
        CreateBoard b = new CreateBoard("/Users/sigveskaugvoll/Documents/Skole/2016H/AI/Assignment 3 - A*/boards/board-2-4.txt");
        b.fillData();
        b.getBoard();


        //Run the Astar algorithm on the 2D board representation.
        AstarAlgorithm algo = new AstarAlgorithm();
        //System.out.println(algo.algorithm(b.startNode, b.goalNode));



        //algo.openSetToString();
        //algo.closedSetToString();
        //Use the terminal-view path method to draw the path!
        algo.cameFromToString();

        //Draw the board with the path in the terminal.
        b.getBoard();


    }


}
