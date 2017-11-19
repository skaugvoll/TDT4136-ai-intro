package Astar.Views;

import Astar.Algo.*;

import Astar.Controllers.DrawBoard;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.util.List;

/**
 * Created by sigveskaugvoll on 30.09.2016.
 */
public class SelectBoardView extends Scene {

    private final Stage primaryStage; //the application window,
    private final Stage oldStage; //the original application window specifications.
    private boolean gridLines = false; //change to false to deactivate the grid lines visibility.

    private final BorderPane root; //the scene "on" the stage



    private CreateBoard b; //object to represent the board / task to solve

    private List<Node> path; //variable to hold the path on the task
    private List<Node> closedSet;
    private List<Node> openSet;

    private GridPane grid = new GridPane(); //The GridPane, we want to add all our layout elements to.

    private DrawBoard board; //Object to fill and generate a GUI - view of the task to solve
    private GridPane boardPane; // ALGORITHM GridPane!.


//method to select correct task, run the algorithm on correct task, with correct algorithm, and represent it on the screen.
    public SelectBoardView(Stage primaryStage) {
        //Declare the necessary
        super(new BorderPane());
        root = (BorderPane) super.getRoot();
        this.primaryStage = primaryStage;
        oldStage = primaryStage;


//Everything that is not necessary for the GUI to start:

        //set the size of the application window.
        primaryStage.setMinWidth(1000);
        primaryStage.setMaxHeight(600);
        root.setMinSize(1000,500);



//Initiate fields, buttons for the GUI
        VBox headerBox = new VBox(); //VBox to wrap header label
        Label header = new Label("Choose the board, you want to solve"); //header label /txt

        header.setAlignment(Pos.TOP_CENTER); //center align the header

        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().add(header);

        //A.1.
        Button a11 = new Button("A.1.1");
        Button a12 = new Button("A.1.2");
        Button a13= new Button("A.1.3");
        Button a14 = new Button("A.1.4");

        //A.2.
        Button a21 = new Button("A.2.1");
        Button a22 = new Button("A.2.2");
        Button a23 = new Button("A.2.3");
        Button a24 = new Button("A.2.4");

        //A.3.
        Button aD = new Button("Dijks");
        Button aB = new Button("BFS");


//Handle buttonEvents!
        a11.setOnAction(e -> {
            handleButtonAction("../boards/board-1-1.txt","A.1.1",'a');
        });

        a12.setOnAction(e -> {
            handleButtonAction("../boards/board-1-2.txt","A.1.2",'a');
        });

        a13.setOnAction(e -> {
            handleButtonAction("../boards/board-1-3.txt","A.1.3",'a');
        });

        a14.setOnAction(e -> {
            handleButtonAction("../boards/board-1-4.txt","A.1.4",'a');
        });

        a21.setOnAction(e -> {
            handleButtonAction("../boards/board-2-1.txt","A.2.1",'a');
        });

        a22.setOnAction(e -> {
            handleButtonAction("../boards/board-2-2.txt","A.2.2",'a');
        });

        a23.setOnAction(e -> {
            handleButtonAction("../boards/board-2-3.txt","A.2.3",'a');
        });

        a24.setOnAction(e -> {
            handleButtonAction("../boards/board-2-4.txt","A.2.4",'a');
        });

        aD.setOnAction(e -> {
            handleButtonAction("../boards/board-2-4.txt","A.3",'d');
        });

        aB.setOnAction(e -> {
            handleButtonAction("../boards/board-2-4.txt","A.3",'b');
        });


        //Define how many rows needed,
        int rowsNeeded = 11;

        for(int r = 0; r < rowsNeeded; r++){
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(40);
            grid.getRowConstraints().add(rowConst);
        }

        //Define how many column needed.
        int colNeeded = 2;
        for(int c = 0; c < colNeeded; c++){
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(60);
            grid.getColumnConstraints().add(colConst);
        }

//Add to the scene, all the layout elements we need

        //A.1.
        grid.add(a11,0,0);
        grid.add(a12,0,1);
        grid.add(a13,0,2);
        grid.add(a14,0,3);

        //A.2.
        grid.add(a21,1,0);
        grid.add(a22,1,1);
        grid.add(a23,1,2);
        grid.add(a24,1,3);

        //A.3
        grid.add(aB,0,4);
        grid.add(aD,1,4);

        //position element.
        grid.setAlignment(Pos.CENTER);

        //show element on screen:
        root.setTop(headerBox);
        root.setCenter(grid);

    }

//Handle button event!
    private void handleButtonAction(String url, String taskNumber, char c) {
        // Button was clicked, do something...
        this.b = new CreateBoard(url); //create a new Bord
        this.b.fillData(); //fill the board with the correct board -data

        //check what algorithm to use.
        if(c == 'b'){
            BreadthFirstSearchAlgorithm algo = new BreadthFirstSearchAlgorithm();
            algo.algorithm(b.startNode);
            this.path = algo.getCameFrom();
            this.closedSet = algo.getClosedSet();
            this.openSet = algo.getOpenSet();
        }
        else if(c == 'd'){
            DjikstraAlgorithm algo = new DjikstraAlgorithm();
            algo.algorithm(b.startNode,b.goalNode);
            this.path = algo.getCameFrom();
            this.closedSet = algo.getClosedSet();
            this.openSet = algo.getOpenSet();

        }
        else{
            AstarAlgorithm algo = new AstarAlgorithm();
            algo.algorithm(b.startNode,b.goalNode);
            this.path = algo.getCameFrom();
            this.closedSet = algo.getClosedSet();
            this.openSet = algo.getOpenSet();

        }

//create a GUI representation fo the 2D-Array board
        this.board = new DrawBoard(b,path, closedSet, openSet);

        //set boardPane = to the DrawBoard-GUI generated board-representation.
        this.boardPane = board.getGridPane();
        boardPane.setGridLinesVisible(gridLines);


        //Add a header, telling what task, is shown
        Label task = new Label(taskNumber);
        task.setAlignment(Pos.CENTER);

        //add a button to get back to "select task-menu".
        Button backBtn = new Button("Back to Algorithms");
        backBtn.setAlignment(Pos.BOTTOM_CENTER);


        //handle the backBtn event.
        backBtn.setOnAction(h -> {
            primaryStage.setScene(new SelectBoardView(this.oldStage));
        });


        //Create a VBOX for the task-label, and center the label.
        VBox taskBox = new VBox();
        taskBox.setAlignment(Pos.CENTER);
        taskBox.getChildren().add(task);

//set the scene of the stage. (change scenes.)
        root.setTop(taskBox);
        root.setCenter(boardPane);
        root.setBottom(backBtn);
    }

}
