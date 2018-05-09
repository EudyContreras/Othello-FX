package com.eudycontreras.othello.callbacks;

import java.util.List;

import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.enumerations.GameEndState;
import com.eudycontreras.othello.enumerations.PlayerType;
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
public interface EventCallbackController {

	void resetCells();
	
	void addGamePiece(PlayerType player, int row, int col, boolean initialStateMove);

	void updateBoardScore(int playerOnePieceCount, int playerTwoPieceCount);

	void endGame(GameEndState endState);

	void showPossibleMove(PlayerType player, Index index);
	
	void hidePossibleMove();
	
	void showPossibleCell(PlayerType player, Index index);

	void hidePossibleCell(PlayerType playerOne, Index index);
	
	void hidePossibleCell(PlayerType playerOne, Index index, boolean state);

	void convertEnclosedCells(GameBoardCell gameBoardCell);

	void setPossibleCells(PlayerType playerOne, List<Index> gameBoardObjectiveIndexes);

	void displayInformation(int depthCounter, int leafCounter, int pruneCounter, int nodesExamined);

	void showOnConsole(Object obj);


}
