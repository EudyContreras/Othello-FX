package com.eudycontreras.othello.application;

import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.controllers.AgentMove;
import com.eudycontreras.othello.controllers.GameController;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.models.GameBoardCell;

import javafx.application.Application;
import javafx.stage.Stage;

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
public class Othello {

	private GameController gameController;
	
	private AgentController agentController;
	
	private OthelloGame othelloGame; 
	
	private OthelloGameView othelloGameView;
	
	
	public Othello(Stage primaryStage, AgentMove agent) {
		this.gameController = new GameController(this);
		this.agentController = new AgentController(this, agent);
		this.othelloGame = new OthelloGame(OthelloSettings.DEFAULT_BOARD_GRID_SIZE);
		this.othelloGameView = new OthelloGameView(primaryStage,this, OthelloSettings.DEFAULT_BOARD_GRID_SIZE);
		
		this.othelloGameView.setViewCallback(gameController.getViewCallback());
		
		this.gameController.setOthelloGame(othelloGame);
		
		this.gameController.setControllerCallback(othelloGameView.getControllerCallback());
		
		this.othelloGameView.initialize();
		
		this.gameController.initialize();

		this.othelloGameView.openGame(()->{
			this.gameController.setInitialState(0);
			this.setAgentMove();
		});
	}
	
	public void setAgentMove() {
		if(OthelloSettings.USE_AI_AGENT){
			agentController.makeMove(othelloGame.getGameBoard());
		}
	}

	public GameController getGameController() {
		return gameController;
	}
	
	public void log(Object obj){
		log(obj,OthelloSettings.DEBUG_GAME);
	}
	
	public void log(Object obj, boolean debug){
		if(debug){
			this.othelloGameView.getControllerCallback().showOnConsole(obj);
			System.out.println(obj);
		}
	}
	
	public static void printBoard(GameBoardCell[][] cells) {
		printBoard(cells, false);
	}
	
	public static void printBoard(GameBoardCell[][] cells, boolean print) {
		
		if(!print){
			return;
		}
		
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

	public static void println(Object obj) {
		if(OthelloSettings.DEBUG_GAME)
		System.out.println(obj);
	}
	
	public static void print(Object obj) {
		if(OthelloSettings.DEBUG_GAME)
		System.out.print(obj);
	}


}
