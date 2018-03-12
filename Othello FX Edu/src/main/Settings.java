package main;

import com.eudycontreras.othello.controllers.AgentController.DeepeningType;

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
 *
 */
public class Settings {
	/**
	 * If deepening logic is use please define here
	 */
	public static final DeepeningType DEEPENING = DeepeningType.NONE;
	/**
	 * Used for defining the maximum search time
	 */
	public static final int MAX_SEARCH_TIME = 4000;
	
	/**
	 * Used for defining the minimum search time
	 */
	public static final int MIN_SEARCH_TIME = 1200;
	
	/**
	 * Used for defining the search time
	 */
	public static final int SEARCH_TIME = 2500;
	
	/**
	 * Used for defining the maximum value
	 */
	public static final int MAX_VALUE = Integer.MAX_VALUE;
	/**
	 * Used for defining the minimum value
	 */
	public static final int MIN_VALUE = Integer.MIN_VALUE;
	/**
	 * The name of the player one : White player
	 */
	public static final String PLAYER_ONE = "Ying";
	/**
	 * The name of the player two : Black player
	 */
	public static final String PLAYER_TWO = "Yang";
	/**
	 * This defines if the AI agent should
	 * be used or not. If false the game will
	 * be started in two human player mode.
	 */
	public static final boolean USE_AI_AGENT = true;
	/**
	 * This defines the scale at which the
	 * game is rendered. This options helps 
	 * for cases where the game resolution is either
	 * too big or to small recommended values range
	 * from 0.4 to 1.4 and a value of 1.0 is default
	 */
	public static final double GAME_WINDOW_SCALE = 1.0;
	/**
	 * The size of the board:
	 * The size can be 4, 6, or 8 any other value
	 * will be converted to the closest valid value
	 * The default size of the Othello board is 8x8
	 */
	public static final int BOARD_GRID_SIZE = 8;
	/**
	 * The theme which is to be set to the board
	 * There are a total of 14 available themes 
	 * Choose a number between 0 and 13. You may also
	 * cycle through themes in game
	 */
	public static final int THEME_INDEX = 0;


}
