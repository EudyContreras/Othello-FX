package com.eudycontreras.othello.capsules;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * The move master class which contains necessary information for a move
 * Move classes must extend from this base class
 * 
 * @author Eudy Contreras
 *
 */
public abstract class AgentMove implements Comparable<AgentMove>{

	
	protected Index moveIndex;

	public abstract boolean isValid();
	

	/**
	 * The index which this move will occupy
	 * @return the move index
	 */
	public Index getMoveIndex() {
		return moveIndex;
	}

	/**
	 * Sets the destination index of the move.
	 * @param startIndex: The index which this move will occupy
	 */
	public void setMoveIndex(Index moveIndex) {
		this.moveIndex = moveIndex;
	}
	
	
}
