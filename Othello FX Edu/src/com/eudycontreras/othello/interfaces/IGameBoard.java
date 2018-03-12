package com.eudycontreras.othello.interfaces;

import com.eudycontreras.othello.enumerations.PlayerType;
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
public interface IGameBoard {
	
	public void updateGameBoard(PlayerType player, int row, int col);
	
	public void resetGameBoard();
	
	public void setInitialState(GameBoardState state);
	
	public void updateGameBoard();
	
}
