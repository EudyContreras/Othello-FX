package com.eudycontreras.othello.controllers;

import com.eudycontreras.othello.enumerations.PlayerTurn;

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
public abstract class Agent implements IAgentMove {

	
	protected int searchDepth = 0;
	protected int reachedLeafNodes = 0;
	protected int nodesExamined = 0;
	protected int prunedCounter = 0;

	protected PlayerTurn playerTurn;
	
	protected String agentName;

	public Agent(PlayerTurn playerTurn){
		this.playerTurn = playerTurn;
	}
	
	/**
	 * The name must be less than 15 characters long
	 * Any name with more than 14 characters will be cut off.
	 * @param agentName
	 */
	public Agent(String agentName){
		this.agentName = agentName;
	}
	
	/**
	 * The name must be less than 15 characters long
	 * Any name with more than 14 characters will be cut off.
	 * @param agentName
	 * @param playerTurn
	 */
	public Agent(String agentName, PlayerTurn playerTurn){
		this.agentName = agentName;
		this.playerTurn = playerTurn;
	}
	
	public int getSearchDepth() {
		return searchDepth;
	}

	public void setSearchDepth(int searchDepth) {
		this.searchDepth = searchDepth;
	}

	public int getReachedLeafNodes() {
		return reachedLeafNodes;
	}

	public void setReachedLeafNodes(int reachedLeafNodes) {
		this.reachedLeafNodes = reachedLeafNodes;
	}

	public int getNodesExamined() {
		return nodesExamined;
	}

	public void setNodesExamined(int nodesExamined) {
		this.nodesExamined = nodesExamined;
	}

	public int getPrunedCounter() {
		return prunedCounter;
	}

	public void setPrunedCounter(int prunedCounter) {
		this.prunedCounter = prunedCounter;
	}

	public void resetCounters() {
		this.searchDepth = 0;
		this.reachedLeafNodes = 0;
		this.nodesExamined = 0;
		this.prunedCounter = 0;
	}


	public PlayerTurn getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(PlayerTurn playerTurn) {
		this.playerTurn = playerTurn;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
}
