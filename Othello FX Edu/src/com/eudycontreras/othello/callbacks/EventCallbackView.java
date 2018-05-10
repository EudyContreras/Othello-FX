package com.eudycontreras.othello.callbacks;

import javafx.scene.input.MouseEvent;

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
public interface EventCallbackView {

	void setMouseEnteredEvent(MouseEvent e, int row, int col);

	void setMouseExitedEvent(MouseEvent e, int row, int col);

	void setMousePressedEvent(MouseEvent e, int row, int col);

	void resetBoard(int delay);
	
	void resetBoard(int delay, Runnable endScript);
	
	void onGamePaused();
	
	void onGamePaused(Runnable onPaused);

	void onGameResumed();
	
	void onGameResumed(Runnable onResumed);
}
