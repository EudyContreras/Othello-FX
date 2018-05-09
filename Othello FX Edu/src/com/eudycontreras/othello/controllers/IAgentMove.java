package com.eudycontreras.othello.controllers;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.models.GameBoardState;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * Class which represents an AI agent which will produce a move
 * Feel free to take a good look at the agent controller for additional
 * helper methods, examples and methods for creating more complex AI agents.
 * 
 * @author Eudy Contreras
 */
public interface IAgentMove {
	
	public AgentMove getMove(GameBoardState gameState);
	
}
