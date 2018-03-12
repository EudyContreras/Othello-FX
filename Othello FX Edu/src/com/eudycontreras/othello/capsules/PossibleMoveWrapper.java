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
	
	private List<IndexWrapper> path = new ArrayList<>(64);
	
	private final IndexWrapper startingPoint;
	private final IndexWrapper endingPoint;
	
	public PossibleMoveWrapper(int startRow, int startCol, int endRow, int endCol, int pathLength){
		this(new IndexWrapper(startRow,startCol),new IndexWrapper(endRow,endCol),pathLength);
	}
	
	public PossibleMoveWrapper(IndexWrapper startingPoint, IndexWrapper endingPoint, int pathLength){
		this.startingPoint = startingPoint;
		this.endingPoint = endingPoint;
		this.pathLenght = pathLength;
	}

	public int getPathLenght() {
		return pathLenght;
	}

	public IndexWrapper getStartingPoint() {
		return startingPoint;
	}

	public IndexWrapper getEndingPoint() {
		return endingPoint;
	}
	

	public void setPath(List<IndexWrapper> path) {
		this.path.addAll(path);
	}

	public List<IndexWrapper> getPath() {
		return path;
	}
}