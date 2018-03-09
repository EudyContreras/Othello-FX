package com.eudycontreras.othello.views;

import com.eudycontreras.othello.application.OthelloSettings;
import com.eudycontreras.othello.enumerations.PieceType;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Tester extends Application {
 
    @Override
    public void start(Stage stage) {
 
        Hyperlink hyperlink = new Hyperlink("Go to Eudy Contreras Git Repo");
 
        hyperlink.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                getHostServices().showDocument("https://github.com/EudyContreras");
            }
        });
 
        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.getChildren().addAll(hyperlink);
        
        GameAboutView aboutView = new GameAboutView();
        aboutView.initialize( 500, 150);
        aboutView.setShowing(true);
        aboutView.showAboutView();
        
        Scene scene = new Scene(aboutView,Color.TRANSPARENT);	
		scene.getStylesheets().add(getClass().getResource(OthelloSettings.STYLESHEET).toExternalForm());

		stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
 
    

}
