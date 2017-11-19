package Astar.Controllers;

import Astar.Algo.CreateBoard;
import Astar.Algo.Node;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import java.util.List;

/**
 * Created by sigveskaugvoll on 30.09.2016.
 */
public class DrawBoard {

    private boolean comparison = true; //this is used to switch open and closed set visibility.

    private final List<Node> path;  //the BEST path from start to goal
    private List<Node> closedSet;   //the set containing all the visited nodes
    private List<Node> openSet;     //the set containing all the known nodes

    private CreateBoard board;
    private GridPane gridPane = new GridPane();
    private int rows;
    private int columns;



    public DrawBoard(CreateBoard board, List<Node> path, List<Node> closedSet, List<Node> openSet) {
        this.board = board;
        this.path = path;
        this.rows = board.numberRows;
        this.columns = board.numberColumns;
        this.closedSet = closedSet;
        this.openSet = openSet;
        draw();

    }


    private void draw(){

        //create cell for each row
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(40);
            gridPane.getRowConstraints().add(rowConst);
        }

        //create cell for each column
        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(40);
            gridPane.getColumnConstraints().add(colConst);
        }

        //Paint the board with correct colors, corresponding to the type and value of the cell.
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < columns; c++){
                HBox cell = new HBox(); //Create a new HBox, to "host" our node-cell.
                Node current = board.board[r][c]; //get the current Node from our board.
                char currentType = current.getType(); //get the type of the node.
                if(path.contains(current) && (currentType != 'A' && currentType != 'B')){
                    Label trace = new Label("\u25CF");
                    cell.getChildren().add(trace);
                    cell.setAlignment(Pos.CENTER);
                }
                if(comparison){
                    if(closedSet.contains(current) && !path.contains(current) && currentType != 'A' && currentType != 'B'){
                        Label openTrace = new Label("*");
                        cell.getChildren().add(openTrace);
                        cell.setAlignment(Pos.CENTER);
                    }
                    else if(openSet.contains(current) && !path.contains(current) && currentType != 'A' && currentType != 'B'){
                        Label closedTrace = new Label("x");
                        cell.getChildren().add(closedTrace);
                        cell.setAlignment(Pos.CENTER);
                    }
                }


                if(currentType == 'w'){
                    cell.setStyle("-fx-background-color: #4d4dff;");
                }
                if(currentType == 'r'){
                    cell.setStyle("-fx-background-color: #bf8040;");
                }
                if(currentType == 'm'){
                    cell.setStyle("-fx-background-color: #a6a6a6;");
                }
                if(currentType == 'g'){
                    cell.setStyle("-fx-background-color: #80ff80;");
                }
                if(currentType == 'f'){
                    cell.setStyle("-fx-background-color: #008000;");
                }
                if(currentType == 'A'){
                    cell.setStyle("-fx-background-color: #ff0000;");
                    cell.getChildren().add(new Label("A"));
                    cell.setAlignment(Pos.CENTER);
                }
                if(currentType == 'B'){
                    cell.setStyle("-fx-background-color: #00ff00;");
                    cell.getChildren().add(new Label("B"));
                    cell.setAlignment(Pos.CENTER);
                }
                if(currentType == '#'){
                    cell.setStyle("-fx-background-color: #555555;");
                }
                if(currentType =='.'){
                    cell.setStyle("-fx-background-color: WHITE;");
                }
                gridPane.add(cell,c,r); //add the cell, to the correct 'column' and 'row'.
            }
        }

    }






    //returns the GridPane containing the board
    public GridPane getGridPane(){
        return this.gridPane;
    }

}
