package Astar;


import Astar.Views.SelectBoardView;
import javafx.application.Application;

import javafx.scene.Scene;

import javafx.stage.Stage;



/**
 * Created by sigveskaugvoll on 26.09.2016.
 */
public class Main extends Application {

   @Override
    public void start(Stage primaryStage) throws Exception {


       Scene firstscene = new SelectBoardView(primaryStage); //Create the "select scene menu"

       primaryStage.setTitle("Hello A* lover!"); //edit, the metadata of the application window / stage

       primaryStage.setScene(firstscene); //set the first scene on stage

       primaryStage.show(); //Show the application!

    }

    public static void main(String[] args) {
        launch(args);
    }
}
