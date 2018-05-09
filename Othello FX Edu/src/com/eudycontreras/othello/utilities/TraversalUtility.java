package com.eudycontreras.othello.utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.eudycontreras.othello.capsules.DirectionWrapper;
import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.capsules.TraversalWrapper;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.TraversalCardinal;
import com.eudycontreras.othello.models.GameBoard;
import com.eudycontreras.othello.models.GameBoardCell;


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
public class TraversalUtility {

	public static final TraversalCardinal[] CARDINALS = { 
			TraversalCardinal.NORTH_WEST, 
			TraversalCardinal.NORTH,
			TraversalCardinal.NORTH_EAST, 
			TraversalCardinal.EAST, 
			TraversalCardinal.SOUTH_EAST, 
			TraversalCardinal.SOUTH,
			TraversalCardinal.SOUTH_WEST, 
			TraversalCardinal.WEST, };

	public static final Index[] OFFSETS = { 
			new Index(-1,-1), // NORTH_WEST
			new Index( 0,-1), // NORTH
			new Index( 1,-1), // NORTH_EAST
			new Index( 1, 0), // EAST
			new Index( 1, 1), // SOUTH_EAST
			new Index( 0, 1), // SOUTH
			new Index(-1, 1), // SOUTH_WEST
			new Index(-1, 0), // WEST
	};

	public static final HashMap<Index, TraversalCardinal> INDEX_CONVERSION_MAP;

	static {
		INDEX_CONVERSION_MAP = new HashMap<Index, TraversalCardinal>();

		INDEX_CONVERSION_MAP.put(OFFSETS[0], CARDINALS[0]);
		INDEX_CONVERSION_MAP.put(OFFSETS[1], CARDINALS[1]);
		INDEX_CONVERSION_MAP.put(OFFSETS[2], CARDINALS[2]);
		INDEX_CONVERSION_MAP.put(OFFSETS[3], CARDINALS[3]);
		INDEX_CONVERSION_MAP.put(OFFSETS[4], CARDINALS[4]);
		INDEX_CONVERSION_MAP.put(OFFSETS[5], CARDINALS[5]);
		INDEX_CONVERSION_MAP.put(OFFSETS[6], CARDINALS[6]);
		INDEX_CONVERSION_MAP.put(OFFSETS[7], CARDINALS[7]);
	}

	public static final HashMap<TraversalCardinal, Index> CARDINAL_CONVERSION_MAP;

	static {
		CARDINAL_CONVERSION_MAP = new HashMap<TraversalCardinal, Index>();

		CARDINAL_CONVERSION_MAP.put(CARDINALS[0], OFFSETS[0]);
		CARDINAL_CONVERSION_MAP.put(CARDINALS[1], OFFSETS[1]);
		CARDINAL_CONVERSION_MAP.put(CARDINALS[2], OFFSETS[2]);
		CARDINAL_CONVERSION_MAP.put(CARDINALS[3], OFFSETS[3]);
		CARDINAL_CONVERSION_MAP.put(CARDINALS[4], OFFSETS[4]);
		CARDINAL_CONVERSION_MAP.put(CARDINALS[5], OFFSETS[5]);
		CARDINAL_CONVERSION_MAP.put(CARDINALS[6], OFFSETS[6]);
		CARDINAL_CONVERSION_MAP.put(CARDINALS[7], OFFSETS[7]);
	}

	private static boolean colIndexOutOfBounds(int minCol, int maxCol, int col) {

		return col > maxCol ? true : col < minCol ? true : false;
	}

	private static boolean rowIndexOutOfBounds(int minRow, int maxRow, int row) {

		return row > maxRow ? true : row < minRow ? true : false;
	}

	public static DirectionWrapper getNeighbor(GameBoardCell cell, Index offset) {

		int rowOffset = offset.getRow();
		int colOffset = offset.getCol();

		int row = cell.getRow() + rowOffset;
		int col = cell.getCol() + colOffset;

		int minRow = 0;
		int minCol = 0;

		int rowCount = cell.getGameBoard().getCells().length - 1;
		int colCount = cell.getGameBoard().getCells()[0].length - 1;

		if (!rowIndexOutOfBounds(minRow, rowCount, row) && !colIndexOutOfBounds(minCol, colCount, col)) {

			TraversalCardinal relation = TraversalUtility.INDEX_CONVERSION_MAP.get(offset);

			return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, relation);
		}

		return null;
	}

	public static DirectionWrapper getNeighbor(GameBoardCell cell, TraversalCardinal neighbor) {

		int row = 0;
		int col = 0;

		int minRow = 0;
		int minCol = 0;

		int rowCount = cell.getGameBoard().getCells().length - 1;
		int colCount = cell.getGameBoard().getCells()[0].length - 1;

		switch (neighbor) {
		case NORTH_WEST:
			row = cell.getRow() - 1;
			col = cell.getCol() - 1;

			if (!rowIndexOutOfBounds(minRow, rowCount, row) && !colIndexOutOfBounds(minCol, colCount, col)) {
				return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, TraversalCardinal.NORTH_WEST);
			} else {
				return null;
			}
		case NORTH:
			row = cell.getRow();
			col = cell.getCol() - 1;

			if (!colIndexOutOfBounds(minCol, colCount, col)) {
				return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, TraversalCardinal.NORTH);
			} else {
				return null;
			}
		case NORTH_EAST:
			row = cell.getRow() + 1;
			col = cell.getCol() - 1;

			if (!rowIndexOutOfBounds(minRow, rowCount, row) && !colIndexOutOfBounds(minCol, colCount, col)) {
				return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, TraversalCardinal.NORTH_EAST);
			} else {
				return null;
			}
		case EAST:
			row = cell.getRow() + 1;
			col = cell.getCol();

			if (!rowIndexOutOfBounds(minRow, rowCount, row)) {
				return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, TraversalCardinal.EAST);
			} else {
				return null;
			}
		case SOUTH_EAST:
			row = cell.getRow() + 1;
			col = cell.getCol() + 1;

			if (!rowIndexOutOfBounds(minRow, rowCount, row) && !colIndexOutOfBounds(minCol, colCount, col)) {
				return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, TraversalCardinal.SOUTH_EAST);
			} else {
				return null;
			}
		case SOUTH:
			row = cell.getRow();
			col = cell.getCol() + 1;

			if (!colIndexOutOfBounds(minCol, colCount, col)) {
				return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, TraversalCardinal.SOUTH);
			} else {
				return null;
			}
		case SOUTH_WEST:
			row = cell.getRow() - 1;
			col = cell.getCol() + 1;

			if (!rowIndexOutOfBounds(minRow, rowCount, row) && !colIndexOutOfBounds(minCol, colCount, col)) {
				return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, TraversalCardinal.SOUTH_WEST);
			} else {
				return null;
			}
		case WEST:
			row = cell.getRow() - 1;
			col = cell.getCol();

			if (!rowIndexOutOfBounds(minRow, rowCount, row)) {
				return new DirectionWrapper(cell.getGameBoard().getGameBoardCell(row, col), cell, TraversalCardinal.WEST);
			} else {
				return null;
			}
		default:
			break;
		}
		return null;
	}

	public static LinkedList<DirectionWrapper> getNeighborCells(GameBoardCell cell, BoardCellState state) {

		LinkedList<DirectionWrapper> neighbors = new LinkedList<>();

		DirectionWrapper wrapper = null;

		for (TraversalCardinal neighbor : TraversalUtility.CARDINALS) {
			wrapper = getNeighbor(cell, TraversalUtility.CARDINAL_CONVERSION_MAP.get(neighbor));
			if (wrapper != null && !wrapper.equals(cell) && wrapper.getCell().getCellState() == state) {
				neighbors.add(wrapper);
			}
		}

		return neighbors;
	}

	public static boolean isTraversable(GameBoardCell origin, GameBoardCell current) {
		if (origin.getCellState() == BoardCellState.BLACK) {
			if (current.getCellState() == BoardCellState.WHITE) {
				return true;
			}
		} else if (origin.getCellState() == BoardCellState.WHITE) {
			if (current.getCellState() == BoardCellState.BLACK) {
				return true;
			}
		}
		return false;
	}

	public static List<TraversalWrapper> getTraversalPath(GameBoardCell cell, int depth, TraversalCardinal neighbor) {

		LinkedList<TraversalWrapper> neighbors = new LinkedList<>();

		getTraversalPath(cell.getGameBoard(), cell, cell, depth, neighbors, neighbor);

		return neighbors;
	}

	private static void getTraversalPath(GameBoard grid, GameBoardCell origin, GameBoardCell cell, int depth, List<TraversalWrapper> neighbors, TraversalCardinal neighbor) {

		TraversalWrapper path = null;

		DirectionWrapper wrapper = getNeighbor(cell, neighbor);

		if (wrapper != null) {

			if (isTraversable(origin, wrapper.getCell())) {

				path = new TraversalWrapper(wrapper.getCell().getIndex(), cell.getIndex());

				neighbors.add(0, path);

				if (depth > 0) {

					getTraversalPath(grid, origin, wrapper.getCell(), depth - 1, neighbors, neighbor);
				}

			}
		}
	}

	public static List<ObjectiveWrapper> getAvailableCells(GameBoardCell cell, int depth) {

		List<ObjectiveWrapper> wrappers = new LinkedList<>();

		List<DirectionWrapper> neighbors =  getNeighborCells(cell, cell.getCounterCell(cell));

		for (DirectionWrapper neighbor : neighbors) {

			ObjectiveWrapper availableCell = getAvailableCells(cell, depth, neighbor.getRelation());

			if (availableCell != null) {

				wrappers.add(availableCell);
			}

		}
		return wrappers;
	}

	private static ObjectiveWrapper getAvailableCells(GameBoardCell source, int depth, TraversalCardinal relation) {

		List<TraversalWrapper> paths = getTraversalPath(source, depth, relation);

		if (!paths.isEmpty()) {

			Index index = paths.get(0).getIndex();

			GameBoardCell cell = source.getGameBoard().getGameBoardCell(index);

			DirectionWrapper wrapper = getNeighbor(cell, relation);

			if (wrapper != null) {

				if (wrapper.getCell().getCellState() == BoardCellState.EMPTY) {

					ObjectiveWrapper availableCell = new ObjectiveWrapper(wrapper.getCell(), source);

					availableCell.setPath(paths);

					return availableCell;
				}
			}
		}
		return null;
	}

	public static LinkedList<TraversalWrapper> getLongestList(List<LinkedList<TraversalWrapper>> paths) {

		LinkedList<TraversalWrapper> longest = paths.get(0);

		int maxSize = Integer.MIN_VALUE;

		for (int i = 0; i < paths.size(); i++) {

			if (paths.get(i).size() > maxSize && !paths.get(i).isEmpty()) {

				maxSize = paths.get(i).size();
				longest = paths.get(i);
			}
		}
		return longest;
	}
}
