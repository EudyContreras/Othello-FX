package com.eudycontreras.othello.capsules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.eudycontreras.othello.models.GameBoardCell;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * Class which encapsulates information about
 * an objective cell.
 * 
 * @author Eudy Contreras
 *
 */
public class ObjectiveWrapper {

	public static final ObjectiveWrapper NONE = getDefault();

	public static final Comparator<ObjectiveWrapper> COMPARATOR = ObjectiveWrapper.getComparator();

	private GameBoardCell objectiveCell;
	private GameBoardCell currentCell;	
	
	private List<TraversalWrapper> path = new LinkedList<>();
	
	public ObjectiveWrapper(List<TraversalWrapper> path) {
		this.path = path;
		if(path == null){
			path = new LinkedList<>();
		}
	}

	public ObjectiveWrapper(GameBoardCell cell, GameBoardCell currentCell) {
		this.objectiveCell = cell;
		this.currentCell = currentCell;
	}
	
	private static ObjectiveWrapper getDefault(){
		return new ObjectiveWrapper(new LinkedList<TraversalWrapper>());
	}
	
	/**
	 * Gets destination game board cell represented by this objective wrapper
	 * @return : the board cell
	 */
	public GameBoardCell getObjectiveCell() {
		return objectiveCell;
	}
	
	/**
	 * Sets destination game board cell represented by this objective wrapper
	 * @param cell : The board cell
	 */
	public void setObjectiveCell(GameBoardCell cell) {
		this.objectiveCell = cell;
	}
	
	/**
	 * Gets the current game board cell represented by this objective wrapper
	 * @return : the board cell
	 */
	public GameBoardCell getCurrentCell() {
		return currentCell;
	}
	
	/**
	 * Sets the current game board cell represented by this objective wrapper
	 * @param currentCell : the board cell
	 */
	public void setCurrentCell(GameBoardCell currentCell) {
		this.currentCell = currentCell;
	}

	/**
	 * Returns the traversal path used to reach this
	 * objective.
	 * @return
	 */
	public List<TraversalWrapper> getPath() {
		return path;
	}

	/**
	 * Sets the traversal path used to reach this 
	 * objective
	 * @param path
	 */
	public void setPath(List<TraversalWrapper> path) {
		this.path = path;
		if(path == null){
			path = new LinkedList<>();
		}
	}
	
	private static Comparator<ObjectiveWrapper> getComparator() {
		return new Comparator<ObjectiveWrapper>(){

			@Override
			public int compare(ObjectiveWrapper arg0, ObjectiveWrapper arg1) {
				return Integer.compare(arg0.getPath().size(), arg1.getPath().size());
			}
			
		};
	}
}
