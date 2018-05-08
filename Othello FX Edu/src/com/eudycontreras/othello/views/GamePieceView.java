package com.eudycontreras.othello.views;

import java.util.Random;

import com.eudycontreras.othello.enumerations.PieceType;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
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
public class GamePieceView extends Circle
{
	
	private static final String WHITE = "radial-gradient(focus-angle 45deg, focus-distance 40%, center 50% 50%, radius 75%, white 0%, rgb(70,70,70) 100%)";
	private static final String BLACK = "radial-gradient(focus-angle 45deg, focus-distance 40%, center 50% 50%, radius 75%, rgb(60,60,60) 0%, black 100%)";

	private ScaleTransition scale = new ScaleTransition(Duration.millis(800));
	private RotateTransition rotate = new RotateTransition(Duration.millis(800));

	private Random ramdom = new Random();
	
	private PieceType pieceType = PieceType.WHITE;
	
	private Runnable action;
	
	private int rowIndex = 0;
	private int colIndex = 0;
	
	private int duration = 700;
	
	public GamePieceView(PieceType type, double radius, int popDelay, int row, int col) {
		this(type, radius, popDelay, row, col, false);
	}
	
	public GamePieceView(PieceType type, double radius, int popDelay, int row, int col, boolean initial) {
		super(radius);
		this.rowIndex = row;
		this.colIndex = col;
		this.pieceType = type;
		this.setCache(false);
		this.setPieceType(type);
		this.setScaleX(0);
		this.setScaleY(0);
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.rgb(0,0,0,0.5));
		dropShadow.setRadius(10);
		dropShadow.setSpread(0.35);
		dropShadow.setOffsetX(-6.0);
		dropShadow.setOffsetY(6.0);
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
	
		setEffect(dropShadow);
		
		if(initial){
			
			if(!UserSettings.USE_ANIMATION){
				this.setScaleX(1);
				this.setScaleY(1);
				return;
			}
			rotate.setNode(this);
			rotate.setDuration(Duration.millis(duration));
			rotate.setAxis(Rotate.Z_AXIS);
			rotate.setFromAngle(-720);
			rotate.setToAngle(0);
			rotate.play();
			
			scale.setNode(this);
			scale.setDuration(Duration.millis(duration));
			scale.setFromX(0);
			scale.setFromY(0);
			scale.setToX(1);
			scale.setToY(1);
			scale.setDelay(Duration.millis(popDelay));
			scale.play();
		}else{
			if(UserSettings.USE_ANIMATION){
				scale.setNode(this);
				scale.setDuration(Duration.millis(300));
				scale.setFromX(0);
				scale.setFromY(0);
				scale.setToX(1);
				scale.setToY(1);
				scale.setDelay(Duration.millis(popDelay));
				scale.play();
			}else{
				this.setScaleX(1);
				this.setScaleY(1);
			}
		}
		
		

	}
	   
	public int randInt(int min, int max) {
	    int randomNum = ramdom.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public PieceType getPieceType() {
		return pieceType;
	}


	public void setPieceType(PieceType pieceType) {
		this.pieceType = pieceType;
		switch(pieceType){
		case BLACK:
			setFill(RadialGradient.valueOf(BLACK));
			break;
		case WHITE:
			setFill(RadialGradient.valueOf(WHITE));
			break;
		default:
			break;
		
		}
	}

	public void removeFromBoard(Runnable script) {
		
		if(!UserSettings.USE_ANIMATION){
			this.setScaleX(0);
			this.setScaleY(0);
			if(action != null){
				action.run();
			}
			
			if(script != null){
				script.run();
			}
			return;
		}
		
	
		ScaleTransition scale = new ScaleTransition(Duration.millis(250));
		scale.setNode(this);
		scale.setFromX(1);
		scale.setFromY(1);
		scale.setToX(0);
		scale.setToY(0);
		scale.play();
		scale.setOnFinished(e ->{
			if(action != null){
				action.run();
			}
			
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

	public void setEndAction(Runnable action){
		this.action = action;
	}
}