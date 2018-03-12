package com.eudycontreras.othello.capsules;

import com.eudycontreras.othello.enumerations.TraversalCardinal;
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
public class DirectionWrapper {
	
	private GameBoardCell cell;
	private GameBoardCell source;
	private TraversalCardinal relation;
	
	public DirectionWrapper(GameBoardCell cell, GameBoardCell source, TraversalCardinal relation) {
		this.cell = cell;
		this.relation = relation;
		this.source = source;
	}	

	public GameBoardCell getSource() {
		return source;
	}

	public void setSource(GameBoardCell source) {
		this.source = source;
	}

	public GameBoardCell getCell() {
		return cell;
	}

	public void setCell(GameBoardCell cell) {
		this.cell = cell;
	}

	public TraversalCardinal getRelation() {
		return relation;
	}

	public void setRelation(TraversalCardinal relation) {
		this.relation = relation;
	}
	
	
}
