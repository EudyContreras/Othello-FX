package com.eudycontreras.othello.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.eudycontreras.othello.application.Othello;
import com.eudycontreras.othello.application.OthelloSettings;
import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.capsules.TraversalWrapper;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.exceptions.NoSpecifiedAgentException;
import com.eudycontreras.othello.exceptions.NotImplementedException;
import com.eudycontreras.othello.models.GameBoard;
import com.eudycontreras.othello.models.GameBoardCell;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.utilities.GameTreeUtility;
import com.eudycontreras.othello.utilities.TraversalUtility;
import static main.UserSettings.A;
import static main.UserSettings.B;
import static main.UserSettings.C;
import static main.UserSettings.D;
import static main.UserSettings.E;
import static main.UserSettings.F;
import static main.UserSettings.G;
import static main.UserSettings.H;

import javafx.application.Platform;
import main.UserSettings;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * Helper class for facilitating AI Agent creation
 * 
 * @author Eudy Contreras
 */
public class AgentController {

	public static final DeepeningType DEEPENING = UserSettings.DEEPENING;

	public static final boolean PRINT_BOARD_STATES = false;
	
	
	public static final int NEIGHBOR_OFFSET_X[] = {-1, -1, 0, 1, 1, 1, 0, -1};
	public static final int NEIGHBOR_OFFSET_Y[] = {0, 1, 1, 1, 0, -1, -1, -1};

	private static final int[][] WEIGHT_MATRIX_8X8 = { 
			{ A, G, B, C, C, B, G, A},
			{ G, H, F, F, F, F, H, G},
			{ B, F, D, D, D, D, F, B},
			{ C, F, D, E, E, D, F, C},
			{ C, F, D, E, E, D, F, C},
			{ B, F, D, D, D, D, F, B},
			{ G, H, F, F, F, F, H, G},
			{ A, G, B, C, C, B, G, A},};
	
	private static final int[][] WEIGHT_MATRIX_6X6 = { 
			{ A, G, B, B, G, A},
			{ G, H, F, F, H, G},
			{ B, F, D, D, F, B},
			{ B, F, D, D, F, B},
			{ G, H, F, F, H, G},
			{ A, G, B, B, G, A},};
	
	private static final int[][] WEIGHT_MATRIX_4X4 = { 
			{ A, G, G, A},
			{ G, H, H, G},
			{ B, F, F, B},
			{ G, H, H, G},
			{ A, G, G, A},};
	
	public static final int[][] WEIGHT_MATRIX = getWeightMatrix(OthelloSettings.DEFAULT_BOARD_GRID_SIZE);

	public static final int MAX_SEARCH_DEPTH = getMaximumDepth(DEEPENING);

	public static final int MAX_VALUE = UserSettings.MAX_VALUE;
	public static final int MIN_VALUE = UserSettings.MIN_VALUE;
	
	private Othello othello;
	
	private Agent agentOne;
	private Agent agentTwo;

	public AgentController(Othello othello, Agent move) {
		this.othello = othello;
		this.agentOne = move;
	}

	public AgentController(Othello othello, Agent agentOne, Agent agentTwo) {
		this.othello = othello;
		this.agentOne = agentOne;
		this.agentTwo = agentTwo;
	}
	
	private Agent getAgent(PlayerTurn player){
		switch(player){
		case PLAYER_ONE:
			if(agentOne == null){
				try {
					throw new NoSpecifiedAgentException("Agent One has not been specified or it is null, Please specified Agent One");
				} catch (NoSpecifiedAgentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return agentOne;
		case PLAYER_TWO:
			if(agentTwo == null){
				try {
					throw new NoSpecifiedAgentException("Agent Two has not been specified or it is null, Please specified Agent Two");
				} catch (NoSpecifiedAgentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return agentTwo;
		default:
			if(agentOne == null){
				try {
					throw new NoSpecifiedAgentException("Agent One has not been specified or it is null, Please specified Agent One");
				} catch (NoSpecifiedAgentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return agentOne;
		
		}
	}

	
	public synchronized void makeMove(GameBoard gameBoard) {
		makeMove(PlayerTurn.PLAYER_ONE, gameBoard);
	}
	
	public synchronized void makeMove(PlayerTurn agentTurn, GameBoard gameBoard) {

		if(othello.getGameController().isGamePaused()){
			return;
		}
		
		Agent agent = getAgent(agentTurn);
		
		
		GameBoardState root = gameBoard.getGameState();

		switch(agentTurn){
		case PLAYER_ONE:
			root.setPlayerTurn(BoardCellState.WHITE);
			break;
		case PLAYER_TWO:
			root.setPlayerTurn(BoardCellState.BLACK);
			break;
		default:
			break;
			
		}
		
		ThreadManager.execute(()->{
			
			AgentMove move = agent.getMove(root);
			
			othello.getGameController().passInformation(
					agent.getSearchDepth(),
					agent.getReachedLeafNodes(), 
					agent.getPrunedCounter(), 
					agent.getNodesExamined());
			
			
			if(OthelloSettings.DEBUG_GAME){
				System.out.println("SEARCH DEPTH: " + agent.getSearchDepth());
				System.out.println("TOTAL NODES PRUNED: " + agent.getPrunedCounter());
				System.out.println("TOTAL LEAFS REACHED: " + agent.getReachedLeafNodes());
				System.out.println("TOTAL NODES EXAMINED: " + agent.getNodesExamined());
				System.out.println();
			}
				
			agent.resetCounters();

			Platform.runLater(() -> {
				if(move != null){
					othello.getGameController().setAgentMove(agentTurn,move);
				}
			});
		});
	}
	
	/**
	 * Method for demonstrating an agent move 
	 * @param gameState
	 * @return
	 */
	public static MoveWrapper getExampleMove(GameBoardState gameState) {
		return getExampleMove(gameState, PlayerTurn.PLAYER_ONE);
	}
	
	public static MoveWrapper getExampleMove(GameBoardState gameState, PlayerTurn playerTurn) {
		int value = new Random().nextInt(3);
		
		if(value == 0){
			return AgentController.findBestMove(gameState, playerTurn);
		}else if(value == 1){
			return AgentController.findSafeMove(gameState, playerTurn);
		}else if(value == 3){
			return AgentController.findRandomMove(gameState, playerTurn);
		}

		return AgentController.findBestMove(gameState, playerTurn);
	}
	
	/**
	 * Example method which returns the move that yields the most immediate reward
	 * @param currentState : The current state of the game
	 * @param turn : The current turn give the state
	 * @return : The move that yields the best score
	 */
	public static MoveWrapper findBestMove(GameBoardState currentState, PlayerTurn turn){
		
		//Best move chosen from a list of moves
		ObjectiveWrapper bestMove = getBestMove(currentState, turn);

		return new MoveWrapper(bestMove);
	}
	/**
	 * Example method which returns a random move from a list of all available moves
	 * @param currentState : The current state of the game
	 * @param turn : The current turn give the state
	 * @return : The random move
	 */
	public static MoveWrapper findRandomMove(GameBoardState currentState, PlayerTurn turn){
		
		//Retrieves and stores all moves for specified player given the current state of the game
		List<ObjectiveWrapper> agentMoves = getAvailableMoves(currentState, turn);
		
		//Random move chosen from the list of moves
		ObjectiveWrapper randomMove = agentMoves.get(new Random().nextInt(agentMoves.size()));
		
		return new MoveWrapper(randomMove);
	}

	/**
	 * Example method which shows a greedy safe move finder algorithm: One step lookahead
	 * @param currentState : The current state of the game
	 * @param turn : The current turn give the state
	 * @return : The safest move possible given a one step lookahead
	 */
	public static MoveWrapper findSafeMove(GameBoardState currentState, PlayerTurn turn) {
			
		//Retrieves and stores all moves for specified player given the current state of the game
		List<ObjectiveWrapper> agentMoves = getAvailableMoves(currentState, turn);
		
		//Stores the moves the adversary of the specified player
		List<ResultWrapper> adversaryMoves = new LinkedList<>();
		
		//Checks if the specified player has no moves and if not it simply returns an empty move wrapper
		if(agentMoves.isEmpty()){
			return new MoveWrapper(null);
		}
			
		//Chooses the first available move as the reference move.
		ObjectiveWrapper chosen = agentMoves.get(0);

		//Loops through all the moves in order to find the safest move
		for(ObjectiveWrapper agentMove : agentMoves){
			
			//Creates a state base on the current state and the current move being looped through
			GameBoardState agentMoveState = getNewState(currentState, agentMove);
			
			//Stores the available moves for the adversary
			List<ObjectiveWrapper>  adversaryMovesTemp = getAvailableMoves(agentMoveState, GameTreeUtility.getCounterPlayer(turn));
			
			//Loops through all the adversary moves in order to perform a score calculation
			//This is made to check if the resulting agent move lead to a high adversary profit or not
			for(ObjectiveWrapper adversaryMove : adversaryMovesTemp){
				
				//Stores the state created by the current adversary moves
				GameBoardState adversaryMoveState = getNewState(agentMoveState, adversaryMove);
				
				//The score of the agent given the current adversary move state
				long agentScore = adversaryMoveState.getStaticScore(BoardCellState.WHITE);
				
				//The score of the adversary given the current adversary move state
				long adversaryScore = adversaryMoveState.getStaticScore(BoardCellState.BLACK);
				
				//Checks if the score of the adversary is lower
				//If so and the total value gain from the current move is higher
				//than the value of the currently chosen move this new move is then chosen
				if(adversaryScore < agentScore){
					
					//If the total gain of the chosen move is less than the current move, the current move is then chosen
					if(chosen.getPath().size() <= agentMove.getPath().size()){
						
						chosen = agentMove;
					}	
				}
			}
			
			//Gets the best move that the adversary can make given the current state
			ObjectiveWrapper bestMove = getBestMove(agentMoveState, GameTreeUtility.getCounterPlayer(turn));
			
			if(bestMove == null){
				continue;
			}
		
			//The best move is then added to the list of adversary moves as a result
			adversaryMoves.add(new ResultWrapper(turn, agentMoveState, bestMove, agentMove));
		
		}
		
		//Returns best move if adversary has no move
		if(adversaryMoves.isEmpty()){
			return findBestMove(currentState,turn);
		}
		
		//Sorts the adversary moves in reversed order given the total reward of the move of the agent
		Collections.sort(adversaryMoves, Collections.reverseOrder(ResultWrapper.AGENT_REWARD_COMPARATOR));
		
		//Deals with unconventional states
		if(adversaryMoves.get(0).getAgentMove() == null || adversaryMoves.get(0).getHumanMove() == null){
			return findBestMove(currentState,turn);
		}
		if(nullOrEmpty(adversaryMoves.get(0).getAgentMove().getPath()) || nullOrEmpty(adversaryMoves.get(0).getHumanMove().getPath())){
			return findBestMove(currentState,turn); 
		}
		
		//If in the context of the worst move of the adversary the resulting move the of agent has a lesser
		//reward than the resulting move of the adversary the adversary moves are re-sorted using the adversary
		//reward comparator.
		if(adversaryMoves.get(0).getAgentMove().getPath().size() <= adversaryMoves.get(0).getHumanMove().getPath().size()){
			
			Collections.sort(adversaryMoves, ResultWrapper.HUMAN_REWARD_COMPARATOR);
		}
		
		//Returns a move wrapper containing the move of the agent that was
		//a result of the worst move of the adversary
		return new MoveWrapper(adversaryMoves.get(0).getAgentMove());		
	}
	
	
	public static <T> boolean nullOrEmpty(List<T> list){
		return list.isEmpty() || list == null;
	}
	
	/**
	 * Returns the best move for said player given said state.
	 * @param state : The current state of the board
	 * @param player : The current player
	 * @return
	 */
	public static ObjectiveWrapper getBestMove(GameBoardState state, PlayerTurn player){
		
		List<ObjectiveWrapper> moves = getAvailableMoves(state, player);
		
		if(moves.isEmpty()){
			return null;
		}
		
		ObjectiveWrapper move = getBestMove(moves);
		
		if(move == ObjectiveWrapper.NONE) return null;
		
		return move;
	}
	
	/**
	 * Returns the best move given a list of moves
	 * @param moves: List of moves
	 * @return
	 */
	private static ObjectiveWrapper getBestMove(List<ObjectiveWrapper> moves) {

		if(moves.isEmpty()){
			return ObjectiveWrapper.NONE;
		}
		
		ObjectiveWrapper longest = moves.get(0);

		for (int i = 0; i<moves.size(); i++) {

			if (moves.get(i).getPath().size() > longest.getPath().size()) {

				longest = moves.get(i);
			}
		}
		return longest;
	}
	
	/**
	 * Returns the available moves for said player given the state of the board
	 * @param state : The state of the board
	 * @param player : The current player
	 * @return
	 */
	public static List<ObjectiveWrapper> getAvailableMoves(GameBoardState state, PlayerTurn player){

		List<GameBoardCell> cells = state.getGameBoard().getGameBoardCells(player);
		
		List<ObjectiveWrapper> moves = new LinkedList<>();
		
		for(GameBoardCell cell : cells){
			
			for(ObjectiveWrapper move: TraversalUtility.getAvailableCells(cell, GameTreeUtility.TRAVERSAL_DEPTH)){
				
				if(moves.isEmpty()){
					moves.add(move);
				}else{
					if(moves.get(0).getPath().size() < move.getPath().size()){
						moves.add(0,move);
					}else{
						moves.add(move);
					}
				}
			}
		}	
		
		return moves;
	}

	/**
	 * Returns a strategy to use given how full the board is.
	 * @param state: The state of the board
	 * @return
	 */
	public static StrategyType getStrategyType(GameBoardState state){
		int earlyCount = (int)(state.getTotalCount() * 0.3333);
		int midCount = (int)(state.getTotalCount() * 0.5);
		
		if(state.getTotalCount() <= earlyCount){
			return StrategyType.EARLY_GAME;
		}else if(state.getTotalCount() > earlyCount && state.getTotalCount() < midCount){
			return StrategyType.MID_GAME;
		}else{
			return StrategyType.LATE_GAME;
		}
	}
	
	/**
	 * Returns new state based on given state and the given move.
	 * @param state: The state of the board
	 * @param move: The move.
	 * @return
	 */
	public static GameBoardState getNewState(GameBoardState state, ObjectiveWrapper move){
		
		return GameTreeUtility.createChildState(null, state, move);
	}
	
	/**
	 * Returns an evaluation given a state
	 * @param state: The state of the board
	 * @return
	 */
	public static double getGameEvaluation(GameBoardState state,  PlayerTurn playerTurn){	
		return getGameEvaluation(state, getStrategyType(state),playerTurn);
	}
	
	/**
	 * Returns an evaluation given the state of a board and a strategy
	 * @param state: The state of the board
	 * @param stradegy : Strategy
	 * @return
	 */
	public static double getGameEvaluation(GameBoardState state, StrategyType stradegy,  PlayerTurn playerTurn){	
		return getGameEvaluation(state, stradegy, HeuristicType.DYNAMIC, playerTurn);
	}
	
	/**
	 * Returns an evaluation given the state of the board and a type of heuristic
	 * @param state: The state of the board
	 * @param heuristicType: A type of heuristic
	 * @return
	 */
	public static double getGameEvaluation(GameBoardState state, HeuristicType heuristicType,  PlayerTurn playerTurn){	
		return getGameEvaluation(state,  getStrategyType(state), heuristicType, playerTurn);
	}
	

	public static double getGameEvaluation(GameBoardState state, StrategyType evaluation, HeuristicType heuristicType,  PlayerTurn playerTurn){	
		switch(evaluation){
		case EARLY_GAME:
			return heuristicEvaluation(state, heuristicType, playerTurn);
		case MID_GAME:
			return heuristicEvaluation(state, heuristicType, playerTurn);
		case LATE_GAME:
			return heuristicEvaluation(state, heuristicType, playerTurn);
		default:
			return heuristicEvaluation(state, heuristicType, playerTurn);
		
		}	
	}
	
	/**
	 * Return the terminal evaluation given a state
	 * @param state:  The state of the board
	 * @return
	 */
	public static int getTerminalEvaluation(GameBoardState state){
		GameBoardCell[][] grid = state.getGameBoard().getCells();
		
		int agentPieces = 0;
		int humanPieces = 0;

		for(int row=0; row<grid.length; row++){
			for(int col=0; col<grid[row].length; col++)  {
				if(grid[row][col].getCellState() == BoardCellState.WHITE)  {
					agentPieces++;
				} else if(grid[row][col].getCellState() == BoardCellState.BLACK)  {
					humanPieces++;
				}
			}
		}
		
		if((agentPieces + humanPieces) == (state.getBoardSize() * state.getBoardSize())){
			
			if(agentPieces > humanPieces){
				return MAX_VALUE;
			}else if(agentPieces < humanPieces){
				return MIN_VALUE;
			}else{
				return 0;
			}
		}
		return 0;
	}
	
	public static double getDifferentiationHeuristic(GameBoardState state){

		GameBoardCell[][] grid = state.getGameBoard().getCells();
		
		int agentPieces = 0;
		int humanPieces = 0;
			
		double multiplier = 100.0;
		
		for(int row=0; row<grid.length; row++){
			for(int col=0; col<grid[row].length; col++)  {
				if(grid[row][col].getCellState() == BoardCellState.WHITE)  {
					agentPieces++;
				} else if(grid[row][col].getCellState() == BoardCellState.BLACK)  {
					humanPieces++;
				}
			}
		}
		
		if(agentPieces > humanPieces){
			return (multiplier * agentPieces)/(agentPieces + humanPieces);
		}else if(agentPieces < humanPieces){
			return -(multiplier * humanPieces)/(agentPieces + humanPieces);
		}
		return 0;	
	}
	
	public static double getStabilityHeuristic(GameBoardState state){
		return getVulnerabilityHeuristic(getAvailableMoves(state, PlayerTurn.PLAYER_ONE),getAvailableMoves(state, PlayerTurn.PLAYER_TWO),state);
	}
	
	public static double getVulnerabilityHeuristic(List<ObjectiveWrapper> agentMoves, List<ObjectiveWrapper> humanMoves, GameBoardState state){
		int unstableAgentPieces = 0;
		int unstableHumanPieces = 0;
		
		double multiplier = 10.0;
		
		for(ObjectiveWrapper move : agentMoves){
			unstableHumanPieces += move.getPath().size();
		}
		for(ObjectiveWrapper move : humanMoves){
			unstableAgentPieces += move.getPath().size();
		}
		
		if(unstableHumanPieces < unstableAgentPieces){
			return -(multiplier * unstableAgentPieces)/(unstableAgentPieces + unstableHumanPieces);
		}else if(unstableAgentPieces < unstableAgentPieces){
			return (multiplier * unstableHumanPieces)/(unstableAgentPieces + unstableHumanPieces);
		}
		
		return 0;
	}
	
	public static double getLineDominationHeuristic(GameBoardState state){

		try {
			throw new NotImplementedException("Line domination heuristic has no implementation! Add an implementation and try again.");
		} catch (NotImplementedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Double.NaN;
	
	}
	
	public static double getMobilityHeuristic(GameBoardState state){
		return getMobilityHeuristic(getAvailableMoves(state, PlayerTurn.PLAYER_ONE),getAvailableMoves(state, PlayerTurn.PLAYER_TWO), state);
	}
	
	public static double getMobilityHeuristic(List<ObjectiveWrapper> agentMovesP, List<ObjectiveWrapper> humanMovesP, GameBoardState state){
		
		int agentMoves = agentMovesP.size();
		int humanMoves = humanMovesP.size();
		
		double multiplier = 100.0;
		
		if(agentMoves > humanMoves){
			return (multiplier * agentMoves)/(agentMoves + humanMoves);
		}else if(agentMoves < humanMoves){
			return -(multiplier * humanMoves)/(agentMoves + humanMoves);
		}
		return 0;
	}
	

	public static double getCornerDominationHeuristic(GameBoardState state){
		GameBoardCell[][] grid = state.getGameBoard().getCells();
		
		int count = grid.length-1;
		
		int[] x = {0, 0, count, count};
		int[] y = {0, count, 0, count};
		
		int agentCorners = 0;
		int humanCorners = 0;
		
		double multiplier = 25.0;
		
		for(int i = 0; i<x.length; i++){	
			switch(grid[x[i]][y[i]].getCellState()){
			case BLACK:
				humanCorners++;
				break;
			case WHITE:
				agentCorners++;
				break;
			default:
				break;
			}
		}

		return multiplier * (agentCorners - humanCorners);
	}
	
	public static double getCornerProximityHeuristic(GameBoardState state){

		GameBoardCell[][] grid = state.getGameBoard().getCells();

		int count = grid.length-1;
		
		int agentCorners = 0;
		int humanCorners = 0;
		
		double multiplier = -12.5;	

		if(!qualifiedCell(grid[0][0]))   {
			if(grid[0][1].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[0][1].getCellState() == BoardCellState.BLACK) humanCorners++;
			if(grid[1][1].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[1][1].getCellState() == BoardCellState.BLACK) humanCorners++;
			if(grid[1][0].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[1][0].getCellState() == BoardCellState.BLACK) humanCorners++;
		}
		if(!qualifiedCell(grid[0][count]))   {
			if(grid[0][count-1].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[0][count-1].getCellState() == BoardCellState.BLACK) humanCorners++;
			if(grid[1][count-1].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[1][count-1].getCellState() == BoardCellState.BLACK) humanCorners++;
			if(grid[1][count].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[1][count].getCellState() == BoardCellState.BLACK) humanCorners++;
		}
		if(!qualifiedCell(grid[count][0]))   {
			if(grid[count][1].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[count][1].getCellState() == BoardCellState.BLACK) humanCorners++;
			if(grid[count-1][1].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[count-1][1].getCellState() == BoardCellState.BLACK) humanCorners++;
			if(grid[count-1][0].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[count-1][0].getCellState() == BoardCellState.BLACK) humanCorners++;
		}
		if(!qualifiedCell(grid[count][count]))   {
			if(grid[count-1][count].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[count-1][count].getCellState() == BoardCellState.BLACK) humanCorners++;
			if(grid[count-1][count-1].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[count-1][count-1].getCellState() == BoardCellState.BLACK) humanCorners++;
			if(grid[count][count-1].getCellState() == BoardCellState.WHITE) agentCorners++;
			else if(grid[count][count-1].getCellState() == BoardCellState.BLACK) humanCorners++;
		}

		return multiplier * (agentCorners - humanCorners);
	}
	
	public static Heuristic getPieceInformation(GameBoardState state){

		Heuristic heuristic = new Heuristic();
		
		GameBoardCell[][] grid = state.getGameBoard().getCells();
		
		int agentPieces = 0;
		int humanPieces = 0;
			
		int weightHeuristic = 0;
		
		int agentFrontierPieces = 0;
		int humanFrontierPieces = 0;

		int offsetX = 0;
		int offsetY = 0;
		
		int minRow = 0;
		int minCol = 0;
		
		int maxRow = grid.length;
		int maxCol = grid[0].length;
		
		double multiplier = 100.0;

		double frontierHeuristic = 0;
		double staticHeuristic = 0;
		
		for(int row=0; row<grid.length; row++){
			for(int col=0; col<grid[row].length; col++)  {
				
				if(!qualifiedCell(grid[row][col])){
					continue;
				}
				
				if(grid[row][col].getCellState() == BoardCellState.WHITE)  {
					weightHeuristic += WEIGHT_MATRIX[row][col];
					agentPieces++;
				} else if(grid[row][col].getCellState() == BoardCellState.BLACK)  {
					weightHeuristic -= WEIGHT_MATRIX[row][col];
					humanPieces++;
				}
				
				for(int offset=0; offset<grid.length; offset++)  {
					
					offsetX = row + NEIGHBOR_OFFSET_X[offset]; 
					offsetY = col + NEIGHBOR_OFFSET_Y[offset];
				
					if(offsetX >= minRow && offsetX < maxRow && offsetY >= minCol && offsetY < maxCol) {
						
						if(!qualifiedCell(grid[offsetX][offsetY])){
							
							if(grid[row][col].getCellState() == BoardCellState.WHITE){
								agentFrontierPieces++;
							}else if(grid[row][col].getCellState() == BoardCellState.BLACK){
								humanFrontierPieces++;
							}
							
							break;						
						}						
					}
				}
			}
		}
		
		if(agentPieces > humanPieces){
			staticHeuristic = (multiplier * agentPieces)/(agentPieces + humanPieces);
		}else if(agentPieces < humanPieces){
			staticHeuristic = -(multiplier * humanPieces)/(agentPieces + humanPieces);
		}
		
		if(agentFrontierPieces > humanFrontierPieces){
			frontierHeuristic = -(multiplier * agentFrontierPieces)/(agentFrontierPieces + humanFrontierPieces);
		}else if(agentFrontierPieces < humanFrontierPieces){
			frontierHeuristic = (multiplier * humanFrontierPieces)/(agentFrontierPieces + humanFrontierPieces);
		}
		
		heuristic.setWeight(weightHeuristic);
		
		heuristic.setBlackCount(humanPieces);
		heuristic.setWhiteCount(agentPieces);
		
		heuristic.setBlackFrontiers(humanFrontierPieces);
		heuristic.setWhiteFrontiers(agentFrontierPieces);
		
		heuristic.setStaticHeuristic(staticHeuristic);
		heuristic.setFrontierHeuristic(frontierHeuristic);
		
		return heuristic;
	}

	public static double getFrontierHeuristic(GameBoardState state){
		GameBoardCell[][] grid = state.getGameBoard().getCells();
		
		int agentFrontierPieces = 0;
		int humanFrontierPieces = 0;
		
		int neighborOffsetX[] = {-1, -1, 0, 1, 1, 1, 0, -1};
		int neighborOffsetY[] = {0, 1, 1, 1, 0, -1, -1, -1};

		int offsetX = 0;
		int offsetY = 0;
		
		int minRow = 0;
		int minCol = 0;
		
		int maxRow = grid.length;
		int maxCol = grid[0].length;
		
		double multiplier = 100.0;

		for(int row=0; row<grid.length; row++)
			for(int col=0; col<grid[row].length; col++)  {
				
				if(!qualifiedCell(grid[row][col])){
					continue;
				}
				for(int offset=0; offset<grid.length; offset++)  {
					
					offsetX = row + neighborOffsetX[offset]; 
					offsetY = col + neighborOffsetY[offset];
				
					if(offsetX >= minRow && offsetX < maxRow && offsetY >= minCol && offsetY < maxCol) {
						
						if(!qualifiedCell(grid[offsetX][offsetY])){
							
							if(grid[row][col].getCellState() == BoardCellState.WHITE){
								agentFrontierPieces++;
							}else if(grid[row][col].getCellState() == BoardCellState.BLACK){
								humanFrontierPieces++;
							}
							
							break;						
						}						
					}
				}
			}
		
		if(agentFrontierPieces > humanFrontierPieces){
			return -(multiplier * agentFrontierPieces)/(agentFrontierPieces + humanFrontierPieces);
		}else if(agentFrontierPieces < humanFrontierPieces){
			return (multiplier * humanFrontierPieces)/(agentFrontierPieces + humanFrontierPieces);
		}
		
		return 0;
	}
	
	public static double getDynamicHeuristic(GameBoardState state)  {

		Heuristic heuristics = getPieceInformation(state);
		
		List<ObjectiveWrapper> agentMoves = getAvailableMoves(state, PlayerTurn.PLAYER_ONE);
		List<ObjectiveWrapper> humanMoves = getAvailableMoves(state, PlayerTurn.PLAYER_TWO);

		double staticMultiplier = 10.0;
		double cornerMultiplier = 801.724;
		double proximityMultiplier = 382.026;
		double mobilityMultiplier = 78.922;
		double frontierMultiplier = 74.396;
		double vulnerabilityMultiplier = 0;
		double weightMultiplier = 10.0;
		
		double cornerDomination = getCornerDominationHeuristic(state);

		double cornerProximity = getCornerProximityHeuristic(state);

		double mobilityCapability = getMobilityHeuristic(agentMoves, humanMoves, state);
		
		double vurnerability = getVulnerabilityHeuristic(agentMoves, humanMoves, state);
		
		double frontierProned = heuristics.getFrontierHeuristic();
		
		double staticEvaluation = heuristics.getStaticHeuristic();
		
		double positionWeight = heuristics.getPositionalWeight();

		return  (staticMultiplier * staticEvaluation) + 
				(cornerMultiplier * cornerDomination) + 
				(proximityMultiplier * cornerProximity) + 
				(mobilityMultiplier * mobilityCapability) + 
				(frontierMultiplier * frontierProned) + 
				(weightMultiplier * positionWeight +
				(vulnerabilityMultiplier * vurnerability));
	}
	
	public static boolean isTerminal(GameBoardState state, PlayerTurn player){
		
		GameBoardCell[][] grid = state.getGameBoard().getCells();
		
		int agentPieces = 0;
		int humanPieces = 0;

		for(int row=0; row<grid.length; row++){
			for(int col=0; col<grid[row].length; col++)  {
				if(grid[row][col].getCellState() == BoardCellState.WHITE)  {
					agentPieces++;
				} else if(grid[row][col].getCellState() == BoardCellState.BLACK)  {
					humanPieces++;
				}
			}
		}
		
		
		if((agentPieces + humanPieces) == (state.getBoardSize() * state.getBoardSize())){			
			return true;
		}else{
			if(getAvailableMoves(state, player).isEmpty()){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean timeLimitExceeded(int maxSearchTime, long startingTime) {
		return getElapsedTime(startingTime) > maxSearchTime;
	}
	
	public static long getRemainingTime(int maxSearchTime, long startingTime) {
		return maxSearchTime - getElapsedTime(startingTime) ;
	}
	
	public static long getElapsedTime(long startingTime) {

		long now = System.currentTimeMillis();

		return (now - startingTime);
	}
	
	public static int computeMaxDepth(int maxSearchTime, int searchDepth, int depthLimit, long startTime, DeepeningType deepening) {
		if (deepening == DeepeningType.DYNAMIC_DEEPENING) {
			if(searchDepth >= depthLimit){
				if(!timeLimitExceeded(maxSearchTime,startTime) && getRemainingTime(maxSearchTime,startTime) > maxSearchTime*0.1){				
					return depthLimit+1;
				}
			}
		}	
		return depthLimit;
	}
	
	private static int getMaximumDepth(DeepeningType deepening) {
		switch(deepening){
		case DYNAMIC_DEEPENING:
			return 6;
		case ITERATIVE_DEEPENING:
			return 1000;
		case NONE:
			return 6;
		default:
			return 6;
		
		}
	}
	
	private static int[][] getWeightMatrix(int boardSize){
		switch(boardSize){
		case 4:
			return AgentController.WEIGHT_MATRIX_4X4;
		case 6:
			return AgentController.WEIGHT_MATRIX_6X6;
		case 8:
			return AgentController.WEIGHT_MATRIX_8X8;
		default:
			return AgentController.WEIGHT_MATRIX_8X8;
		}
	}

	public static boolean cutOffTest(GameBoardState state, int maxSearchTime, int searchDepth, int depthLimit, long startTime) {
		
		if(searchDepth >= depthLimit){
			if(!timeLimitExceeded(maxSearchTime,startTime)){					
				if(getElapsedTime(startTime) >= maxSearchTime * 0.35){
					return true;
				}else{
					return false;		
				}
			}else{
				return true;
			}
		}
		else{
			if(timeLimitExceeded(maxSearchTime,startTime)){			
				return true;
			}
		}

		return false;
	}

	public static double heuristicEvaluation(GameBoardState state, HeuristicType heuristic, PlayerTurn player) {
		switch(heuristic){
		case CORNER_DOMINATION:
			return getCornerDominationHeuristic(state);
		case DIFFERENTIATION:
			return getDifferentiationHeuristic(state);
		case MOBILITY:
			return getMobilityHeuristic(state);
		case STABILITY:
			return getStabilityHeuristic(state);
		case FRONTIER:
			return getFrontierHeuristic(state);
		case DYNAMIC:
			return getDynamicHeuristic(state);
		default:
			break;

		}
		
		return getDifferentiationHeuristic(state);
	}
	
	private static boolean qualifiedCell(GameBoardCell cell){
		return cell.getCellState() == BoardCellState.BLACK || cell.getCellState() == BoardCellState.WHITE;
	}

	public static enum HeuristicType{
		DIFFERENTIATION,
		
		MOBILITY,
		
		CORNER_DOMINATION,
		
		CORNER_PROXIMITY,
		
		LINE_DOMINATION,
		
		VULNERABILITY,
		
		STABILITY,
		
		FRONTIER,
		
		DYNAMIC
	}
	
	public static enum StrategyType{
		EARLY_GAME,
		
		MID_GAME,
		
		LATE_GAME
		
	}
	
	public static enum Stability{
		LOW,
		
		MEDIUM,
		
		HIGH,
	}
	
	public static enum DeepeningType{	
		ITERATIVE_DEEPENING,
		
		DYNAMIC_DEEPENING,
		
		NONE
	}
	
	public static class Heuristic{
		
		private int weight;
		
		private int whiteCount;
		private int blackCount;
		
		private int whiteFrontiers;
		private int blackFrontiers;
		
		private double frontierHeuristic;
		private double staticHeuristic;
		
		public int getPositionalWeight() {
			return weight;
		}
		
		public double getFrontierHeuristic() {
			return frontierHeuristic;
		}

		public double getStaticHeuristic() {
			return staticHeuristic;
		}

		public void setFrontierHeuristic(double frontierHeuristic) {
			this.frontierHeuristic = frontierHeuristic;
		}
		
		public void setStaticHeuristic(double staticHeuristic) {
			this.staticHeuristic = staticHeuristic;
		}

		public void setWeight(int difference) {
			this.weight = difference;
		}

		public int getWhiteCount() {
			return whiteCount;
		}

		public void setWhiteCount(int whiteCount) {
			this.whiteCount = whiteCount;
		}

		public int getBlackCount() {
			return blackCount;
		}

		public void setBlackCount(int blackCount) {
			this.blackCount = blackCount;
		}

		public int getWhiteFrontiers() {
			return whiteFrontiers;
		}

		public void setWhiteFrontiers(int whiteFrontiers) {
			this.whiteFrontiers = whiteFrontiers;
		}

		public int getBlackFrontiers() {
			return blackFrontiers;
		}

		public void setBlackFrontiers(int blackFrontiers) {
			this.blackFrontiers = blackFrontiers;
		}

	}
	
	public static class Result{
		
		private Score score;
		private Move move;
		
		public Result(Score score, Move move) {
			super();
			this.score = score;
			this.move = move;
		}

		public Score getScore() {
			return score;
		}

		public void setScore(Score score) {
			this.score = score;
		}

		public Move getMove() {
			return move;
		}

		public void setMove(Move move) {
			this.move = move;
		}
		
	}
	
	public static class Score{

		public static final Score DEFAULT = getDefault(0);
		
		public static final Score MAX_SCORE = getDefault(Integer.MAX_VALUE);
		
		public static final Score MIN_SCORE = getDefault(Integer.MIN_VALUE);
			
		private double value;
				
		public Score(double value) {
			this.value = value;
		}

		private static Score getDefault(double value){
			return new Score(value);
		}
	
		public void setValue(double value){
			this.value = value;
		}
		
		public int getValue(){
			return (int)value;
		}
	}

	public static class Move{
		
		public static final Move DEFAULT = getDefault();
		
		private MoveWrapper move;

		public Move(ObjectiveWrapper move){
			this.move = new MoveWrapper(move);
		}

		private static Move getDefault(){
			return new Move(null);
		}

		public MoveWrapper getMove() {
			return move;
		}

		public void setMove(ObjectiveWrapper move) {
			this.move = new MoveWrapper(move);
		}
	}
	
	public static class ResultWrapper{
		
		public static final Comparator<ResultWrapper> HUMAN_REWARD_COMPARATOR = ResultWrapper.getHumanRewardComparator();

		public static final Comparator<ResultWrapper> AGENT_REWARD_COMPARATOR = ResultWrapper.getAgentRewardComparator();
		
		public static Comparator<ResultWrapper> STATIC_SCORE_COMPARATOR;
		
		private GameBoardState state;
		
		private ObjectiveWrapper humanMove;
		
		private ObjectiveWrapper agentMove;

		public ResultWrapper(PlayerTurn playerTurn, GameBoardState state, ObjectiveWrapper adversaryMove, ObjectiveWrapper aiMove) {
			super();
			this.state = state;
			this.humanMove = adversaryMove;
			this.agentMove = aiMove;
			switch(playerTurn){
			case PLAYER_ONE:
				STATIC_SCORE_COMPARATOR = ResultWrapper.getStaticScoreComparator(BoardCellState.WHITE);
				break;
			case PLAYER_TWO:
				STATIC_SCORE_COMPARATOR = ResultWrapper.getStaticScoreComparator(BoardCellState.BLACK);
				break;
			default:
				break;
				
			}
		
		}

		public ObjectiveWrapper getHumanMove() {
			return humanMove;
		}

		public ObjectiveWrapper getAgentMove() {
			return agentMove;
		}
		
		public GameBoardState getState() {
			return state;
		}

		public static Comparator<ResultWrapper> getHumanRewardComparator(){
			return new Comparator<ResultWrapper>(){

				@Override
				public int compare(ResultWrapper arg0, ResultWrapper arg1) {
					
					ObjectiveWrapper obj0 = arg0.getHumanMove();
					ObjectiveWrapper obj1 = arg1.getHumanMove();
					
					List<TraversalWrapper> path0 = obj0.getPath();
					List<TraversalWrapper> path1 = obj1.getPath();
					
					return Integer.compare(path0.size(), path1.size());
				}
				
			};
		}

		private static Comparator<ResultWrapper> getAgentRewardComparator() {
			return new Comparator<ResultWrapper>(){

				@Override
				public int compare(ResultWrapper arg0, ResultWrapper arg1) {
					return Integer.compare(arg0.getAgentMove().getPath().size(), arg1.getAgentMove().getPath().size());
				}
				
			};
		}

		private static Comparator<ResultWrapper> getStaticScoreComparator(BoardCellState boardCellState) {
			return new Comparator<ResultWrapper>(){

				@Override
				public int compare(ResultWrapper arg0, ResultWrapper arg1) {
					return Integer.compare((int)arg0.getState().getStaticScore(boardCellState), (int)arg1.getState().getStaticScore(boardCellState));
				}			
			};
		}
	}
}
