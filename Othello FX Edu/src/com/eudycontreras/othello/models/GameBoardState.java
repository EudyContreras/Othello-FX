package com.eudycontreras.othello.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.utilities.GameBoardUtility;
import com.eudycontreras.othello.utilities.GameTreeUtility;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 *
 * Class which encapsulates the state of a game board
 * @author Eudy Contreras
 *
 */
public class GameBoardState {
	
	private int whiteCount = 0;
	private int blackCount = 0;
	
	private boolean visited = false;
	
	private boolean isRoot = false; 

	private BoardCellState playerTurn;
	
	private GameBoard gameBoard;
	
	private ObjectiveWrapper leadingMove;

	private GameBoardState parentState;
	
	private List<GameBoardState> childStates;
	
	public GameBoardState(GameBoard gameBoard){
		this(gameBoard, false);
	}
	
	public GameBoardState(GameBoard gameBoard, boolean isRoot){
		this.gameBoard = gameBoard;
		this.isRoot = isRoot;
		this.childStates = new ArrayList<>();
	}
	
	public static GameBoardState createBoardState(GameBoardCell[][] cells){
		return createBoardState(cells,false);
	}
	
	public static GameBoardState createBoardState(GameBoardCell[][] cells, boolean root){
		return createBoardState(cells,root,false);
	}
	
	public static GameBoardState createBoardState(GameBoardCell[][] cells, boolean root, boolean leaf){
		
		GameBoardState state = GameBoardUtility.getCurrentGameState(new GameBoard(cells), root);
		
		state.setRoot(root);

		return state;
	}
	
	public void setWhiteCount(int whiteCounter) {
		this.whiteCount = whiteCounter;
	}

	public void setBlackCount(int blackCounter) {
		this.blackCount = blackCounter;
	}

	public int getWhiteCount() {
		return whiteCount;
	}

	public int getBlackCount() {
		return blackCount;
	}
	
	public int getTotalCount() {
		return blackCount + whiteCount;
	}

	/**
	 * Gets the move which created this state
	 * @return
	 */
	public ObjectiveWrapper getLeadingMove(){
		return leadingMove;
	}
	
	/**
	 * Sets the move which created this state.
	 * @param leadingMove
	 */
	public void setLeadingMove(ObjectiveWrapper leadingMove){
		this.leadingMove = leadingMove;
	}

	/**
	 * Checks if this state is terminal
	 * @return
	 */
	public boolean isTerminal(){
		return gameBoard.getCount(BoardCellState.ANY) == (gameBoard.getBoardSize() * gameBoard.getBoardSize());		
	}
	
	/**
	 * Gets the parent of this state
	 * @return
	 */
	public GameBoardState getParentState() {
		return parentState;
	}

	/**
	 * Sets the parent of this state
	 * @param parentState
	 */
	public void setParentState(GameBoardState parentState) {
		this.parentState = parentState;
	}

	/**
	 * Gets a list containing the child states of this
	 * particular state
	 * @return
	 */
	public List<GameBoardState> getChildStates() {
		return childStates;
	}
	
	public List<GameBoardState> getChildStates(BoardCellState state){
		//return childStates.stream().filter(s-> s.getPlayerTurn() == state).collect(Collectors.toList());
		return childStates;
	}
	
	/**
	 * Adds a child state to this state
	 * @param childState
	 */
	public void addChildState(GameBoardState childState) {
		this.childStates.add(childState);
	}	
	
	/**
	 * Adds a list of child states to this state
	 * @param childStates
	 */
	public void addChildStates(List<GameBoardState> childStates) {
		this.childStates.addAll(childStates);
	}
	
	/**
	 * Adds an array of child states to this state
	 * @param childStates
	 */
	public void addChildStates(GameBoardState... childStates) {
		for(int i = 0; i<childStates.length; i++){
			this.childStates.add(childStates[i]);
		}
	}

	/**
	 * Calculates the total reward of this state
	 * given the specified move.
	 * @param move
	 * @return
	 */
	public int getTotalReward(MoveWrapper move) {
		return move.getMoveReward();
	}
	
	/**
	 * Returns the static score of this state.
	 * @param state
	 * @return
	 */
	public long getStaticScore(BoardCellState state){
		return (gameBoard.getCount(state) - gameBoard.getCount(GameTreeUtility.getCounterState(state)));
	}

	/**
	 * Returns the board size 
	 * @return
	 */
	public int getBoardSize() {
		return gameBoard.getBoardSize();
	}

	/**
	 * Returns the game board which produced this state.
	 * @return
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}
	
	/**
	 * Returns the cells of the game board which produced
	 * this state
	 * @return
	 */
	public GameBoardCell[][] getCells(){
		return gameBoard.getCells();
	}
	
	/**
	 * Returns the specified cell of the game board which produced
	 * this state
	 * @return
	 */
	public GameBoardCell getCell(int row, int col){
		return gameBoard.getGameBoardCell(row, col);
	}
	
	/**
	 * Returns the turn of the player 
	 * @return
	 */
	public BoardCellState getPlayerTurn() {
		return playerTurn;
	}

	/**
	 * Sets the turn of the player
	 * @param playerTurn
	 */
	public void setPlayerTurn(BoardCellState playerTurn) {
		this.playerTurn = playerTurn;
	}
	
	/**
	 * Determines if this state has been visited
	 * @return
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * Sets the visited state of this board state
	 * @param visited
	 */
	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	/**
	 * Checks if this state is the root state
	 * @return
	 */
	public boolean isRoot() {
		return isRoot;
	}

	/**
	 * Sets the root state of this child state.
	 * @param isRoot
	 */
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	/**
	 * Sets the game board cells to the parent game board
	 * of this state
	 * @param cells
	 */
	public void setGameBoardCells(GameBoardCell[][] cells) {
		gameBoard.setGameBoardCells(cells);
	}
	
	@Override
	public String toString() {
		return "Game board State: Player Turn: " + playerTurn + " WHITE : " + gameBoard.getCount(BoardCellState.WHITE) + " BLACK : " + gameBoard.getCount(BoardCellState.BLACK) 
		+ " Static Score White: " + getStaticScore(BoardCellState.WHITE) + " Static Score Black: " + getStaticScore(BoardCellState.BLACK) ;
	}

	public static Comparator<GameBoardState> moveLengthComparable() {
		return new Comparator<GameBoardState>(){

			@Override
			public int compare(GameBoardState arg0, GameBoardState arg1) {
				return Integer.compare(arg1.getLeadingMove().getPath().size(), arg0.getLeadingMove().getPath().size());
			}	
		};
	}

	public static Comparator<GameBoardState> countComparable(BoardCellState state) {
		return new Comparator<GameBoardState>(){

			@Override
			public int compare(GameBoardState arg0, GameBoardState arg1) {
				return Long.compare(arg1.getStaticScore(state), arg0.getStaticScore(state));
			}	
		};
	}
}
