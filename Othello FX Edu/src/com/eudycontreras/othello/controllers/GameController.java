package com.eudycontreras.othello.controllers;

import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.othello.application.Othello;
import com.eudycontreras.othello.application.OthelloGame;
import com.eudycontreras.othello.application.OthelloSettings;
import com.eudycontreras.othello.callbacks.EventCallbackController;
import com.eudycontreras.othello.callbacks.EventCallbackView;
import com.eudycontreras.othello.capsules.AvailableWrapper;
import com.eudycontreras.othello.capsules.IndexWrapper;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.capsules.TraversalWrapper;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.GameEndState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.enumerations.PlayerType;
import com.eudycontreras.othello.models.GameBoardCell;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;
import com.eudycontreras.othello.utilities.GameBoardUtility;
import com.eudycontreras.othello.utilities.TraversalUtility;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 */
public class GameController {

	
	private boolean determiningWinner;
	
	private Othello othello;
	
	private OthelloGame othelloGame;
	 
    private AvailableWrapper whitePossibleCells;
    private AvailableWrapper blackPossibleCells;
    
	private EventCallbackController callbackController;
	
	private PlayerTurn currentTurn = PlayerTurn.PLAYER_ONE;
	
	public GameController(Othello othello){
		this.othello = othello;
	}

	public void initialize(){
		if(callbackController != null){
			this.callbackController.resetCells();
		}
		
		Othello.printBoard(othelloGame.getGameBoard().getCells());		
		
		computeAvailableMoves(PlayerType.PLAYER_ONE, BoardCellState.WHITE);
		computeAvailableMoves(PlayerType.PLAYER_TWO, BoardCellState.BLACK);
	
		storeAvailableCells(PlayerType.PLAYER_ONE, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_ONE))),false);
		storeAvailableCells(PlayerType.PLAYER_TWO, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_TWO))),false);
		
		updateGameScore();	
	}

	public void resetGame(){
		this.determiningWinner = false;
		this.othelloGame.getGameBoard().resetBoard();
	}
	
	public void makeMove(PlayerType player, int row, int col){
		makeMove(player, row, col,false);
	}
	
	public void makeMove(PlayerType player, int row, int col, boolean initialStateMove){
			
		if(callbackController != null){

			this.callbackController.resetCells();
			this.callbackController.addGamePiece(player, row, col, initialStateMove);
			this.callbackController.convertEnclosedCells(this.othelloGame.getGameBoard().getGameBoardCell(row,col));
		}
		
		switch(player){
		case PLAYER_ONE:
	
			this.othelloGame.getGameBoard().getGameBoardCell(row,col).setCellState(BoardCellState.WHITE);
			this.othelloGame.getGameBoard().getGameBoardCell(row,col).convertEnclosedCells();
			this.othelloGame.getGameBoard().resetBuildTrails(BoardCellState.ANY);
			
			computeAvailableMoves(PlayerType.PLAYER_TWO, BoardCellState.BLACK);

			storeAvailableCells(PlayerType.PLAYER_TWO, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_TWO))),false);
			
			callbackController.hidePossibleMove();
			
			for(IndexWrapper move: blackPossibleCells.getIndexes()){
				callbackController.showPossibleMove(PlayerType.PLAYER_TWO, move);	
			}
			
			currentTurn = PlayerTurn.PLAYER_TWO;
			break;
		case PLAYER_TWO:

			this.othelloGame.getGameBoard().getGameBoardCell(row,col).setCellState(BoardCellState.BLACK);
			this.othelloGame.getGameBoard().getGameBoardCell(row,col).convertEnclosedCells();
			this.othelloGame.getGameBoard().resetBuildTrails(BoardCellState.ANY);

			computeAvailableMoves(PlayerType.PLAYER_ONE, BoardCellState.WHITE);
			
			storeAvailableCells(PlayerType.PLAYER_ONE, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_ONE))),false);
				
			callbackController.hidePossibleMove();
			
			for(IndexWrapper move: whitePossibleCells.getIndexes()){
				callbackController.showPossibleMove(PlayerType.PLAYER_ONE, move);	
			}
			
			currentTurn = PlayerTurn.PLAYER_ONE;
			break;
		default:
			break;
		}
		
		updateGameScore();	
		
		ThreadManager.schedule(TimeSpan.millis(2000), ()->{
			evaluateGame(othelloGame.getGameBoard().getGameState());
		});
		
		
		Othello.printBoard(othelloGame.getGameBoard().getCells(), false);

	}
	

	public void computeAvailableMoves(PlayerType player, BoardCellState state){
		
		List<GameBoardCell> cells = othelloGame.getGameBoard().getGameBoardCells(state);

		int validMoves = 0;
		
		Othello.println("");

		for(GameBoardCell cell: cells){
			
			List<ObjectiveWrapper> availableCells = TraversalUtility.getAvailableCells(cell, 8);
			
			for(ObjectiveWrapper availableCell: availableCells){
				
				IndexWrapper index = availableCell.getObjectiveCell().getIndex();
				
				othelloGame.getGameBoard().getGameBoardCell(index).setObjective(getStateBaseObjective(state), buildTrail(availableCell));
	
				Othello.print("Index : " +availableCell.getObjectiveCell().getIndex() + " Path: ");
				
				for(TraversalWrapper path: availableCell.getPath()){
					Othello.print(path.getIndex()+ ", ");
				}
				
				Othello.print(" Path Color: " + othelloGame.getGameBoard().getGameBoardCell(availableCell.getPath().get(0).getIndex()));
				Othello.print(" Objective Color: " + cell);
				
				validMoves++;
				
				Othello.println("");
			}			
		}

		othello.log("Possible cells for: "+ state + " : " +validMoves);
		
		Othello.println("");
	}
	

	public boolean hasAvailableMoves(PlayerType player, BoardCellState state){
		
		List<GameBoardCell> cells = othelloGame.getGameBoard().getGameBoardCells(state);

		for(GameBoardCell cell: cells){
			
			List<ObjectiveWrapper> availableCells = TraversalUtility.getAvailableCells(cell, 8);
			
			if(!availableCells.isEmpty()){
				return true;
			}
		}

		return false;
	}
	
	public void storeAvailableCells(PlayerType player, List<IndexWrapper> indexes, boolean log) {
		switch(player){
		case PLAYER_ONE:
			whitePossibleCells = new AvailableWrapper(player, indexes);
			if(log){
				othello.log("All stored indexes for white: ");
				for(int i = 0; i < indexes.size(); i++){
					othello.log(indexes.get(i));
				}
			}
			break;
		case PLAYER_TWO:
			blackPossibleCells = new AvailableWrapper(player, indexes);
			if(log){
				othello.log("All stored indexes for black: ");
				for(int i = 0; i < indexes.size(); i++){
					othello.log(indexes.get(i));
				}
			}
			break;
		default:
			break;		
		}
	}
	
	private BoardCellState getStateBaseObjective(BoardCellState state){
		if(state == BoardCellState.WHITE){
			return BoardCellState.WHITE_OBJECTIVE;
		}
		else if(state == BoardCellState.BLACK){
			return BoardCellState.BLACK_OBJECTIVE;
		}
		
		return BoardCellState.EMPTY;
	}
	
	
	private BoardCellState getBoardCellState(PlayerType player){
		if(player == PlayerType.PLAYER_ONE){
			return BoardCellState.WHITE;
		}else if(player == PlayerType.PLAYER_TWO){
			return BoardCellState.BLACK;
		}
		
		return BoardCellState.NONE;
	}
	
	private List<IndexWrapper> buildTrail(ObjectiveWrapper cell){
		
		List<IndexWrapper> indexes = new ArrayList<>();
		List<TraversalWrapper> paths = cell.getPath();
		
		for(int i = 0; i < paths.size(); i++){
			
			indexes.add(paths.get(i).getIndex());
		}
		
		return indexes;
	}
	
	public void updateGameScore(){

		int playerOnePieceCount = othelloGame.getGameBoard().getGameBoardCells(BoardCellState.WHITE).size();
		int playerTwoPieceCount = othelloGame.getGameBoard().getGameBoardCells(BoardCellState.BLACK).size();
		
		if(callbackController != null)
		callbackController.updateBoardScore(playerOnePieceCount, playerTwoPieceCount);
	}
	
	private synchronized void evaluateGame(GameBoardState state){

		long whiteCount = state.getGameBoard().getCount(BoardCellState.WHITE);
		long blackCount = state.getGameBoard().getCount(BoardCellState.BLACK);
		
		int boardSize = othelloGame.getGameBoard().getBoardSize() * othelloGame.getGameBoard().getBoardSize();
		
		othello.log("Board cells left: " + (boardSize - (whiteCount + blackCount)), OthelloSettings.DEBUG_GAME);
		othello.log("Total cell piece count: " + (whiteCount + blackCount), OthelloSettings.DEBUG_GAME);
		othello.log("Total white piece count: " + whiteCount, OthelloSettings.DEBUG_GAME);
		othello.log("Total black piece count: " + blackCount, OthelloSettings.DEBUG_GAME);
		
		if (boardSize == (whiteCount + blackCount)){	
			determineWinner(whiteCount, blackCount);
		}else{
			switch(currentTurn){
				case PLAYER_ONE:
					if(!hasAvailableMoves(PlayerType.PLAYER_ONE, BoardCellState.WHITE)){
						if(hasAvailableMoves(PlayerType.PLAYER_TWO, BoardCellState.BLACK)){
							
							computeAvailableMoves(PlayerType.PLAYER_TWO, BoardCellState.BLACK);

							storeAvailableCells(PlayerType.PLAYER_TWO, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_TWO))),false);
							
							currentTurn = PlayerTurn.PLAYER_TWO;
							
							Platform.runLater(()->{
								for(IndexWrapper move: blackPossibleCells.getIndexes()){
									callbackController.showPossibleMove(PlayerType.PLAYER_TWO, move);	
								}
							});
						}else{
							System.out.println("NO MORE MOVES CAN BE MADE!");
							determineWinner(whiteCount, blackCount);
						}
					}
					break;
				case PLAYER_TWO:
					if(!hasAvailableMoves(PlayerType.PLAYER_TWO, BoardCellState.BLACK)){
						if(hasAvailableMoves(PlayerType.PLAYER_ONE, BoardCellState.WHITE)){
							
							computeAvailableMoves(PlayerType.PLAYER_ONE, BoardCellState.WHITE);

							storeAvailableCells(PlayerType.PLAYER_ONE, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_ONE))),false);
							
							currentTurn = PlayerTurn.PLAYER_ONE;
							
							Platform.runLater(()->{
								for(IndexWrapper move: whitePossibleCells.getIndexes()){
									callbackController.showPossibleMove(PlayerType.PLAYER_ONE, move);	
								}
								
							});
							
							othello.setAgentMove();
						}else{
							System.out.println("NO MORE MOVES CAN BE MADE!");
							determineWinner(whiteCount, blackCount);
						}
					}
					break;
			}
		}
		
	}

	private void determineWinner(long whiteCount, long blackCount) {
	
		if(determiningWinner){
			return;
		}
		System.out.println("DETERMINING WINNER");
		determiningWinner = true;
		if(whiteCount > blackCount){
			
			notifyEndState(GameEndState.WHITE_WINS);
			
		}else if(blackCount > whiteCount){
			
			notifyEndState(GameEndState.BLACK_WINS);
			
		}else{
			
			notifyEndState(GameEndState.DRAW);
		}
	}
	
	private PlayerType getPlayerType(PlayerTurn turn){
		if(turn == PlayerTurn.PLAYER_ONE){
			return PlayerType.PLAYER_ONE;
		}else{
			return PlayerType.PLAYER_TWO;
		}
	}
	
	private void switchTurn(){
//		if(currentTurn == PlayerTurn.PLAYER_ONE){
//			currentTurn = PlayerTurn.PLAYER_TWO;
//		}else{
//			currentTurn = PlayerTurn.PLAYER_ONE;
//		}
	}
	
	private void notifyEndState(GameEndState endState) {
		Platform.runLater(()->{
			if(callbackController != null){
				callbackController.endGame(endState);
			}
		});
	}

	public OthelloGame getOthelloGame() {
		return othelloGame;
	}

	public void setOthelloGame(OthelloGame othelloGame) {
		this.othelloGame = othelloGame;
	}

	public EventCallbackController getOthelloGameView() {
		return callbackController;
	}

	public void setControllerCallback(EventCallbackController controllerCallback) {
		this.callbackController = controllerCallback;
	}

	public EventCallbackView getViewCallback() {
		return callbackView;
	}
	
	private EventCallbackView callbackView = new EventCallbackView(){

		@SuppressWarnings("unused")
		@Override
		public void setMouseEnteredEvent(MouseEvent e, int row, int col) {
			switch(currentTurn){
			case PLAYER_ONE:
				if(!OthelloSettings.USE_AI_AGENT && whitePossibleCells != null){
					for(IndexWrapper index: whitePossibleCells.getIndexes()){
						if(row == index.getRow() && col == index.getCol()){
							callbackController.showPossibleCell(PlayerType.PLAYER_ONE, index);
						}
					}
				}
				break;
			case PLAYER_TWO:
				if(blackPossibleCells != null){
					for(IndexWrapper index: blackPossibleCells.getIndexes()){
						if(row == index.getRow() && col == index.getCol()){
							callbackController.showPossibleCell(PlayerType.PLAYER_TWO, index);
						}
					}
				}
				break;
			default:
				break;
			
			}
		}

		@SuppressWarnings("unused")
		@Override
		public void setMouseExitedEvent(MouseEvent e, int row, int col) {
			switch(currentTurn){
			case PLAYER_ONE:
				if(!OthelloSettings.USE_AI_AGENT && whitePossibleCells != null){
					for(IndexWrapper index: whitePossibleCells.getIndexes()){
						if(row == index.getRow() && col == index.getCol()){
							callbackController.hidePossibleCell(PlayerType.PLAYER_ONE, index);
						}
					}
				}
				break;
			case PLAYER_TWO:
				if(blackPossibleCells != null){
					for(IndexWrapper index: blackPossibleCells.getIndexes()){
						if(row == index.getRow() && col == index.getCol()){
							callbackController.hidePossibleCell(PlayerType.PLAYER_TWO, index);
						}
					}
				}
				break;
			default:
				break;
			
			}
		}

		@SuppressWarnings("unused")
		@Override
		public void setMousePressedEvent(MouseEvent e, int row, int col) {
			callbackController.hidePossibleCell(PlayerType.PLAYER_TWO, new IndexWrapper(row,col));
			
			switch(currentTurn){
			case PLAYER_ONE:
				if(!OthelloSettings.USE_AI_AGENT && whitePossibleCells != null){
					for(IndexWrapper index: whitePossibleCells.getIndexes()){
						if(row == index.getRow() && col == index.getCol()){
							makeMove(getPlayerType(currentTurn),row,col);
							
							switchTurn();
						}
					}
				}
			break;
			case PLAYER_TWO:
				if(blackPossibleCells != null){
					for(IndexWrapper index: blackPossibleCells.getIndexes()){
						if(row == index.getRow() && col == index.getCol()){
							makeMove(getPlayerType(currentTurn),row,col);
							
							if(OthelloSettings.USE_AI_AGENT){
								othello.setAgentMove();
							}else{
								switchTurn();							
							}
						}
					}
				}
				
				
				break;
			default:
				break;
			
			}
		}

		@Override
		public void resetBoard(int delay) {

			determiningWinner = false;
			othelloGame.getGameBoard().resetBoard();
			currentTurn = PlayerTurn.PLAYER_ONE;
			
			passInformation(0,0,0,0);
			
			computeAvailableMoves(PlayerType.PLAYER_ONE, BoardCellState.WHITE);
			computeAvailableMoves(PlayerType.PLAYER_TWO, BoardCellState.BLACK);
		
			storeAvailableCells(PlayerType.PLAYER_ONE, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_ONE))),false);
			storeAvailableCells(PlayerType.PLAYER_TWO, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_TWO))),false);
			
			updateGameScore();	
			
			setInitialState(delay);
			
			if(OthelloSettings.USE_AI_AGENT){
				othello.setAgentMove();
			}
		}	
	};

	public void setInitialState(int delay) {
		List<GameBoardCell> initialState = GameBoardUtility.createDefaultState(othelloGame.getGameBoard());

		for(GameBoardCell cell: initialState){
			if(cell.getCellState() == BoardCellState.WHITE){
				makeMove(PlayerType.PLAYER_ONE,cell.getRow(),cell.getCol(),true);
			}else if(cell.getCellState() == BoardCellState.BLACK){
				makeMove(PlayerType.PLAYER_TWO,cell.getRow(),cell.getCol(),true);
			}
		}
	}

	public void setAgentMove(MoveWrapper move) {
//		System.out.println(move);
		
		if(move.isValid()){
			makeMove(getPlayerType(PlayerTurn.PLAYER_ONE),move.getMoveIndex().getRow(),move.getMoveIndex().getCol());
		}else{
			this.othelloGame.getGameBoard().resetBuildTrails(BoardCellState.ANY);
			
			computeAvailableMoves(PlayerType.PLAYER_TWO, BoardCellState.BLACK);

			storeAvailableCells(PlayerType.PLAYER_TWO, othelloGame.getGameBoard().getGameBoardObjectiveIndexes(getStateBaseObjective(getBoardCellState(PlayerType.PLAYER_TWO))),false);
			
			updateGameScore();	
			
			currentTurn = PlayerTurn.PLAYER_TWO;
			
			ThreadManager.schedule(TimeSpan.millis(2000), ()->{
				evaluateGame(othelloGame.getGameBoard().getGameState());
			});

		}
		switchTurn();
	}

	public void passInformation(int depthCounter, int leafCounter, int pruneCounter, int nodesExamined) {
		callbackController.displayInformation(depthCounter, leafCounter, pruneCounter, nodesExamined);
	}
}
