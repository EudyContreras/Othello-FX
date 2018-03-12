package com.eudycontreras.othello.views;


import com.eudycontreras.othello.application.OthelloGameView;
import com.eudycontreras.othello.application.OthelloResources;
import com.eudycontreras.othello.application.OthelloSettings;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
public class GameMenuView extends StackPane {

	private static final String NORMAL_BUTTON = "game-menu-button";
	private static final String EXIT_BUTTON = "game-menu-quit-button";
	
	private Button[] menuButtons = new Button[5];
	
	private HBox sideMenu = new HBox();

	private VBox menuItemHolder = new VBox(14);
	
	private GameLogoView label = new GameLogoView(OthelloSettings.GAME_NAME);	
	
	private ImageView logoView = new ImageView(OthelloResources.LOGO);
	
	private DropShadow dropShadow = new DropShadow();
	
	private OthelloGameView main;
	
    public GameMenuView(OthelloGameView main, Scene scene){
    	this.main = main;
		
		dropShadow.setColor(Color.rgb(0,0,0,0.6));
		dropShadow.setRadius(20);
		dropShadow.setSpread(0.15);
		dropShadow.setOffsetX(-8);
		dropShadow.setOffsetY(8);
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
		
		logoView.setEffect(dropShadow);		
		logoView.setPreserveRatio(true);
		logoView.setTranslateX(8);
		//logoView.setRotate(180);
		logoView.setFitWidth(main.getMenuWidth()*1.3);
				
		menuItemHolder.prefHeightProperty().bind(scene.heightProperty());
		menuItemHolder.setPickOnBounds(false);
		menuItemHolder.setPrefWidth(main.getMenuWidth());

		menuButtons[0] = new Button(OthelloSettings.Menu.RESET);
		menuButtons[1] = new Button(OthelloSettings.Menu.THEMES);
		menuButtons[2] = new Button(OthelloSettings.Menu.SETTINGS);
		menuButtons[3] = new Button(OthelloSettings.Menu.SCORE_BOARD);
		menuButtons[4] = new Button(OthelloSettings.Menu.EXIT);
				
		for(Button button : menuButtons){
			button.setId(NORMAL_BUTTON);
			button.setPrefWidth(main.getMenuWidth());
		}
		
		
		menuButtons[4].setId(EXIT_BUTTON);
		
		menuItemHolder.getChildren().add(label);
		menuItemHolder.getChildren().add(main.getGameScoreView());
		menuItemHolder.getChildren().add(logoView);
		menuItemHolder.getChildren().addAll(menuButtons);
		menuItemHolder.setAlignment(Pos.TOP_CENTER);
		menuItemHolder.setPadding(new Insets(20,0,0,0));
		
		sideMenu.setPickOnBounds(false);
		sideMenu.setAlignment(Pos.CENTER);
		sideMenu.getChildren().add(menuItemHolder);

		getChildren().add(sideMenu);
		setPickOnBounds(false);
		addDefaultActions();
    }
    
	public void showLogos() {
		label.showLogos();
	}
    
    private void addDefaultActions(){
    	assignActions(0, ()->{
    		main.resetBoard(50);
    	});
    	assignActions(1, ()->{
    		main.cycleThemes();
    	});
    	assignActions(2, ()->{
    		main.openSettings();
    	});
    	assignActions(3, ()->{
    		main.showAboutView();
    	});
    	assignActions(4, ()-> {
			main.closeGame();
		});
    }
    
    public void assignActions(int buttonIndex, Runnable action){
    	if(action!=null){
    		menuButtons[buttonIndex].setOnAction(e ->{
    			action.run();
    		});
    	}
    }    
    
}