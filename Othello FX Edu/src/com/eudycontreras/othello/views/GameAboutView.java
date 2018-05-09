package com.eudycontreras.othello.views;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.eudycontreras.othello.application.OthelloGameView;
import com.eudycontreras.othello.application.OthelloResources;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
public class GameAboutView extends Group {

	private static final String LAYOUT_STYLE = "game-end-view-style";

	private VBox layout = new VBox(12);

	private BorderPane boxLayout = new BorderPane();

	private StackPane centerBoard = new StackPane();

	private StackPane pictureLayout = new StackPane();

	private StackPane textLayout = new StackPane();

	private Button exitButton = new Button("Exit");

	private ImageView profilePic = new ImageView();

	private Runnable continueAction = null;
	private Runnable hideAction = null;

	private boolean showing = false;

	public GameAboutView() {

		HBox layout = new HBox(12);

		layout.getChildren().add(boxLayout);
		layout.getChildren().add(textLayout);

		this.layout.getChildren().add(layout);

		pictureLayout.getChildren().add(centerBoard);
		pictureLayout.getChildren().add(profilePic);
		
		profilePic.setImage(OthelloResources.CREATOR_PROFILE);
		profilePic.setPreserveRatio(true);
		profilePic.setFitWidth(120);
		
		
		exitButton.setAlignment(Pos.CENTER);
		this.layout.getChildren().add(exitButton);

		this.layout.setPadding(new Insets(30));
		this.layout.setId(LAYOUT_STYLE);

		this.layout.setAlignment(Pos.CENTER);

		getChildren().addAll(this.layout);
		setMouseTransparent(true);
		setVisible(false);
	}
	
	public VBox getFields(){
		VBox layout = new VBox(2);
		
		Field nameField = new Field("Name:", "Eudy Contreras");
		Field occupationField = new Field("Email:", "Econtreras12@live.com");
		Link linkedin = new Link("LinkedIn","https://se.linkedin.com/in/eudy-contreras-42a0a8a2");
		Link github = new Link("Github","https://github.com/EudyContreras");
		
		layout.getChildren().add(nameField);
		layout.getChildren().add(occupationField);
		layout.getChildren().add(linkedin);
		layout.getChildren().add(github);
		
		return layout;
	}

	public void initialize(double width, double height) {
		this.initialize(null, width, height);
	}

	public void initialize(OthelloGameView gameView, double width, double height) {

		boxLayout.setTop(pictureLayout);

		centerBoard.setPrefWidth(width);
		centerBoard.setPrefHeight(height * 1.2);
		centerBoard.setId("game-end-view-center-style");

		textLayout.setPrefWidth(width);
		textLayout.setPrefHeight(height);
		textLayout.setId("game-end-view-center-style");
		textLayout.getChildren().add(getFields());

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.RED);
		dropShadow.setRadius(15);
		dropShadow.setSpread(0.25);
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);

		exitButton.setPrefWidth(130);
		exitButton.setId("game-menu-end-dialog-button");
		exitButton.setOnAction(e -> {
			if (continueAction != null) {
				continueAction.run();
			}
			animateHide(400);
		});
	}

	public void setContinueAction(Runnable action) {
		this.continueAction = action;
	}
	
	public void setHideAction(Runnable action){
		this.hideAction = action;
	}

	public void showAboutView() {

		setVisible(true);
		setMouseTransparent(false);

		animateShow(400);
	}

	public void setShowing(boolean showing) {
		this.showing = showing;
	}

	public boolean isShowing() {
		return showing;
	}

	public void hideEndGameView() {
		animateHide(200);
	}

	private void animateShow(int miliseconds) {

		if(!UserSettings.USE_ANIMATION){
			setVisible(true);
			setMouseTransparent(false);
			this.setScaleX(1);
			this.setScaleY(1);
			this.setOpacity(1);
		
			return;
		}
		
		ScaleTransition scaleTransition = new ScaleTransition();

		FadeTransition fadeTransition = new FadeTransition();

		scaleTransition.setNode(this);
		fadeTransition.setNode(this);

		fadeTransition.setDuration(Duration.millis(miliseconds));

		fadeTransition.setFromValue(0);

		fadeTransition.setToValue(1);

		fadeTransition.play();

		scaleTransition.setDuration(Duration.millis(miliseconds));
		scaleTransition.setInterpolator(Interpolator.SPLINE(1, 0, 0.2, 1));

		scaleTransition.setFromX(0.3);
		scaleTransition.setFromY(0.3);

		scaleTransition.setToX(1);
		scaleTransition.setToY(1);

		scaleTransition.play();
	}

	private void animateHide(int miliseconds) {
		if(!UserSettings.USE_ANIMATION){
			if(hideAction != null){
				hideAction.run();
			}
			this.setScaleX(0);
			this.setScaleY(0);
			this.setOpacity(0);
			setMouseTransparent(true);
			setVisible(false);
			setShowing(false);
			return;
		}
		ScaleTransition scaleTransition = new ScaleTransition();

		FadeTransition fadeTransition = new FadeTransition();

		scaleTransition.setNode(this);
		fadeTransition.setNode(this);

		fadeTransition.setDuration(Duration.millis(miliseconds / 2));

		fadeTransition.setFromValue(1);

		fadeTransition.setToValue(0);

		fadeTransition.play();

		scaleTransition.setDuration(Duration.millis(miliseconds));
		scaleTransition.setInterpolator(Interpolator.SPLINE(1, 0, 0.2, 1));

		scaleTransition.setFromX(1);
		scaleTransition.setFromY(1);

		scaleTransition.setToX(0);
		scaleTransition.setToY(0);

		scaleTransition.setOnFinished(e -> {
			if(hideAction != null){
				hideAction.run();
			}
			setMouseTransparent(true);
			setVisible(false);
			setShowing(false);
		});
		scaleTransition.play();

	}

	public class Field extends HBox{
		
		private String fieldLabel = "";
		private String fieldValue = "";
		
		private Text fieldLabelView = new Text();
		private Text fieldValueView = new Text();
		

		public Field(String fieldLabel, String fieldValue) {
			super();
			this.fieldLabel = fieldLabel;
			this.fieldValue = fieldValue;
			this.fieldLabelView.setText(fieldLabel);
			this.fieldValueView.setText(fieldValue);
			this.getChildren().addAll(fieldLabelView,fieldValueView);
			this.setPadding(new Insets(3,12,3,12));
			this.setAlignment(Pos.CENTER_LEFT);
			this.setSpacing(10);
			
			setUpTexts();
		}
		
		private void setUpTexts(){
			this.fieldLabelView.setFill(Color.WHITE);
			this.fieldValueView.setFill(Color.WHITE);
			
			this.fieldLabelView.setFont( Font.font(null, FontWeight.BOLD, 15));
			this.fieldValueView.setFont( Font.font(null, FontWeight.BOLD, 13));
		}

		public String getFieldLabel() {
			return fieldLabel;
		}

		public void setFieldLabel(String fieldLabel) {
			this.fieldLabel = fieldLabel;
		}

		public String getFieldValue() {
			return fieldValue;
		}

		public void setFieldValue(String fieldValue) {
			this.fieldValue = fieldValue;
		}
	}
	
	public class Link extends HBox{
		
		private String link = "";
		private String fieldValue = "";
		
		private Text fieldValueView = new Text();
		
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        
		public Link(String fieldValue, String link) {
			super();
			this.link = link;
			this.fieldValue = fieldValue;
			this.fieldValueView.setText(fieldValue);
			
			this.getChildren().addAll(fieldValueView);
			this.setPadding(new Insets(3,12,3,12));
			this.setAlignment(Pos.CENTER_LEFT);
			this.setSpacing(10);
			
			setUpTexts();
		}
		
		private void setUpTexts(){
		
			this.fieldValueView.setFont(Font.font(null, FontWeight.BOLD, 13));
			this.fieldValueView.setFill(Color.DODGERBLUE.brighter());
			this.fieldValueView.setUnderline(true);
			this.fieldValueView.setOnMouseClicked((e)->{
				try {
            		URI uri = new URI(link);
					java.awt.Desktop.getDesktop().browse(uri);
				} catch (IOException | URISyntaxException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			});
		 
		}


		public String getFieldValue() {
			return fieldValue;
		}

		public void setFieldValue(String fieldValue) {
			this.fieldValue = fieldValue;
		}
	}
}