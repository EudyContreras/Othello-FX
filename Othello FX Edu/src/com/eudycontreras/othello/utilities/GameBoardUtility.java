package com.eudycontreras.othello.utilities;

import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.models.GameBoard;
import com.eudycontreras.othello.models.GameBoardCell;
import com.eudycontreras.othello.models.GameBoardState;

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
public class GameBoardUtility {

	public static List<GameBoardCell> createDefaultState(GameBoard gameBoard){
		return createDefaultState(gameBoard, gameBoard.getBoardSize());
	}
	
	public static List<GameBoardCell> createDefaultState(GameBoard gameBoard, int boardSize){
		
		List<GameBoardCell> cells = new ArrayList<>();
		
		int pieceCount = 4; 
		
		int offsetRow = -1;
		int offsetCol = -1;
		
		for(int i = 0; i<pieceCount; i++){
			
			if(i > 0){
				if(offsetCol == 0){
					offsetRow--;
				}
				if(offsetRow == 0){
					offsetCol++;
				}
				if(offsetCol == -1){
					offsetRow++;
				}
			}

			int row = (boardSize/2) +offsetRow;
			int col = (boardSize/2) +offsetCol;
			
			GameBoardCell cell = null;
			
			if(i%2 == 0){
				cell = new GameBoardCell(gameBoard, row, col,BoardCellState.WHITE);
			}else{
				cell = new GameBoardCell(gameBoard, row, col,BoardCellState.BLACK);
			}
			
			cells.add(cell);	
		}
		
		return cells;
		
	}
	
	public static GameBoardState getCurrentGameState(GameBoard gameBoard) {
		return getCurrentGameState(gameBoard,false);
	}
	
	public static GameBoardState getCurrentGameState(GameBoard gameBoard, boolean root) {
		
		GameBoardState gameState = new GameBoardState(gameBoard);
		
		gameState.setRoot(true);

		return gameState;
	}	
}
