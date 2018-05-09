package com.eudycontreras.othello.models;

import java.util.LinkedList;
import java.util.List;

import com.eudycontreras.othello.application.OthelloSettings;
import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.utilities.GameBoardUtility;
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
public class GameBoard {
	
	private static final int DEFAULT_BOARD_SIZE = OthelloSettings.DEFAULT_BOARD_GRID_SIZE;

	private GameBoardCell[][] cells = new GameBoardCell[DEFAULT_BOARD_SIZE][DEFAULT_BOARD_SIZE];
	
	private int boardSize = DEFAULT_BOARD_SIZE;
	
	public GameBoard(){}
	
	public GameBoard(int boardSize){
		this.boardSize = boardSize;
		this.cells = new GameBoardCell[boardSize][boardSize];
		this.initializeCells(cells);
	}
	
	public GameBoard(int boardSize, List<GameBoardCell> gameState){
		this.boardSize = boardSize;
		this.cells = new GameBoardCell[boardSize][boardSize];
		this.initializeCells(cells);
	}
	
	public GameBoard(GameBoardCell[][] cells){
		if(cells != null){
			this.boardSize = cells.length;
			this.cells = cells;
			this.computeCellNeighbors(this.cells);
		}
	}
	
	public void setGameBoardCells(GameBoardCell[][] cells){
		this.boardSize = cells.length;
		this.cells = cells;
		this.computeCellNeighbors(this.cells);
	}
	
	public void startGame(PlayerTurn turn, GameBoardState state){
		this.resetBoard();
	}
		
	private void initializeCells(GameBoardCell[][] cells){	
		this.initializeCells(cells,GameBoardUtility.createDefaultState(this, boardSize));
	}

	private void initializeCells(GameBoardCell[][] cells, List<GameBoardCell> state){		
		for(int row = 0; row < cells.length; row++){
			for(int col = 0; col < cells[row].length; col++){
				cells[row][col] = new GameBoardCell(this, row, col);	
			}
		}

		this.computeCellNeighbors(cells);
	}
	
	private void computeCellNeighbors(GameBoardCell[][] cells){
		for(int row = 0; row < cells.length; row++){
			for(int col = 0; col < cells[row].length; col++){
				cells[row][col].computeNeighbors();		
			}
		}
	}

	public void resetBoard(){
		
		for(int row = 0; row < cells.length; row++){
			for(int col = 0; col < cells[row].length; col++){
				cells[row][col].clearObjectiveTrail();
				cells[row][col].setCellState(BoardCellState.EMPTY);	
			}
		}
		this.computeCellNeighbors(cells);
	}
	
	public void resetBoardCells(){
		for(int row = 0; row < cells.length; row++){
			for(int col = 0; col < cells[row].length; col++){
				cells[row][col].setCellState(BoardCellState.EMPTY);				
			}
		}
		this.computeCellNeighbors(cells);
	}
	
	public void makeMove(MoveWrapper move){
		int startRow = move.getStartIndex().getRow();
		int startCol = move.getStartIndex().getCol();
		
		int endRow = move.getMoveIndex().getRow();
		int endCol = move.getMoveIndex().getCol();
		
		int rowOffset = (startRow < endRow) ? 1 : (startRow > endRow) ? -1 : 0;
		int colOffset = (startCol < endCol) ? 1 : (startCol > endCol) ? -1 : 0;
		
		int row = move.getStartIndex().getRow();
		int col = move.getStartIndex().getCol();
		
		for(int i = 0; i<move.getMoveReward(); i++){
			
			row+=rowOffset;
			col+=colOffset;
			
			cells[row][col].setCellState(move.getTargetState());
		}
	}
	
	public GameBoardCell getGameBoardCell(Index index){
		return cells[index.getRow()][index.getCol()];
	}
	
	public GameBoardCell getGameBoardCell(int row, int col){
		return cells[row][col];
	}

	public void setGameBoardCell(GameBoardCell cell, int row, int col){
		this.cells[row][col] = cell;
	}
	
	public GameBoardCell[][] getCells() {
		return cells;
	}
	
	public List<GameBoardCell> getGameBoardCells(PlayerTurn player){

		switch(player){
		case PLAYER_ONE:
			return getGameBoardCells(BoardCellState.WHITE);
		case PLAYER_TWO:
			return getGameBoardCells(BoardCellState.BLACK);
		default:
			return getGameBoardCells(BoardCellState.ANY);
		}
	}

	public List<GameBoardCell> getGameBoardCells(BoardCellState state){

		List<GameBoardCell> gameCells = new LinkedList<>();
		
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if(cells[row][col].getCellState() == BoardCellState.ANY){
					if(cells[row][col].getCellState() == BoardCellState.WHITE || cells[row][col].getCellState() == BoardCellState.BLACK){
						gameCells.add(cells[row][col]);
					}
				}else{
					if(cells[row][col].getCellState() == state){
						gameCells.add(cells[row][col]);
					}
				}
			}
		}
		
		return gameCells;
	}

	public List<Index> getGameBoardObjectiveIndexes(BoardCellState state){

		List<Index> gameCells = new LinkedList<>();
		
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if(cells[row][col].getObjectiveState() == state){
					gameCells.add(cells[row][col].getIndex());
				}
			}
		}
		
		return gameCells;
	} 
	
	public void resetBuildTrails(BoardCellState state) {
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if(state == BoardCellState.ANY){
					cells[row][col].clearObjectiveTrail();
				}else{
					if(cells[row][col].getObjectiveState() == state){
						cells[row][col].clearObjectiveTrail();
					}
				}
			}
		}
	}

	public long getCount(BoardCellState state) {
		long counter = 0;
		
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if(state == BoardCellState.ANY){
					if(cells[row][col].getCellState() == BoardCellState.WHITE || cells[row][col].getCellState() == BoardCellState.BLACK){
						counter++;
					}
				}else{
					if(cells[row][col].getCellState() == state){
						counter++;
					}
				}
			}
		}
		
		return counter;
	}

	public void swappColors(List<Index> trail, BoardCellState objectiveState) {
		for (Index index: trail) {
			switch(objectiveState){
			case BLACK_OBJECTIVE:
				getGameBoardCell(index).swappColor(BoardCellState.BLACK);
				break;
			case WHITE_OBJECTIVE:
				getGameBoardCell(index).swappColor(BoardCellState.WHITE);
				break;
			default:
				break;
			
			}
		}
	}
	
	public GameBoardState getGameState() {
		return GameBoardUtility.getCurrentGameState(this);
	}

	public int getBoardSize() {
		return boardSize;
	}
}
