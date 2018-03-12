package com.eudycontreras.othello.views;


import com.eudycontreras.othello.application.OthelloGameView;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * 
 * @author Eudy Contreras
 */
public class GameInfoView extends VBox {

	private VBox[] infoHolder = new VBox[6];
	
	private Text[] infoContent = new Text[6];
	
	private Button[] infoLabel = new Button[6];
	
	private StackPane[] infoPanels = new StackPane[6];
	
	private TextArea console = new TextArea("|");
	
	private OthelloGameView othello;
	private Scene scene;

    public GameInfoView(OthelloGameView othello, Scene scene){
    	this.othello = othello;
    	this.scene = scene;
    	this.setSpacing(25);
    	this.setPadding(new Insets(25,20,12,20));
    	this.setPrefHeight(othello.getGameMenuView().getHeight());
    	this.createInfoPanels();
    	this.setLabels();
    }    
    
    private void setLabels(){
    	setName(0, "State");
    	setName(1, "Search Depth");
    	setName(2, "Nodes Pruned");
    	setName(3, "Leafs Reached");
    	setName(4, "Nodes Examined");
    	setName(5, "Console");
    	
    	console.setEditable(false);
    	console.setPadding(new Insets(20));
    	
    	infoPanels[0].setId("info-panel-state-style");
    	infoPanels[0].setPrefHeight(80);
    	
    	Rectangle graphic = new Rectangle();
    	graphic.setWidth(infoPanels[0].getPrefWidth()*0.9);
		graphic.setHeight(infoPanels[0].getPrefHeight()*0.7);
		graphic.setArcHeight(20);
		graphic.setArcWidth(20);
		graphic.setFill(Color.BLACK);
		graphic.setStroke(Color.rgb(100, 170, 0));
		graphic.setStrokeWidth(4);
		
		infoPanels[0].getChildren().add(0, graphic);
		
		infoContent[0].setFont(Font.font(null, FontWeight.BOLD, 18));
    	infoContent[0].setText("State of game");
		
    	infoPanels[5].setPrefHeight(100 + (50 * 4));
    	
    	Rectangle graphic2 = new Rectangle();
    	graphic2.setWidth(infoPanels[5].getPrefWidth()*0.9);
    	graphic2.setHeight(infoPanels[5].getPrefHeight()*0.9);
    	graphic2.setArcHeight(20);
    	graphic2.setArcWidth(20);
		graphic2.setFill(Color.BLACK);
		graphic2.setStroke(Color.rgb(100, 170, 0));
		graphic2.setStrokeWidth(4);
		
    	infoPanels[5].getChildren().clear();
    	infoPanels[5].getChildren().add(graphic2);
    	infoPanels[5].getChildren().add(console);
    	
    }
    private void createInfoPanels(){
    	
    	for(int i = 0; i<infoHolder.length; i++){
    		
    		infoHolder[i] = new VBox(0);
    		
    		infoHolder[i].setAlignment(Pos.CENTER);
    		
    		infoContent[i] = new Text("0.0");
    		infoContent[i].setFill(Color.rgb(220, 220, 220));
    		infoContent[i].setFont(Font.font(null, FontWeight.BOLD, 18));
    		infoContent[i].setId("info-label");
    		infoContent[i].setTextAlignment(TextAlignment.CENTER);
    		
    		infoLabel[i] = new Button("Label");
    		infoLabel[i].setFont(Font.font(null, FontWeight.BOLD, 14));
    		infoLabel[i].setId("info-label");
    		infoLabel[i].setPrefWidth(220);
    		infoLabel[i].setTextAlignment(TextAlignment.CENTER);
    		
    		infoPanels[i] = new StackPane();
    		infoPanels[i].setId("info-panel-state-style");
    		infoPanels[i].setPrefHeight(50);
    		infoPanels[i].setPrefWidth(220);
    		infoPanels[i].getChildren().add(infoContent[i]);
    		
    		if(i > 0){
    			Rectangle graphic = new Rectangle();
            	graphic.setWidth(infoPanels[i].getPrefWidth()*0.9);
        		graphic.setHeight(infoPanels[i].getPrefHeight()*0.7);
        		graphic.setArcHeight(20);
        		graphic.setArcWidth(20);
        		graphic.setFill(Color.BLACK);
        		graphic.setStroke(Color.rgb(100, 170, 0));
        		graphic.setStrokeWidth(4);
        		
        		infoPanels[i].getChildren().add(0, graphic);
    		}
    		
    		infoHolder[i].getChildren().add(infoLabel[i]);
    		infoHolder[i].getChildren().add(infoPanels[i]);
    		
    		getChildren().add(infoHolder[i]);
    		
    	}
    }
    
    public void setName(int index, String name){
    	this.infoLabel[index].setText(name);
    }
    
    public void updateInfo(int index, String info){
    	this.infoContent[index].setText(info);
    }
    
    public void printToConsole(Object obj){
    	if(obj == null) return;
    	
    	Platform.runLater(()->{
        	this.console.appendText(obj.toString() + "\n");
    	});

    }
    
	public void resetConsole() {
		Platform.runLater(()->{
        	this.console.clear();
    	});
	}
}