package main;

import com.eudycontreras.othello.application.Othello;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
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
 * Class which represents an AI agent which will produce a move
 * Feel free to take a good look at the agent controller for additional
 * helper methods, examples and methods for creating more complex AI agents.
 * 
 * Feel also free to refer to the threading package for multi-threading and timer helpers
 * 
 * <pre>
 *  Execute a task on a separate thread
 * 
 *  ThreadManager.execute(()->{
 * 	 Thing to do here
 *  });
 * 				
 *  Execute a task and perform an action upon completion
 * 				
 *  ThreadManager.execute(
 * 	 (success)-> {
 * 		 Task to perform 
 * 	 },
 * 	 ()-> {
 * 		 Thing to do upon completion
 * 	 });
 *</pre>
 * 
 * @author Eudy Contreras
 *
 */
public class AgentManager extends Application{
	

	/**
	 * Application start method: Choose the agent
	 * you wish to use and run the program
	 */
	@Override
	public void start(Stage primaryStage) {
		new Othello(primaryStage, new ExampleAgentOne("Agent A"), new ExampleAgentTwo("Agent B"));
		//new Othello(primaryStage, new ExampleAgentB());
		//new Othello(primaryStage, new ExampleAgentC());
		//new Othello(primaryStage, new ExampleAgentD());
	}

	public static void main(String[] args) {
		launch(args);
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
}
