package com.eudycontreras.othello.capsules;

import com.eudycontreras.othello.enumerations.TraversalCardinal;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * Class which encapsulates information about a path
 * start point, destination and direction.
 * 
 * @author Eudy Contreras
 */
public class TraversalWrapper {

	private Index index;
	private Index parentIndex;
	private TraversalCardinal direction;
	private PathFlag flag;

	public TraversalWrapper(Index index, Index parentIndex) {
		this(index, parentIndex, TraversalCardinal.NONE, PathFlag.UNVISITED);
	}

	public TraversalWrapper(Index index, Index parentIndex, TraversalCardinal direction) {
		this(index,  parentIndex, direction, PathFlag.UNVISITED);
	}

	public TraversalWrapper(Index index, Index parentIndex, TraversalCardinal direction, PathFlag flag) {
		super();
		this.index = index;
		this.parentIndex = parentIndex;
		this.direction = direction;
		this.flag = flag;
	}

	/**
	 * gets the index of the starting point
	 * @return: the parent index
	 */
	public Index getParentIndex() {
		return parentIndex;
	}

	/**
	 * sets the index of the starting point
	 * @param parentIndex : the starting point
	 */
	public void setParentIndex(Index parentIndex) {
		this.parentIndex = parentIndex;
	}

	/**
	 * Get the index of the destination
	 * @return : the destination index
	 */
	public Index getIndex() {
		return index;
	}

	/**
	 * Sets the index of the destination
	 * @param index : the destination index
	 */
	public void setIndex(Index index) {
		this.index = index;
	}

	/**
	 * Gets the direction from the starting point
	 * to the destination.
	 * @return : the direction
	 */
	public TraversalCardinal getDirection() {
		return direction;
	}
	
	/**
	 * Sets the direction of the traversal
	 * @param direction 
	 * @return
	 */
	public TraversalWrapper setDirection(TraversalCardinal direction) {
		this.direction = direction;
		return this;
	}

	/**
	 * Returns the flag of this traversal path
	 * @return
	 */
	public PathFlag getFlag() {
		return flag;
	}

	/**
	 * Sets a flag to this traversal path
	 * @param flag
	 * @return
	 */
	public TraversalWrapper setFlag(PathFlag flag) {
		this.flag = flag;
		return this;
	}

	
	public enum PathFlag{
		VISITED, UNVISITED, OBJECTIVE_CELL
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((flag == null) ? 0 : flag.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TraversalWrapper other = (TraversalWrapper) obj;
		if (direction != other.direction)
			return false;
		if (flag != other.flag)
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		return true;
	}
}
