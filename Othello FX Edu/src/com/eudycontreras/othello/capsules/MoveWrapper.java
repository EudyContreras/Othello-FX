package com.eudycontreras.othello.capsules;

import com.eudycontreras.othello.enumerations.BoardCellState;
/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * Class which encapsulates information about 
 * a performed move.
 * 
 * @author Eudy Contreras
 *
 */
public class MoveWrapper extends AgentMove{

	private int moveReward;
	
	private Index startIndex;
	
	private BoardCellState target;
	
	public final boolean NULL;

	public MoveWrapper(ObjectiveWrapper objective, int initialWorth){
		this(objective);
		this.moveReward = initialWorth;
	}
	
	public MoveWrapper(ObjectiveWrapper objective){
		if(objective != null){
			this.setObjectiveInformation(objective);
			NULL = false;
		}else{
			NULL = true;
		}
	}
	
	public MoveWrapper(BoardCellState target, Index moveIndex, Index startIndex, int moveLength) {
		super();
		this.moveReward = moveLength;
		this.moveIndex = moveIndex;
		this.startIndex = startIndex;
		this.target = target;
		NULL = false;
	}
	
	
	public void setObjectiveInformation(ObjectiveWrapper objective){
		if(objective.getObjectiveCell() == null){
			return;
		}
		this.target = objective.getObjectiveCell().getCellState();
		this.moveIndex = objective.getObjectiveCell().getIndex();
		this.startIndex = objective.getCurrentCell().getIndex();
		this.moveReward = objective.getPath().size();
	}

	/**
	 * Determines whether the start and move indexes are set
	 * @return
	 */
	@Override
	public boolean isValid() {
		return moveIndex != null && startIndex != null;
	}
	
	/**
	 * Gets the reward obtain by the move
	 * @return the move reward
	 */
	public int getMoveReward() {
		return moveReward;
	}

	/**
	 * Sets the reward obtain by the move
	 * This reward is calculated by the length of 
	 * the move in cells.
	 * @param moveLength : length of the move in cells
	 */
	public void setMoveReward(int moveLength) {
		this.moveReward = moveLength;
	}

	/**
	 * Gets the index from which the move is performed
	 * @return the start index
	 */
	public Index getStartIndex() {
		return startIndex;
	}
	
	/**
	 * Sets the starting index of the move.
	 * @param startIndex: The index from which the move is performed
	 */
	public void setStartIndex(Index startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * Gets the state of final cell which this
	 * move is about to occupy
	 * @return
	 */
	public BoardCellState getTargetState() {
		return target;
	}

	/**
	 * Sets the state of final cell which this
	 * move is about to occupy
	 * @param target : The target cell
	 */
	public void setTargetState(BoardCellState target) {
		this.target = target;
	}
	
	@Override
	public String toString(){
		return "MoveIndex: " + moveIndex + " | startIndex: " + startIndex + " | " + "target: " + target + " | Movelength; " + moveReward;
	}

	/**
	 * Compares the total reward of this move
	 * against the argument
	 */
	@Override
	public int compareTo(AgentMove otherMove) {
		return Integer.compare(getMoveReward(), ((MoveWrapper) otherMove).getMoveReward());
	}
	
}
