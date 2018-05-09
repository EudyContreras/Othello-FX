package com.eudycontreras.othello.capsules;

import java.util.ArrayList;
import java.util.List;

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
public class PossibleMoveWrapper{

	private final int pathLenght;
	
	private List<Index> path = new ArrayList<>(64);
	
	private final Index startingPoint;
	private final Index endingPoint;
	
	public PossibleMoveWrapper(int startRow, int startCol, int endRow, int endCol, int pathLength){
		this(new Index(startRow,startCol),new Index(endRow,endCol),pathLength);
	}
	
	public PossibleMoveWrapper(Index startingPoint, Index endingPoint, int pathLength){
		this.startingPoint = startingPoint;
		this.endingPoint = endingPoint;
		this.pathLenght = pathLength;
	}

	public int getPathLenght() {
		return pathLenght;
	}

	public Index getStartingPoint() {
		return startingPoint;
	}

	public Index getEndingPoint() {
		return endingPoint;
	}
	

	public void setPath(List<Index> path) {
		this.path.addAll(path);
	}

	public List<Index> getPath() {
		return path;
	}
}