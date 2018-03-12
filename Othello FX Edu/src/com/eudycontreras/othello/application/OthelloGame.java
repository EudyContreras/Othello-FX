package com.eudycontreras.othello.application;

import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoard;

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
public class OthelloGame {

	private GameBoard gameBoard;

	public OthelloGame(int boardSize) {
		gameBoard = new GameBoard(boardSize);
		gameBoard.startGame(PlayerTurn.PLAYER_ONE, null);
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

}
