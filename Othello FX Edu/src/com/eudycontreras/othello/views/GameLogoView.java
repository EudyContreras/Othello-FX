package com.eudycontreras.othello.views;

import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
public class GameLogoView extends HBox {

	private Text javaFX = new Text("FX");
	private Text othello = new Text();
	
	public GameLogoView(String label) {
		othello.setText(label);
		othello.setFont(Font.font(null, FontWeight.BOLD, 62));

		javaFX.setFont(Font.font(null, FontWeight.BOLD, 62));

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.rgb(0, 0, 0, 0.8));
		dropShadow.setRadius(15);
		dropShadow.setSpread(0.25);
		dropShadow.setOffsetX(-8.0);
		dropShadow.setOffsetY(8.0);
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
		javaFX.setEffect(dropShadow);

		DropShadow dropShadow2 = new DropShadow();
		dropShadow2.setColor(Color.rgb(0, 0, 0, 1));
		dropShadow2.setRadius(15);
		dropShadow2.setSpread(0.25);
		dropShadow2.setOffsetY(4.0);
		dropShadow2.setOffsetX(-4.0);
		dropShadow2.setBlurType(BlurType.THREE_PASS_BOX);
		othello.setEffect(dropShadow2);

		othello.setId("othello-label");
		othello.setStroke(Color.rgb(100, 100, 100));
		javaFX.setId("java-fx-label");
		javaFX.setStroke(Color.LIMEGREEN);
		javaFX.setStrokeType(StrokeType.OUTSIDE);
		javaFX.setStrokeWidth(2);
	
		setSpacing(6);
		setPickOnBounds(false);
		setAlignment(Pos.CENTER);
		setPadding(new Insets(0, 16, 0, 10));
		getChildren().addAll(othello, javaFX);
	}

	public void showLogos() {
		if(!UserSettings.USE_ANIMATION){
			return;
		}
		ScaleTransition scale = new ScaleTransition(Duration.millis(1000));
		scale.setNode(javaFX);
		scale.setFromX(1);
		scale.setFromY(1);
		scale.setToX(1.25);
		scale.setToY(1.25);
		scale.setDelay(Duration.millis(1000));
		scale.setAutoReverse(true);
		scale.setCycleCount(Transition.INDEFINITE);
		scale.play();
	}
}