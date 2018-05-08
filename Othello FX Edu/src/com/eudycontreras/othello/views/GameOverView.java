package com.eudycontreras.othello.views;

import com.eudycontreras.othello.application.OthelloGameView;
import com.eudycontreras.othello.application.OthelloSettings;
import com.eudycontreras.othello.enumerations.PieceType;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.UserSettings;

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
public class GameOverView extends Group {
	
	private static final String LAYOUT_STYLE = "game-end-view-style";

	private VBox layout = new VBox(12);
	
	private HBox buttons = new HBox(40);
	
	private BorderPane boxLayout = new BorderPane();
	
	private GameScoreView gameScoreView;
	
	private OthelloGameView gameView;
	
	private StackPane centerBoard = new StackPane();
	
	private StackPane centerLayout = new StackPane();
	
	private Button continueButton = new Button("Continue");
	private Button quitButton = new Button("Quit");
	
	private Text winnerLabel = new Text();

	private Runnable continueAction = null;
	
	private boolean showing = false;
	
	public GameOverView(OthelloGameView gameView){	
		this.gameView = gameView;
		layout.getChildren().add(boxLayout);
		
		centerLayout.getChildren().add(centerBoard);
		centerLayout.getChildren().add(winnerLabel);
		
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().add(continueButton);
		buttons.getChildren().add(quitButton);
		
		layout.getChildren().add(buttons);
		layout.setPadding(new Insets(30));
		layout.setId(LAYOUT_STYLE);
		
		layout.setAlignment(Pos.CENTER);
		
		getChildren().add(layout);
		setMouseTransparent(true);
		setVisible(false);
	}

	public void initialize(OthelloGameView gameView, double width, double height){
		this.gameView = gameView;
		
		gameScoreView = new GameScoreView(gameView, width*0.9, height*1.1);
		gameScoreView.showFrame(false);
		gameScoreView.setSpacing(3);
		gameScoreView.setBorderColor(Color.rgb(60, 120, 0));
		
		boxLayout.setTop(centerLayout);
		boxLayout.setCenter(gameScoreView);
		
		centerBoard.setPrefWidth(width);
		centerBoard.setPrefHeight(height*0.8);
		centerBoard.setId("game-end-view-center-style");
	
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.RED);
		dropShadow.setRadius(15);
		dropShadow.setSpread(0.25);
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
		
		winnerLabel.setFill(Color.WHITE);
		winnerLabel.setFont(Font.font(null, FontWeight.BOLD, 44));
		winnerLabel.setId("winner-label");
		winnerLabel.setEffect(dropShadow);
		
		continueButton.setPrefWidth(130);
		continueButton.setId("game-menu-end-dialog-button");
		continueButton.setOnAction(e->{
			if(continueAction != null){			
				continueAction.run();
			}
			animateHide(500);
		});
		
		quitButton.setPrefWidth(130);
		quitButton.setId("game-menu-end-dialog-button");
		quitButton.setOnAction(e->{
			gameView.closeGame();
		});
	}
	
	public void initialize(double width, double height){
		gameScoreView = new GameScoreView(gameView, width*0.9, height*1.1);
		gameScoreView.showFrame(false);
		gameScoreView.setSpacing(3);
		gameScoreView.setBorderColor(Color.rgb(60, 120, 0));
		
		boxLayout.setTop(centerLayout);
		boxLayout.setCenter(gameScoreView);
		
		centerBoard.setPrefWidth(width);
		centerBoard.setPrefHeight(height*0.8);
		centerBoard.setId("game-end-view-center-style");
	
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.RED);
		dropShadow.setRadius(15);
		dropShadow.setSpread(0.25);
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
		
		winnerLabel.setFill(Color.WHITE);
		winnerLabel.setFont(Font.font(null, FontWeight.BOLD, 44));
		winnerLabel.setId("winner-label");
		winnerLabel.setEffect(dropShadow);
		
		continueButton.setPrefWidth(130);
		continueButton.setId("game-menu-end-dialog-button");
		continueButton.setOnAction(e->{
			if(continueAction != null){			
				continueAction.run();
			}
			animateHide(500);
		});
		
		quitButton.setPrefWidth(130);
		quitButton.setId("game-menu-end-dialog-button");
		quitButton.setOnAction(e->{
			System.exit(0);
		});
	}
	
	public void setPlayerScore(int scoreOne, int scoreTwo){
		gameScoreView.updateBoardScore(scoreOne, scoreTwo);
	}
	
	public void setContinueAction(Runnable action){
		this.continueAction = action;
	}
	
	public void showEndGameView(PieceType type){
		System.out.println("Showing GAME OVER!!!!");

		setVisible(true);
		setMouseTransparent(false);
		
		switch(type){
		case WHITE:
			winnerLabel.setText(gameView.getPlayerOne() + " Wins");
			break;
		case BLACK:
			winnerLabel.setText(gameView.getPlayerTwo() + " Wins");
			break;
		case NONE:
			winnerLabel.setText("DRAW");
			break;
		default:
			break;
			
		}
		
		animateShow(400);
	}
	
	public void setShowing(boolean showing) {
		this.showing = showing;
	}
	
	public boolean isShowing(){
		return showing;
	}
	
	public void hideEndGameView(){
		animateHide(200);
	}
	
	private void animateShow(int miliseconds){
		
		if(!UserSettings.USE_ANIMATION){
			this.setOpacity(1);
			this.setScaleX(1);
			this.setScaleY(1);
			return;
		}

		ScaleTransition scaleTransition = new ScaleTransition();
		
		FadeTransition fadeTransition = new FadeTransition();
		
		scaleTransition.setNode(this);
		fadeTransition.setNode(this);
		
		fadeTransition.setDuration(Duration.millis(miliseconds));

		fadeTransition.setFromValue(0);
		
		fadeTransition.setToValue(1);
		
		fadeTransition.play();
		
		scaleTransition.setDuration(Duration.millis(miliseconds));
		scaleTransition.setInterpolator(Interpolator.SPLINE(1, 0, 1, 1));		
		
		scaleTransition.setFromX(0.3);
		scaleTransition.setFromY(0.3);
		
		scaleTransition.setToX(1);
		scaleTransition.setToY(1);
		
		scaleTransition.play();
	}
	
	private void animateHide(int miliseconds){
		
		if(!UserSettings.USE_ANIMATION){
			this.setOpacity(0);
			this.setScaleX(0);
			this.setScaleY(0);
			
			setMouseTransparent(true);
			setVisible(false);
			setShowing(false);
			return;
		}

		
		ScaleTransition scaleTransition = new ScaleTransition();
		
		FadeTransition fadeTransition = new FadeTransition();
		
		scaleTransition.setNode(this);
		fadeTransition.setNode(this);
		
		
		fadeTransition.setDuration(Duration.millis(miliseconds));

		fadeTransition.setFromValue(1);
		
		fadeTransition.setToValue(0);
		
		fadeTransition.play();
	
		scaleTransition.setDuration(Duration.millis(miliseconds));

		scaleTransition.setFromX(1);
		scaleTransition.setFromY(1);
		
		scaleTransition.setToX(0);
		scaleTransition.setToY(0);
		
		scaleTransition.setOnFinished(e ->{
			setMouseTransparent(true);
			setVisible(false);
			setShowing(false);
		});
		scaleTransition.play();
	
	}
}
