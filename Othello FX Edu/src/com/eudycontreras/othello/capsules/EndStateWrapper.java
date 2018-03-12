package com.eudycontreras.othello.capsules;

import com.eudycontreras.othello.enumerations.GameEndState;

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
public class EndStateWrapper {

	private long whiteCount;
	private long blackCount;
	
	private boolean ended;
	
	private GameEndState endState;

	public EndStateWrapper(boolean ended, GameEndState endState, long white, long black) {
		super();
		this.whiteCount = white;
		this.blackCount = black;
		this.ended = ended;
		this.endState = endState;
	}

	public boolean isEnded() {
		return ended;
	}

	public GameEndState getEndState() {
		return endState;
	}

	public long getWhiteCount() {
		return whiteCount;
	}

	public long getBlackCount() {
		return blackCount;
	}
	
}
