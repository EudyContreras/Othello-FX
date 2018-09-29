package main;

import com.eudycontreras.othello.controllers.AgentController.DeepeningType;
import com.eudycontreras.othello.enumerations.GameMode;

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
public class UserSettings {
	/**
	 * If deepening logic is use please define here
	 */
	public static final DeepeningType DEEPENING = DeepeningType.NONE;
	/**
	 * Determines the game mode to use
	 */
	public static final GameMode GAME_MODE = GameMode.AGENT_VS_AGENT;
	/**
	 * Delay before the game starts when the agent is playing
	 */
	public static final int START_DELAY = 1000;
	/**
	 * Used for defining the maximum search time.
	 * By popular rules the max search time should not
	 * go above 5000 milliseconds;
	 */
	public static final int MAX_SEARCH_TIME = 4000;
	
	/**
	 * Used for defining the minimum search time
	 * If the search is done before the minimum search time
	 * the move will not be made until the the minimum time
	 * has passed. Adjust in order for play consistency. when
	 * Human vs Agent is activated.
	 */
	public static final int MIN_SEARCH_TIME = 0;
	
	/**
	 * Used for defining the search time
	 */
	public static final int SEARCH_TIME = 2500;
	/**
	 * The time it take for each Agent to play when 
	 * Agent vs Agent is active. Adjust to better see
	 * play sequence or to make the game go quick.
	 */
	public static final int TURN_INTERVAL = 0;
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
	 * The name must be less than 15 characters long
	 * Any name with more than 14 characters will be cut off.
	 */
	public static final String PLAYER_ONE = "Player One";
	/**
	 * The name of the player two : Black player
	 * The name must be less than 15 characters long.
	 * Any name with more than 14 characters will be cut off.
	 */
	public static final String PLAYER_TWO = "Player Two";
	/**
	 * Determines whether or not animations should be used 
	 * The animations may reduce performance on some systems. 
	 * Specially on systems running other OS than windows. This
	 * is off by default.
	 */
	public static final boolean USE_ANIMATION = (TURN_INTERVAL >= 1000) ;

	/**
	 * This defines the scale at which the
	 * game is rendered. This options helps 
	 * for cases where the game resolution is either
	 * too big or to small recommended values range
	 * from 0.4 to 1.4 and a value of 1.0 is default
	 * Play around to find your sweet spot.
	 */
	public static final double GAME_WINDOW_SCALE = 0.9d;
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
	
	/**
	 * Weight from high to low. The higher the 
	 * weight the higher the reward. Take a look at
	 * the weight matrix insidet the AgentController to see how
	 * the weights are distributed. Feel free to modify
	 * the weights in order to achieve desired heuristics
	 */
	public static final int A = 256;
	public static final int B = 128;
	public static final int C = 64;
	public static final int D = 8;
	public static final int E = 4;
	public static final int F = 2;
	public static final int G = -128;
	public static final int H = -256;

}
