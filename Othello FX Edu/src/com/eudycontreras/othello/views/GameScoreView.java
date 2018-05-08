
package com.eudycontreras.othello.views;

import com.eudycontreras.othello.application.OthelloGameView;
import com.eudycontreras.othello.enumerations.PieceType;
import com.eudycontreras.othello.enumerations.PlayerType;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
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
public class GameScoreView extends StackPane{

	private static final String LABEL = "othello-label";

	private GamePieceView[] playerChip = new GamePieceView[2];
	
	private Text[] playerScore = new Text[2];
	private Text[] playerName = new Text[2];
	
	private VBox[] box = new VBox[2];
		
	private Rectangle[] graphics = new Rectangle[2];

	private StackPane background = new StackPane();

	private OthelloGameView gameView;
	
	private DropShadow textGlow;

	public GameScoreView(OthelloGameView gameView, double width, double height) {
		this.gameView = gameView;
		
		this.playerChip[0] = new GamePieceView(PieceType.WHITE, 25, 0,0,0);
		this.playerChip[1] = new GamePieceView(PieceType.BLACK, 25, 0,0,0);
		
		this.playerName[0] = new Text(gameView.getPlayerOne());
		this.playerName[1] = new Text(gameView.getPlayerTwo());
		
		this.playerScore[0] = new Text();
		this.playerScore[1] = new Text();
		
        this.textGlow = new DropShadow();
        this.textGlow.setColor(Color.DODGERBLUE);
        this.textGlow.setBlurType(BlurType.GAUSSIAN);
        
		this.setupText();
		this.createScoreBox(width, height);
	}

	private void setupText(){		
		for(GamePieceView name : playerChip){
			name.setTranslateY(-5);
		}
		for(Text name : playerName){
			name.setFont(Font.font(null, FontWeight.BLACK, 16));
			name.setId("player_label");
			name.setFill(Color.WHITE);
			name.setTextAlignment(TextAlignment.CENTER);
		}
		
		for(Text score : playerScore){
			score.setText("00");
			score.setId(LABEL);
			score.setFont(Font.font(null, FontWeight.NORMAL, 30));
			score.setFill(Color.WHITE);
			score.setTextAlignment(TextAlignment.CENTER);
		}
	}
	
	private void createScoreBox(double width, double height){
			
		StackPane[] container = new StackPane[2];
	
		HBox holder = new HBox(10);
		
		for(int i = 0; i<container.length; i++){
			container[i] = new StackPane();
			graphics[i] = new Rectangle();
			box[i] = new VBox(0);
			box[i].setAlignment(Pos.CENTER);
		}
		
		for(Rectangle graphic : graphics){
			graphic.setWidth((width / 2) - width * 0.1);
			graphic.setHeight(height*0.35);
			graphic.setArcHeight(25);
			graphic.setArcWidth(25);
			graphic.setFill(Color.BLACK);
			graphic.setStroke(Color.rgb(100, 170, 0));
			graphic.setStrokeWidth(4);
		}
		
		container[0].getChildren().add(graphics[0]);
		container[1].getChildren().add(graphics[1]);
		
		container[0].setPadding(new Insets(4,0,10,0));
		container[1].setPadding(new Insets(4,0,10,0));
		
		container[0].getChildren().add(playerScore[0]);
		container[1].getChildren().add(playerScore[1]);

		box[0].getChildren().add(playerName[0]);
		box[1].getChildren().add(playerName[1]);
		
		box[0].getChildren().add(container[0]);
		box[1].getChildren().add(container[1]);
		
		box[0].getChildren().add(playerChip[0]);
		box[1].getChildren().add(playerChip[1]);
		
		holder.getChildren().addAll(box);
		holder.setAlignment(Pos.CENTER);
		
		background.setId("game-score-board");
		background.setMaxWidth(width);
		background.setMinHeight(height);
		
		setPadding(new Insets(8,0,8,0));
		getChildren().add(background);
		getChildren().add(holder);
	}

	public void setBorderColor(Color color){
		for(Rectangle graphic : graphics){
			graphic.setStroke(color);
			graphic.setStrokeWidth(4);
		}
	}

	public void showFrame(boolean state) {
		background.setVisible(state);
	}
	
	public void setSpacing(int spacing) {
		box[0].setSpacing(spacing);
		box[1].setSpacing(spacing);
	}
	
	public void resetScore(){
		updatePlayerScore(PlayerType.PLAYER_ONE, 0);
		updatePlayerScore(PlayerType.PLAYER_TWO, 0);
	}
	
	public void updateBoardScore(int playerOne, int playerTwo){
		playerName[0].setText(gameView.getPlayerOne());
		playerName[1].setText(gameView.getPlayerTwo());
		updatePlayerScore(PlayerType.PLAYER_ONE, playerOne);
		updatePlayerScore(PlayerType.PLAYER_TWO, playerTwo);
	}
	
	public void updatePlayerScore(PlayerType number, int score) {
		if (score < 10){
			getPlayerScore(number).setText("0"+String.valueOf(score));
		}
		else{
			getPlayerScore(number).setText(String.valueOf(score));
		}
	}
	
	private Text getPlayerScore(PlayerType number){
		if(number == PlayerType.PLAYER_ONE){
			return playerScore[0];
		}else{
			return playerScore[1];
		}
	}
}
