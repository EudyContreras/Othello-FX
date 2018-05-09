package com.eudycontreras.othello.capsules;

import java.util.List;

import com.eudycontreras.othello.enumerations.BoardCellState;

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
public class TrailWrapper {

	private BoardCellState objectiveState;
	
	private List<Index> trail;
	
	public TrailWrapper(BoardCellState objectiveState, List<Index> trail) {
		this.objectiveState = objectiveState;
		this.trail = trail;
	}

	public BoardCellState getObjectiveState() {
		return objectiveState;
	}

	public void setObjectiveState(BoardCellState objectiveState) {
		this.objectiveState = objectiveState;
	}

	public List<Index> getTrail() {
		return trail;
	}

	public void setTrail(List<Index> trail) {
		this.trail = trail;
	}
}
