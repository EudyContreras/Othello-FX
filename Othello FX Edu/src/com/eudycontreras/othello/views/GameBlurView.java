package com.eudycontreras.othello.views;

import com.eudycontreras.othello.tooling.FXSpan;
import com.eudycontreras.othello.tooling.FXValueAnimator;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
public class GameBlurView {
	
	private StackPane screen = null;
	private StackPane blurContent = null;

	private FXValueAnimator valueAnimator = null;
	
	private ImageView overlay = null;
	
	private GaussianBlur blurEffect = null;
	
	private Runnable blurEndAction;
	private Runnable revertAction;
	
	public GameBlurView(){
		this.valueAnimator = new FXValueAnimator();
		this.blurEffect = new GaussianBlur(0);
		this.overlay = new ImageView();
	}
	
	public void setScreen(StackPane screen){
		this.screen = screen;
	}
	
	public void setBlurContent(StackPane content){
		this.blurContent = content;
	}
	
	public void setBlurEndAction(Runnable action){
		this.blurEndAction = action;
	}
	
	public void setBlurRemoveAction(Runnable action){
		this.revertAction = action;
	}
	
	private void createOverlay(){
		if(blurContent != null){
			WritableImage writableImage = new WritableImage((int) blurContent.getWidth(), (int) blurContent.getHeight());
			SnapshotParameters parameters = new SnapshotParameters();
			parameters.setFill(Color.TRANSPARENT);
			blurContent.snapshot(parameters, writableImage);
			overlay.setImage(writableImage);
			overlay.setEffect(blurEffect);
		}
	}
	
	private void showOverlay(){
		cullRegion(true);
	}
	
	private void hideOverlay(){
		cullRegion(false);
	}
	
	private void cullRegion(boolean state) {
		if(screen!=null && blurContent != null){
			if (state) {
				if(!screen.getChildren().contains(overlay)){
					screen.getChildren().remove(blurContent);
					screen.getChildren().add(0,overlay);
				}
			} else {
				if(!screen.getChildren().contains(blurContent)){
					screen.getChildren().remove(overlay);
					screen.getChildren().add(0,blurContent);
				}
			}
		}
	}

	public void applyBlurAnimation(){
		if(!UserSettings.USE_ANIMATION){
			applyBlur();
			return;
		}
		this.createOverlay();
		this.showOverlay();
		this.valueAnimator.setDelay(400);
		this.valueAnimator.stop();
		this.valueAnimator.setDuration(FXSpan.millis(1000));
		this.valueAnimator.setFrom(0);
		this.valueAnimator.setTo(42);
		this.valueAnimator.setOnFinished(blurEndAction);
		this.valueAnimator.onUpdate(value->{
			blurEffect.setRadius(value);
		});
		this.valueAnimator.play();
	}
	
	public void applyBlur(){
		this.createOverlay();
		this.showOverlay();
		blurEndAction.run();
		blurEffect.setRadius(42);
	}
	
	public void revertBlurAnimation(Runnable script){
		
		if(!UserSettings.USE_ANIMATION){
			revertBlur(script);
			return;
		}
		this.valueAnimator.stop();
		this.valueAnimator.setDuration(FXSpan.millis(700));
		this.valueAnimator.setFrom(42);
		this.valueAnimator.setTo(0);
		this.valueAnimator.setOnFinished(()->{
			hideOverlay();
			if(revertAction != null){
				revertAction.run();
			}
			if(script != null){
				script.run();
			}
		});
		this.valueAnimator.onUpdate(value->{
			blurEffect.setRadius(value);
		});
		this.valueAnimator.play();
	}
	
	public void revertBlur(Runnable script){
		blurEffect.setRadius(0);
		hideOverlay();
		if(revertAction != null){
			revertAction.run();
		}
		if(script != null){
			script.run();
		}
	}
}
