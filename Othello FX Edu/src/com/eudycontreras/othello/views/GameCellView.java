package com.eudycontreras.othello.views;

import com.eudycontreras.othello.enumerations.BoardCellType;

import javafx.scene.layout.StackPane;

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
public class GameCellView extends StackPane{
 
	private BoardCellType cellType = BoardCellType.EVEN;
	
	private GamePieceView gamePiece;

	public BoardCellType getCellType() {
		return cellType;
	}

	public void setCellType(BoardCellType cellType) {
		this.cellType = cellType;
	}

	public void addPiece(GamePieceView piece) {
		this.gamePiece = piece;
		getChildren().clear();
		getChildren().add(piece);
		
	}

	public GamePieceView getGamePiece() {
		return gamePiece;
	}
	
	
	
}