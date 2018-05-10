package com.eudycontreras.othello.views;

import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.othello.application.OthelloGameView;
import com.eudycontreras.othello.application.OthelloSettings;
import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.enumerations.BoardCellType;
import com.eudycontreras.othello.enumerations.PieceType;
import com.eudycontreras.othello.threading.ThreadTimer;
import com.eudycontreras.othello.threading.TimeSpan;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
public class GameBoardView extends StackPane {
	
	private static final String CELL_EVEN_ID = "game-board-cell-1-color";
	private static final String CELL_ODD_ID = "game-board-cell-2-color";
	
	private static final String BOARD_COLOR = "game-board-color";
	private static final String GRID_COLOR = "game-board-grid-color";
	
	private GameCellView[][] gameCellView = null;
	private StackPane[][] gameCellSlot = null;
	private Rectangle[][] gameCellOver = null;
	private Text[][] gameCellInfo = null;

	private DropShadow shadow = new DropShadow();
	
	private GridPane gridPane = new GridPane();

	private StackPane container = new StackPane();
	
	private OthelloGameView othelloGame = null;
	
	private ScaleTransition scale = new ScaleTransition();
	
	private List<GamePieceView> pieces = new ArrayList<>();
	private List<GameMoveIndicator> indicators = new ArrayList<>();
	
	private GamePieceView whiteShadow = new GamePieceView(PieceType.WHITE, (OthelloSettings.CELL_SIZE / 3), 0, 0, 0);
	private GamePieceView blackShadow = new GamePieceView(PieceType.BLACK, (OthelloSettings.CELL_SIZE / 3), 0, 0, 0);
	
	public GameBoardView(OthelloGameView othelloGame, Region parent, int rows, int cols) {
		this.othelloGame = othelloGame;
		
		gameCellSlot = new StackPane[rows][cols];
		gameCellOver = new Rectangle[rows][cols];
		gameCellView = new GameCellView[rows][cols];
		gameCellInfo = new Text[rows][cols];
		
		shadow.setRadius(18);
		shadow.setSpread(0.35);
		shadow.setOffsetY(12.0);
		shadow.setOffsetX(-12.0);
		shadow.setColor(Color.rgb(0, 0, 0, 0.6));
		
		scale.setAutoReverse(true);
		scale.setCycleCount(Transition.INDEFINITE);
		scale.setDuration(Duration.millis(450));
		scale.setFromX(0.8);
		scale.setFromY(0.8);
		scale.setToX(1.0);
		scale.setToY(1.0);
	
		gridPane.setPickOnBounds(false);
		gridPane.setPrefWidth(othelloGame.getGameSize()* 0.7);
		gridPane.setPrefHeight(othelloGame.getGameSize() * 0.7);
		gridPane.setHgap(getGridGap());
		gridPane.setVgap(getGridGap());

		createBoardCells();
		
		gridPane.setId(GRID_COLOR);
		gridPane.setPadding(new Insets(0));
		
		container.setId(BOARD_COLOR);
		container.setPickOnBounds(false);
		container.getChildren().add(gridPane);

		setPickOnBounds(false);
		setPadding(new Insets(20, 0, 20, 0));
		getChildren().add(container);
		
		addActionHandling();
	}
	
	private double getGridGap(){
		switch(OthelloSettings.DEFAULT_BOARD_GRID_SIZE){
		case 4:
			return 10;
		case 6:
			return 7.5;
		case 8:
			return 5;
		default:
			return 5;
		}
	}
	
	private void createBoardCells(){
		for (int i = 0; i < gameCellSlot.length; i++) {
			for (int j = 0; j < gameCellSlot.length; j++) {

				gameCellSlot[i][j] = new StackPane();
				gameCellView[i][j] = new GameCellView();
				gameCellOver[i][j] = new Rectangle(OthelloSettings.CELL_SIZE, OthelloSettings.CELL_SIZE, Color.TRANSPARENT);
				gameCellInfo[i][j] = new Text(i + ", "+ j);
				
				gameCellInfo[i][j].setFill(Color.WHITE);
				gameCellInfo[i][j].setOpacity(0.5);
				gameCellInfo[i][j].setFont(Font.font(null, FontWeight.BLACK, 16));
				gameCellInfo[i][j].setVisible(OthelloSettings.DEBUG_GAME);
				
				gameCellSlot[i][j].setId(CELL_EVEN_ID);
				gameCellSlot[i][j].getChildren().add(gameCellOver[i][j]);
				gameCellSlot[i][j].getChildren().add(gameCellView[i][j]);
				gameCellSlot[i][j].getChildren().add(gameCellInfo[i][j]);
				gameCellSlot[i][j].setPrefSize(OthelloSettings.CELL_SIZE, OthelloSettings.CELL_SIZE);

				gameCellOver[i][j].setOpacity(0.9);
				gameCellView[i][j].setPickOnBounds(false);
				gameCellView[i][j].setCellType(BoardCellType.EVEN);
				
				gridPane.add(gameCellSlot[i][j], i, j);
			}
		}

		for (int i = 0; i < gameCellOver.length; i++) {
			for (int j = 0; j < gameCellOver.length; j++) {
				if (j % 2 == 0) {
					if (i % 2 == 0) {
						gameCellView[i][j].setCellType(BoardCellType.ODD);
						gameCellSlot[i][j].setId(CELL_ODD_ID);
					}
				}
				if (j % 2 != 0) {
					if (i % 2 != 0) {
						gameCellView[i][j].setCellType(BoardCellType.ODD);
						gameCellSlot[i][j].setId(CELL_ODD_ID);
					}
				}
			}
		}
	}
	
	private void addActionHandling(){
		for (int i = 0; i < gameCellSlot.length; i++) {
			for (int j = 0; j < gameCellSlot.length; j++) {
				
				final int row = i;
				final int col = j;
				
				gameCellSlot[i][j].setOnMouseEntered(e -> {
					othelloGame.getViewCallback().setMouseEnteredEvent(e, row, col);
				});
				gameCellSlot[i][j].setOnMouseExited(e -> {
					othelloGame.getViewCallback().setMouseExitedEvent(e, row, col);
				});
				gameCellSlot[i][j].setOnMousePressed(e -> {
					othelloGame.getViewCallback().setMousePressedEvent(e, row, col);
					e.consume();
				});
				gameCellSlot[i][j].setOnMouseReleased(e -> {
					e.consume();
				});
				gameCellSlot[i][j].setOnMouseDragged(e -> {
					e.consume();
				});
			}
		}
	}
	
	public void applyPossibleMoveEffect(PieceType type, int row, int col){
		
		GameMoveIndicator indicator = new GameMoveIndicator(type, (OthelloSettings.CELL_SIZE / 3.6), row,col);
	
		indicators.add(indicator);
		
		gameCellSlot[row][col].getChildren().add(0, indicator);
		
		indicator.fadeIn();
	}
	
	public void removePossibleMoveEffect(){	
		for(GameMoveIndicator indicator: indicators){
			
			indicator.removeFromBoard(()->{
				gameCellSlot[indicator.getRowIndex()][indicator.getColIndex()].getChildren().remove(indicator);
				
				if(UserSettings.USE_ANIMATION)
				indicators.remove(indicator);
			});
		}	
		
		if(!UserSettings.USE_ANIMATION){
			indicators.clear();
		}
		
	}
	
	public void applyTraversableEffect(PieceType type, int row, int col){
		if(UserSettings.USE_ANIMATION){
			ScaleTransition scaleCell = new ScaleTransition();
			
			scaleCell.setNode(gameCellSlot[row][col]);
			scaleCell.setDuration(Duration.millis(170));
			scaleCell.setInterpolator(Interpolator.EASE_OUT);
			scaleCell.setFromX(gameCellSlot[row][col].getScaleX());
			scaleCell.setFromY(gameCellSlot[row][col].getScaleY());
			scaleCell.setToX(1.1);
			scaleCell.setToY(1.1);
			scaleCell.play();
		}else{
			gameCellSlot[row][col].setScaleX(1.1);
			gameCellSlot[row][col].setScaleY(1.1);
		}
		
		gameCellSlot[row][col].toFront();
		gameCellSlot[row][col].setEffect(shadow);
		switch(type){
		case BLACK:
			gameCellSlot[row][col].getChildren().add(1,blackShadow);
			
			if(UserSettings.USE_ANIMATION){
			scale.setNode(blackShadow);
			scale.play();
			}
			break;
		case WHITE:
			gameCellSlot[row][col].getChildren().add(1,whiteShadow);
			if(UserSettings.USE_ANIMATION){
			scale.setNode(whiteShadow);
			scale.play();
			}
			break;
		default:
			break;
		}
	}
	
	public void removeTraversableEffect(PieceType type,int row, int col){
		removeTraversableEffect(type,row,col,false);
	}
	
	public void removeTraversableEffect(PieceType type,int row, int col, boolean state){
		if(UserSettings.USE_ANIMATION){
			ScaleTransition scaleCell = new ScaleTransition();
			
			scaleCell.setNode(gameCellSlot[row][col]);
			scaleCell.setDuration(Duration.millis(170));
			scaleCell.setInterpolator(Interpolator.EASE_OUT);
			scaleCell.setFromX(gameCellSlot[row][col].getScaleX());
			scaleCell.setFromY(gameCellSlot[row][col].getScaleY());
			scaleCell.setToX(1);
			scaleCell.setToY(1);
			scaleCell.play();
		}else{
			gameCellSlot[row][col].setScaleX(1);
			gameCellSlot[row][col].setScaleY(1);

		}
		gameCellSlot[row][col].setEffect(null);
		
		if(!state){
			scale.stop();

			gameCellSlot[row][col].getChildren().remove(blackShadow);
			gameCellSlot[row][col].getChildren().remove(whiteShadow);
		}
		
	}
	
	public void setPiece(PieceType type, int row, int col){
		setPiece(type, row, col, 100);
	}
	
	public void setPiece(PieceType type, int row, int col, int delay){
		setPiece(type, row, col, delay, false);	
	}
	
	public void setPiece(PieceType type, int row, int col, int delay, boolean initial){
		GamePieceView piece = new GamePieceView(type, (OthelloSettings.CELL_SIZE / 3), delay, row, col, initial);
		
		pieces.add(piece);
		
		gameCellView[row][col].addPiece(piece);	

	}
		
	public void setPossibleCells(PieceType pieceType, List<Index> indexes) {}

	public void resetCells(){
		for (int i = 0; i < gameCellView.length; i++) {
			for (int j = 0; j < gameCellView[i].length; j++) {
				gameCellOver[i][j].setFill(Color.TRANSPARENT);
			}
		}
	}
	
	public void swapPiece(PieceType type, int row, int col){
		
		pieces.remove(gameCellView[row][col].getGamePiece());
		
		gameCellView[row][col].getGamePiece().removeFromBoard(()->{
			setPiece(type, row, col, 0);
		});
	}
	
	public void resetBoard() {
		resetBoard(800);
	}
	
	public void resetBoard(int delay) {	
		
		othelloGame.getViewCallback().onGamePaused();
		
		if(UserSettings.USE_ANIMATION){

			ThreadTimer.schedule(TimeSpan.millis(500+(UserSettings.TURN_INTERVAL + UserSettings.MIN_SEARCH_TIME)), ()-> {
				Platform.runLater(()->{
					othelloGame.getGameScoreView().resetScore();
					
					for(GamePieceView piece : pieces){
						piece.removeFromBoard(null);
					}

					
					if(!pieces.isEmpty())
					pieces.get(pieces.size()-1).setEndAction(()-> {
					
							pieces.clear();
							
							othelloGame.getViewCallback().onGameResumed();
							othelloGame.getViewCallback().resetBoard(delay);
							
							othelloGame.setResetting(false);
						
					});
					
				});
			});
			
			return;
		}
		
		othelloGame.getGameScoreView().resetScore();
		
		for(GamePieceView piece : pieces){
			piece.removeFromBoard(null);
		}
		
		pieces.clear();

		othelloGame.getViewCallback().onGameResumed();
		othelloGame.getViewCallback().resetBoard(0);
		
	     othelloGame.setResetting(false);
		
	}

	public void toggleInfo() {
		for (int i = 0; i < gameCellInfo.length; i++) {
			for (int j = 0; j < gameCellInfo[i].length; j++) {
				gameCellInfo[i][j].setVisible(!gameCellInfo[i][j].isVisible());
			}
		}

		OthelloSettings.DEBUG_GAME = !OthelloSettings.DEBUG_GAME;
	}
}
