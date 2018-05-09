package com.eudycontreras.othello.models;

import java.util.LinkedList;
import java.util.List;

import com.eudycontreras.othello.capsules.DirectionWrapper;
import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.capsules.TrailWrapper;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.utilities.TraversalUtility;
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
public class GameBoardCell {
	
	private final int row;
	private final int col;

	private GameBoard gameBoard;
	
	private final Index index;
	
	private List<Index> trail = new LinkedList<>();

	private List<DirectionWrapper> neighbors = new LinkedList<>();

	private BoardCellState objectiveState = BoardCellState.NONE;
	
	private BoardCellState cellState = BoardCellState.EMPTY;
	
	public GameBoardCell(GameBoard gameBoard, int row, int col) {
		this(gameBoard, row, col, BoardCellState.EMPTY);
	}

	public GameBoardCell(GameBoard gameBoard, int row, int col, BoardCellState cellState) {
		super();
		this.row = row;
		this.col = col;
		this.gameBoard = gameBoard;
		this.cellState = cellState;
		this.index = new Index(row, col);	
	}

	public void computeNeighbors() {
		this.neighbors = TraversalUtility.getNeighborCells(this, BoardCellState.ANY);	
	}

	public BoardCellState getCounterCell(GameBoardCell cell) {
		if (cell.getCellState() == BoardCellState.BLACK) {
			return BoardCellState.WHITE;
		} 
		else if (cell.getCellState() == BoardCellState.WHITE) {
			return BoardCellState.BLACK;
		}
		
		return BoardCellState.EMPTY;
	}
	
	public void setObjective(BoardCellState cellState, List<Index> trail) {
		this.objectiveState = cellState;
		this.trail.addAll(trail);
	}

	public BoardCellState getCellState() {
		return cellState;
	}
	
	public BoardCellState getObjectiveState() {
		return objectiveState;
	}

	public void swappColor(BoardCellState cellState) {
		this.cellState = cellState;
	}
	
	public void swappColor() {
		if (cellState == BoardCellState.WHITE) {
			setCellState(BoardCellState.BLACK);
		} else if (cellState == BoardCellState.BLACK) {
			setCellState(BoardCellState.WHITE);
		}
	}

	public void setCellState(BoardCellState cellState) {
		this.cellState = cellState;
	}
	
	public void convertEnclosedCells(){
		if(!trail.isEmpty()){
			gameBoard.swappColors(trail, objectiveState);
		}
	}

	public void clearObjectiveTrail(){
		this.objectiveState = BoardCellState.NONE;
		
		if(!trail.isEmpty()){
			this.trail.clear();
		}
	}
	
	public TrailWrapper getTrailWrapper(){
		return new TrailWrapper(objectiveState, trail);
	}
	
	public List<DirectionWrapper> getNeighbors() {
		return neighbors;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public Index getIndex(){
		return index;
	}
	
	public GameBoard getGameBoard() {
		return gameBoard;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GameBoardCell))
			return false;
		GameBoardCell other = (GameBoardCell) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getStateCode(cellState);
	}

	public String getStateCode(BoardCellState state) {
		String code = null;

		switch (state) {
		case ANY:
			return "!";
		case BLACK:
			return "B";
		case EMPTY:
			return "-";
		case WHITE:
			return "W";
		default:
			break;

		}

		return code;
	}
}
