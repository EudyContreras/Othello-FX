package com.eudycontreras.othello.utilities;

import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardCell;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.ThreadManager.Script;
import com.eudycontreras.othello.threading.ThreadManager.Task;

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
public class GameTreeUtility {

	public static final int DEFAULT_BUILD_TIME = 2500;
	
	public static final int TREE_BUILD_DEPTH = 6;
		
	public static final int TRAVERSAL_DEPTH = 8;
	
	public static final int PAUSE_TIME_LIMIT = 12;
	
	public static final int PAUSE_TIME = 2;
		
	public static int pauseCounter = 0;
	
	public static int moveCounter = 0;
	
	public static void printBoard(GameBoardCell[][] cells) {

		System.out.println();
		
		System.out.print(" ");
		for (int row = 0; row < cells.length; row++) {
			System.out.print("   "+row);
		}
		System.out.println();
		for (int row = 0; row < cells.length; row++) {

			System.out.println("  ---------------------------------");

			System.out.print(row + " |");

			for (int col = 0; col < cells[row].length; col++) {

				if (cells[col][row].getCellState() != BoardCellState.EMPTY) {
					System.out.print(" " + cells[col][row] + " |");
				} else {
					System.out.print(" " + " " + " |");
				}
			}

			System.out.println("");
		}
		System.out.println("  ---------------------------------");
		
		System.out.println();
	}
	
	private static BoardCellState getStateBaseObjective(BoardCellState state){
		if(state == BoardCellState.WHITE){
			return BoardCellState.WHITE_OBJECTIVE;
		}
		else if(state == BoardCellState.BLACK){
			return BoardCellState.BLACK_OBJECTIVE;
		}
		
		return BoardCellState.EMPTY;
	}
	
	public static BoardCellState getCounterState(BoardCellState currentTurn) {
		if(currentTurn == BoardCellState.WHITE){
			return BoardCellState.BLACK;
		}else if(currentTurn == BoardCellState.BLACK){
			return BoardCellState.WHITE;
		}
		return BoardCellState.EMPTY;
	}
	
	public static PlayerTurn getCounterPlayer(PlayerTurn currentTurn) {
		if(currentTurn == PlayerTurn.PLAYER_ONE){
			return PlayerTurn.PLAYER_TWO;
		}else{
			return PlayerTurn.PLAYER_ONE;
		}
	}
	
	private static List<Index> buildTrail(ObjectiveWrapper cell){
		
		List<Index> indexes = new ArrayList<>();

		for(int i = 0; i <  cell.getPath().size(); i++){
			
			indexes.add( cell.getPath().get(i).getIndex());
		}
		
		return indexes;
	}
	
	public static void buildDecissionTree(ThreadManager executorManager, GameBoardState currentState, Script script, int treeBuildDepth){
		buildDecissionTree(executorManager, currentState, script, treeBuildDepth,DEFAULT_BUILD_TIME);
	}
	
	public static void buildDecissionTree(ThreadManager executorManager, GameBoardState currentState, Script script, int treeBuildDepth, int treeBuildTime){
		
		List<GameBoardCell> initialChildren = currentState.getGameBoard().getGameBoardCells(BoardCellState.WHITE);
		
		Task[] tasks = new Task[initialChildren.size()];
		
		for(int i = 0; i<tasks.length; i++){		
			final int index = i;
			
			tasks[i] = ()-> buildInitialBranches(currentState, currentState, initialChildren.get(index), treeBuildDepth, System.currentTimeMillis(), treeBuildTime);
		}
			
		executorManager.setEndScript(script);
		executorManager.setTasks(tasks);
		executorManager.execute();

	}
	
	public static GameBoardState buildDecissionTree(GameBoardState currentState, int treeBuildDepth, int treeBuildTime){
		
		return buildDecissionTree(currentState, currentState, treeBuildDepth, System.currentTimeMillis(), treeBuildTime);
	}
	
	
	private static GameBoardState buildInitialBranches(GameBoardState root, GameBoardState currentState, GameBoardCell node, int buildDepth, long startTime, int treeBuildTime){
	
		moveCounter ++;
		
		List<ObjectiveWrapper> possibleMoves = TraversalUtility.getAvailableCells(node, TRAVERSAL_DEPTH);

		if(!possibleMoves.isEmpty()){				
			
			for (ObjectiveWrapper move : possibleMoves) {
				
				GameBoardState childState = createChildState(root, currentState, move);

				buildDecissionTree(root, childState, buildDepth-1, startTime, treeBuildTime);
				
				currentState.addChildState(childState);
			}	
		}
		
		return root;
		
	}
	
	private static GameBoardState buildDecissionTree(GameBoardState root, GameBoardState currentState, int buildDepth, long startTime, int treeBuildTime){	
		
		increaseCounters();
		
		if(timeLimitExceeded(startTime, treeBuildTime) || buildDepth < 0){
			return root;	
		}
		
		for(int row = 0; row<currentState.getCells().length; row++){		
			for(int col = 0; col<currentState.getCells()[row].length; col++){
			
				GameBoardCell node = currentState.getCells()[row][col];
				
				if(!qualifiedCell(node)){
					continue;
				}
				
				if(node.getCellState() != currentState.getPlayerTurn()){
					continue;
				}
				
				List<ObjectiveWrapper> possibleMoves = TraversalUtility.getAvailableCells(node, TRAVERSAL_DEPTH);

				if(!possibleMoves.isEmpty()){									
					
					for (ObjectiveWrapper move : possibleMoves) {

						GameBoardState childState = createChildState(root, currentState, move);

						buildDecissionTree(root, childState, buildDepth-1, startTime, treeBuildTime);
						
						currentState.addChildStates(childState);
					}	
				}
			}
		}	
		return root;
	}

	private static void increaseCounters() {
		if(pauseCounter >= PAUSE_TIME_LIMIT){
			pause(PAUSE_TIME);	
			pauseCounter = 0;
		}
		
		pauseCounter++;
		moveCounter++;
	}
	
	private static void pause(long waitTime) {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

	private static boolean qualifiedCell(GameBoardCell cell){
		return cell.getCellState() == BoardCellState.BLACK || cell.getCellState() == BoardCellState.WHITE;
	}

	public static GameBoardState createChildState(GameBoardState root, GameBoardState currentState, ObjectiveWrapper move){
		
		GameBoardState state = GameBoardState.createBoardState(null, false, false);
		
		GameBoardCell[][] cells = new GameBoardCell[currentState.getBoardSize()][currentState.getBoardSize()];
			
		Index objectiveIndex = move.getObjectiveCell().getIndex();

		int whiteCounter = 0;
		int blackCounter = 0;
		
		for(int row = 0; row<cells.length; row++){			
			for(int col = 0; col<cells[row].length; col++){
				cells[row][col] = new GameBoardCell(state.getGameBoard(), row, col, currentState.getGameBoard().getGameBoardCell(row,col).getCellState());			

				if(cells[row][col].getCellState() == BoardCellState.WHITE){
					whiteCounter++;
				}else if(cells[row][col].getCellState() == BoardCellState.BLACK){
					blackCounter++;
				}
			}
		}
		
		if(move.getCurrentCell().getCellState() == BoardCellState.WHITE){
			whiteCounter += move.getPath().size();
			blackCounter -= move.getPath().size();
		}else{
			whiteCounter -= move.getPath().size();
			blackCounter += move.getPath().size();
		}
		
		state.setWhiteCount(whiteCounter);
		state.setBlackCount(blackCounter);
		
		state.setParentState(currentState);
		state.setLeadingMove(move);
		
		state.setPlayerTurn(getCounterState(currentState.getPlayerTurn()));
		state.setGameBoardCells(cells);
		
		state.getGameBoard().getGameBoardCell(objectiveIndex).setObjective(getStateBaseObjective(currentState.getPlayerTurn()), buildTrail(move));

		state.getGameBoard().getGameBoardCell(objectiveIndex).setCellState(currentState.getPlayerTurn());
		state.getGameBoard().getGameBoardCell(objectiveIndex).convertEnclosedCells();
		
		state.getGameBoard().resetBuildTrails(BoardCellState.ANY);
	
//		System.out.println("Current State: " + currentState.getPlayerTurn());
//		
//		printBoard(cells);
	
		return state;
	}
	
	private static boolean timeLimitExceeded(long startingTime, int treeBuildTime) {

		long now = System.currentTimeMillis();

		long duration = (now - startingTime);
		
		if (duration > treeBuildTime) {
			return true;
		}

		return false;
	}
}
