package com.neet.MapViewer.Overview;

import com.neet.DiamondHunter.Main.Game;
import com.neet.MapViewer.Main.MapMain;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MainController {
	@FXML
	private Label cursorCoords;
	@FXML
	private Button axeButton;	
	@FXML
	private Button boatButton;
	@FXML
	private Label noticeBar;
	@FXML
	private Label statusBar;
	@FXML
	private Button startGameButton;
	
	@FXML
	private void handleKeyActions (KeyEvent event) {
		if (MapMain.tileMapViewer.cursorColor == false) {
			noticeBar.setText("Click Z, X to zoom in/out");
		}
		else if (event.getCode() == KeyCode.Z) {
			MapMain.tileMapViewer.zoomIn();			//method name change plz
		}
		else if (event.getCode() == KeyCode.X) {
			MapMain.tileMapViewer.zoomOut();		//method name change plz
		}
		
		
	}
	
	
}
