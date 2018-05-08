package com.eudycontreras.othello.views;

import com.eudycontreras.othello.enumerations.PieceType;

import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
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
public class GameMoveIndicator extends Circle
{
	
	private PieceType pieceType = PieceType.WHITE;

	private int rowIndex = 0;
	private int colIndex = 0;
	
	public GameMoveIndicator(PieceType type, double radius, int row, int col) {
		super(radius);
		this.rowIndex = row;
		this.colIndex = col;
		this.pieceType = type;
		this.setCache(false);
		this.setPieceType(type);
		this.setOpacity(0);
	}
	
	public PieceType getPieceType() {
		return pieceType;
	}

	public void setPieceType(PieceType pieceType) {
		this.pieceType = pieceType;
		switch(pieceType){
		case BLACK:
			setFill(Color.rgb(20, 20, 20, 0.25));
			setStroke(Color.rgb(20, 20, 20, 0.3));
			setStrokeWidth(1.5);
			break;
		case WHITE:
			setFill(Color.rgb(220, 220, 220, 0.25));
			setStroke(Color.rgb(220, 220, 220, 0.3));
			setStrokeWidth(1.5);
			break;
		default:
			break;
		
		}
	}

	public void fadeIn() {
		if(!UserSettings.USE_ANIMATION){
			this.setOpacity(1);
			return;
		}
		FadeTransition fade = new FadeTransition(Duration.millis(250));
		fade.setNode(this);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.play();
	}
	
	public void removeFromBoard(Runnable script) {
		
		if(!UserSettings.USE_ANIMATION){
			this.setOpacity(0);
			if(script != null){
				script.run();
			}
			return;
		}
		
		FadeTransition fade = new FadeTransition(Duration.millis(400));
		fade.setNode(this);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.play();
		fade.setOnFinished(e ->{
			if(script != null){
				script.run();
			}
		});
	}
		

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

}