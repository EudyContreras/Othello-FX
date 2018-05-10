package com.eudycontreras.othello.application;

import java.util.List;

import com.eudycontreras.othello.callbacks.EventCallbackController;
import com.eudycontreras.othello.callbacks.EventCallbackView;
import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.capsules.TrailWrapper;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.GameEndState;
import com.eudycontreras.othello.enumerations.PieceType;
import com.eudycontreras.othello.enumerations.PlayerType;
import com.eudycontreras.othello.models.GameBoardCell;
import com.eudycontreras.othello.views.GameAboutView;
import com.eudycontreras.othello.views.GameBlurView;
import com.eudycontreras.othello.views.GameBoardView;
import com.eudycontreras.othello.views.GameInfoView;
import com.eudycontreras.othello.views.GameMenuView;
import com.eudycontreras.othello.views.GameOverView;
import com.eudycontreras.othello.views.GameScoreView;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
public class OthelloGameView{

	private boolean resetting;

	private int index = 0;
	
	private int boardSize = OthelloSettings.DEFAULT_BOARD_GRID_SIZE;
	
    private double xOffset = 0;
    private double yOffset = 0;
    
	private double menuWidth = OthelloSettings.DEFAULT_MENU_WIDTH;
	private double gameSize = OthelloSettings.DEFAULT_BOARD_GRID_SIZE;
    
    private String playerOne = OthelloSettings.PLAYER_ONE;
    private String playerTwo = OthelloSettings.PLAYER_TWO;
    
	private BorderPane layout = new BorderPane();
	
    private StackPane root = new StackPane();
    private StackPane gameScreen = new StackPane();
    private StackPane overScreen = new StackPane();
    
    private ImageView overlay = new ImageView();
 
    private Rectangle filler = new Rectangle();				
	
    private Stage primaryStage = null;
    
	private Scene scene = null;
	
	private Othello othello;
	
	private GameAboutView gameAboutView;
    private GameScoreView gameScoreView;
    private GameBoardView gameBoardView;
    private GameMenuView gameMenuView;
    private GameBlurView gameBlurView;
    private GameOverView gameOverView;
    private GameInfoView gameInfoView;
    
    private EventCallbackView viewCallback = null;
    
    private ImagePattern[] themes = new ImagePattern[OthelloResources.TEXTURE.length];
    
	public OthelloGameView(Stage primaryStage, Othello othello, int boardSize){
		try {
			this.othello = othello;
			this.boardSize = boardSize;
			this.primaryStage = primaryStage;
			this.index = OthelloSettings.THEME;

			for(int i = 0; i<themes.length; i++){
				themes[i] =  new ImagePattern(OthelloResources.TEXTURE[i], 0, 0, menuWidth, menuWidth,false);
			}

			layout.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                xOffset = event.getSceneX();
	                yOffset = event.getSceneY();
	            }
	        });
			
			layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                primaryStage.setX(event.getScreenX() - xOffset);
	                primaryStage.setY(event.getScreenY() - yOffset);
	            }
	        });
			
			scene = new Scene(root, Color.TRANSPARENT);	
			scene.getStylesheets().add(getClass().getResource(OthelloSettings.STYLESHEET).toExternalForm());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initialize() {
		gameBlurView = new GameBlurView();
	    gameScoreView = new GameScoreView(this, 280, 180);
	    gameBoardView = new GameBoardView(this, layout, boardSize, boardSize);
	    gameMenuView = new GameMenuView(this,scene);    
	    gameInfoView = new GameInfoView(this,scene);
		gameAboutView = new GameAboutView();
	    gameOverView = new GameOverView(this);
	    
	    gameOverView.initialize(this, 500, 150);
	    gameAboutView.initialize(170, 120);
	    
	    gameOverView.setContinueAction(()->{
	    	gameBlurView.revertBlurAnimation(()->{
	    		resetBoard(100);
	    	});
	    });
	    
	    gameAboutView.setContinueAction(()->{
	    	gameBlurView.revertBlurAnimation(null);
	    });
	    
	    gameAboutView.setHideAction(()->{
	    	viewCallback.onGameResumed();
	    });
	      
	    gameScreen.getChildren().add(filler);
	    gameScreen.getChildren().add(layout);
	    
	    overScreen.setPickOnBounds(false);
	    overScreen.setId("overlay-screen-style");
	    overScreen.getChildren().add(gameOverView);
	    overScreen.getChildren().add(gameAboutView);

	    root.setMinSize(gameSize + menuWidth, gameSize);
		root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
		root.getChildren().add(gameScreen);
		root.getChildren().add(overScreen);	
		
		layout.setPadding(new Insets(20,20,20,20));
		layout.setLeft(gameMenuView);
		layout.setCenter(gameBoardView);
		layout.setRight(gameInfoView);
		
		layout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
		
		gameBlurView.setBlurContent(gameScreen);
		gameBlurView.setScreen(root);
		
		gameBlurView.setBlurEndAction(()->{
			overScreen.setMouseTransparent(false);
		});
		gameBlurView.setBlurRemoveAction(()->{
			overScreen.setMouseTransparent(true);
	
		});
	
		filler.widthProperty().bind(scene.widthProperty());
		filler.heightProperty().bind(scene.heightProperty());
		filler.setStrokeLineCap(StrokeLineCap.ROUND);
		filler.setStrokeType(StrokeType.INSIDE);
		filler.setStrokeWidth(12);
		filler.setStroke(Color.BLACK);
		filler.setArcWidth(45);
		filler.setArcHeight(45);
		filler.setFill(themes[index]);
      
		root.setId("game");
	
		
		primaryStage.setTitle(OthelloSettings.GAME_NAME);
		if(UserSettings.USE_ANIMATION){
			root.setOpacity(0);
			root.setScaleX(0);
			root.setScaleY(0);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
		}else{
			root.setOpacity(0);
			root.setScaleX(0);
			root.setScaleY(0);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
		}
		primaryStage.setScene(scene);
		primaryStage.show();

		createOverlay();

	}

	private void createOverlay(){
		if(gameScreen != null){
			WritableImage writableImage = new WritableImage((int) gameScreen.getWidth(), (int) gameScreen.getHeight());
			SnapshotParameters parameters = new SnapshotParameters();
			parameters.setFill(Color.TRANSPARENT);
			gameScreen.snapshot(parameters, writableImage);
			overlay.setImage(writableImage);
		}
	}
	
	private void showOverlay(){
		cullRegion(true);
	}
	
	private void hideOverlay(){
		cullRegion(false);
	}
	
	private void cullRegion(boolean state) {
		if(root!=null && gameScreen != null){
			if (state) {
				if(!root.getChildren().contains(overlay)){
					root.getChildren().remove(gameScreen);
					root.getChildren().add(0,overlay);
				}
			} else {
				if(!root.getChildren().contains(gameScreen)){
					root.getChildren().remove(overlay);
					root.getChildren().add(gameScreen);
				}
			}
		}
	}
	
	public void cycleThemes(){
		if(index < themes.length-1){
			index++;
		}else{
			index = 0;
		}
		
		filler.setFill(themes[index]);
		
	}


	public String getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(String playerOne) {
		this.playerOne = playerOne;
	}

	public String getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(String playerTwo) {
		this.playerTwo = playerTwo;
	}

	public double getMenuWidth() {
		return menuWidth;
	}

	public double getGameSize() {
		return gameSize;
	}

	public GameScoreView getGameScoreView(){
		return gameScoreView;
	}	
	
	public GameBoardView getGameBoardView(){
		return gameBoardView;
	}
	
	public GameMenuView getGameMenuView(){
		return gameMenuView;
	}
	
	public Scene getScene(){
		return scene;
	}
	
	public void openGame(Runnable script) {
		showOverlay();
		
		if (UserSettings.USE_ANIMATION) {
			ScaleTransition scale = new ScaleTransition(Duration.millis(700));
			scale.setNode(root);
			scale.setFromX(0.55);
			scale.setFromY(0.55);
			scale.setToX(OthelloSettings.SCENE_SCALE);
			scale.setToY(OthelloSettings.SCENE_SCALE);
			scale.setDelay(Duration.millis(50));
			scale.play();

			FadeTransition fade = new FadeTransition(Duration.millis(600));
			fade.setNode(root);
			fade.setFromValue(0);
			fade.setToValue(1);
			fade.setDelay(Duration.millis(50));
			fade.setOnFinished(e -> {
				if (script != null) {
					hideOverlay();
					gameMenuView.showLogos();
					script.run();
				}
			});
			fade.play();
		}else{
			root.setOpacity(1);
			root.setScaleX(OthelloSettings.SCENE_SCALE);
			root.setScaleY(OthelloSettings.SCENE_SCALE);
			if(script!=null){
				hideOverlay(); 
				gameMenuView.showLogos();
				script.run();
			}
		}

	}
	
	public void closeGame() {
		
		if (UserSettings.USE_ANIMATION) {
			ScaleTransition scale = new ScaleTransition(Duration.millis(300));
			scale.setNode(root);
			scale.setFromX(OthelloSettings.SCENE_SCALE);
			scale.setFromY(OthelloSettings.SCENE_SCALE);
			scale.setToX(0.5);
			scale.setToY(0.5);
			scale.play();

			FadeTransition fade = new FadeTransition(Duration.millis(300));
			fade.setNode(root);
			fade.setFromValue(1);
			fade.setToValue(0);
			fade.play();
			fade.setOnFinished(e -> {
				Thread thread = new Thread(() -> {
					try {
						Thread.sleep(200);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.exit(0);
				});

				thread.setDaemon(true);
				thread.start();
			});
		} else {
			System.exit(0);
		}
	}

	
	public boolean isResetting() {
		return resetting;
	}

	public void setResetting(boolean resetting) {
		this.resetting = resetting;
	}

	public StackPane getRoot() {
		return root;
	}

	public StackPane getGameScreen() {
		return gameScreen;
	}

	public StackPane getOverScreen() {
		return overScreen;
	}

	public void setViewCallback(EventCallbackView viewCallback) {
		this.viewCallback = viewCallback;
	}

	public EventCallbackView getViewCallback() {
		return viewCallback;
	}

	public void openSettings() {
		gameBoardView.toggleInfo();
	}

	public void openScoreBoard() {

	}
	
	public void showAboutView(){
		if(gameAboutView.isShowing()){
			return;
		}
		viewCallback.onGamePaused();
		gameAboutView.setShowing(true);
		if(UserSettings.USE_ANIMATION){
			gameBlurView.applyBlurAnimation();
		}else{
			gameBlurView.applyBlur();
		}
		overScreen.toFront();
		gameAboutView.showAboutView();
	}

	public void resetBoard(int delay) {
		if(!resetting){
			
			resetting = true;
			getGameBoardView().resetBoard(delay);
		}
	}
	
	public PlayerType getPlayerType(PieceType pieceType){
		if(pieceType == PieceType.WHITE){
			return PlayerType.PLAYER_ONE;
		}else{
			return PlayerType.PLAYER_TWO;
		}
	}
	
	public PieceType getPieceType(PlayerType playerType){
		if(playerType == PlayerType.PLAYER_ONE){
			return PieceType.WHITE;
		}else{
			return PieceType.BLACK;
		}
	}
		
	private PieceType getPieceTypeFromObjective(BoardCellState state){
		if(state == BoardCellState.WHITE_OBJECTIVE){
			return PieceType.WHITE;
		}else if(state == BoardCellState.BLACK_OBJECTIVE){
			return PieceType.BLACK;
		}
		
		return PieceType.NONE;
	}
	
	public EventCallbackController getControllerCallback() {
		return callbackController;
	}
	
	private EventCallbackController callbackController = new EventCallbackController(){

		@Override
		public void resetCells() {
			gameBoardView.resetCells();
			gameInfoView.resetConsole();
		}

		@Override
		public void updateBoardScore(int playerOne, int playerTwo) {
			gameScoreView.updateBoardScore(playerOne, playerTwo);
			gameOverView.setPlayerScore(playerOne, playerTwo);
		}

		@Override
		public void addGamePiece(PlayerType playerType, int row, int col, boolean initialStateMove) {
			gameBoardView.setPiece(getPieceType(playerType), row, col, 0, initialStateMove);
		}

		@Override
		public void endGame(GameEndState endState) {
			if(gameOverView.isShowing()){
				return;
			}
			
			gameOverView.setShowing(true);
			overScreen.toFront();
			switch(endState){
			case WHITE_WINS:			
				gameOverView.showEndGameView(PieceType.WHITE);
				break;
			case BLACK_WINS:
				gameOverView.showEndGameView(PieceType.BLACK);
				break;
			case DRAW:
				gameOverView.showEndGameView(PieceType.NONE);
				break;
			default:
				break;
			}
			gameBlurView.applyBlurAnimation();
		}

		@Override
		public void showPossibleCell(PlayerType player, Index index) {
			gameBoardView.applyTraversableEffect(getPieceType(player), index.getRow(), index.getCol());
		}

		@Override
		public void hidePossibleCell(PlayerType player, Index index) {
			gameBoardView.removeTraversableEffect(getPieceType(player), index.getRow(), index.getCol());
		}

		@Override
		public void hidePossibleCell(PlayerType player, Index index, boolean state) {
			gameBoardView.removeTraversableEffect(getPieceType(player), index.getRow(), index.getCol(), state);
		}
		
		@Override
		public synchronized void convertEnclosedCells(GameBoardCell gameBoardCell) {
			
			TrailWrapper trailWrapper = gameBoardCell.getTrailWrapper();
			
			PieceType type = getPieceTypeFromObjective(trailWrapper.getObjectiveState());
			
			for(Index index: trailWrapper.getTrail()){
				othello.log("Swapping color of: " + index + " to Color: " + type);
				gameBoardView.swapPiece(type, index.getRow(), index.getCol());
			}
		}

		@Override
		public void setPossibleCells(PlayerType player, List<Index> indexes) {
			gameBoardView.setPossibleCells(getPieceType(player), indexes);
		}

		@Override
		public void showPossibleMove(PlayerType player, Index index) {
			gameBoardView.applyPossibleMoveEffect(getPieceType(player), index.getRow(), index.getCol());
		}

		@Override
		public void hidePossibleMove() {
			gameBoardView.removePossibleMoveEffect();
		}

		@Override
		public void displayInformation(int depthCounter, int leafCounter, int pruneCounter, int nodesExamined) {
			gameInfoView.updateInfo(1, String.valueOf(depthCounter));
			gameInfoView.updateInfo(2, String.valueOf(pruneCounter));
			gameInfoView.updateInfo(3, String.valueOf(leafCounter));
			gameInfoView.updateInfo(4, String.valueOf(nodesExamined));
		}

		@Override
		public void showOnConsole(Object obj) {
			gameInfoView.printToConsole(obj);
		}
	};
}
