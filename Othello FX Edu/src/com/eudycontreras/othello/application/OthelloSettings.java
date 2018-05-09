package com.eudycontreras.othello.application;

import com.eudycontreras.othello.enumerations.GameMode;

import main.UserSettings;

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
public class OthelloSettings {
	

	public static final String PLAYER_ONE = getPlayerOneName(UserSettings.PLAYER_ONE);
	public static final String PLAYER_TWO = getPlayerTwoName(UserSettings.PLAYER_TWO);
	
	public static final String STYLESHEET = "application.css";
	public static final String GAME_NAME = "Othello";
	
	public static boolean DEBUG_GAME = false;

	public static final boolean USE_AI_AGENT =UserSettings.GAME_MODE == GameMode.HUMAN_VS_AGENT || UserSettings.GAME_MODE == GameMode.AGENT_VS_AGENT ;

	public static final int DEFAULT_MENU_WIDTH = 200;
	public static final int DEFAULT_BOARD_SIZE = 800;

	public static final int BOARD_GRID_SIZE = UserSettings.BOARD_GRID_SIZE;
	public static final int DEFAULT_BOARD_GRID_SIZE = getBoardSize(BOARD_GRID_SIZE);
	
	public static final double SCENE_SCALE = UserSettings.GAME_WINDOW_SCALE;
	public static final double CELL_SIZE = getCellSize(DEFAULT_BOARD_GRID_SIZE);
	
	public static final int THEME = UserSettings.THEME_INDEX;
	
	private static final double getCellSize(int boardSize){
		if(boardSize == 4){
			return 99 * 2;
		}else if(boardSize == 6){
			return 66 * 2;
		}else if(boardSize == 8){
			return 50 * 2;
		}else{
			return 40;
		}
	}
	
	private static final boolean validBoardSize(int boardSize){
		if(boardSize == 8 || boardSize == 6 || boardSize == 4) return true;
		
		return false;
	}
	
	public static final int getBoardSize(int boardSize){

		if(validBoardSize(boardSize)){
			return boardSize;
		}else{
			return getClosestValidSize(boardSize);
		}
	}
	
	private static int getClosestValidSize(int boardSize) {
		if(boardSize > Integer.MIN_VALUE && boardSize < 4){
			return 4;
		}else if(boardSize == 5){
			return 6;
		}else if(boardSize == 7){
			return 8;
		}else{
			return 8;
		}
	}

	private static final String getPlayerOneName(String name){
		if(UserSettings.GAME_MODE == GameMode.AGENT_VS_AGENT){
			return "Agent One";
	
		}else if(UserSettings.GAME_MODE == GameMode.HUMAN_VS_AGENT){
			
			return "Agent";
		}else{
		
			String fixedName = name;
			
			if(fixedName.length() > 14){
				fixedName = fixedName.substring(0, 14);
			}
			
			return fixedName != null ? fixedName : "Player One";
		}
	}
	
	private static final String getPlayerTwoName(String name){
		if(UserSettings.GAME_MODE == GameMode.AGENT_VS_AGENT){
			return "Agent Two";
		}
		
		String fixedName = name;
		
		if(fixedName.length() > 14){
			fixedName = fixedName.substring(0, 14);
		}
		
		return fixedName != null ? fixedName : "Player Two";
	}
	
	public static class Menu{
		
		public static final String RESET = "Reset";
		public static final String SETTINGS = "Debug";
		public static final String THEMES = "Themes";
		public static final String SCORE_BOARD = "About";
		public static final String EXIT = "Exit";
	}
	
}
