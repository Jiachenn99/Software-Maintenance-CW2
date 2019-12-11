package com.neet.MapViewer.Overview;

import com.neet.DiamondHunter.Main.Game;
import com.neet.DiamondHunter.Manager.Content;
import com.neet.MapViewer.Main.MapMain;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {

	@FXML
	private Label cursorCoordinates;
	@FXML
	private Label statusBar;
	@FXML
	private Label axePosition;
	@FXML
	private Label boatPosition;
	@FXML
	private Button axeButton;
	@FXML
	private Button boatButton;
	
	ImageView axeImage = new ImageView();
	ImageView boatImage = new ImageView();
	private Image axeIcon = SwingFXUtils.toFXImage(Content.ITEMS[1][1], null);
	private Image boatIcon = SwingFXUtils.toFXImage(Content.ITEMS[1][0], null);
	public int flag;
	
	//exporting icons into controller from contents
	private void makeIcons() {
		axeImage.setImage(axeIcon);
		boatImage.setImage(boatIcon);
		axeButton.setGraphic(axeImage);
		boatButton.setGraphic(boatImage);
	}
	
	//places sprite icons into buttons
	public void initialize() {
		makeIcons();
	}
	
	//handles axeButton when clicked
	@FXML
	public void axeButtonPressed() {
		MapMain.tileMapViewer.turningOnCurorColor();
		statusBar.setText("Placing axe...");
		flag = MapMain.tileMapViewer.settingAxe();			//handleSetAxeRequest (delcomment)
		if (flag == 1) {
			bumbleAxe();
		}
		else if (flag == 2) {
			statusBar.setText("Axe coordinates updated.");
			axePosition.setText(
					"Axe: (" + MapMain.tileMapViewer.cursor.cursorX + "," + MapMain.tileMapViewer.cursor.cursorY + ")");
		}
		else if (flag == 0) {
			statusBar.setText("Axe has been placed");
			axePosition.setText(
					"Axe: (" + MapMain.tileMapViewer.cursor.cursorX + "," + MapMain.tileMapViewer.cursor.cursorY + ")");
		}
	}
	
	//handles boatButton when clicked
	@FXML
	public void boatButtonPressed() {
		MapMain.tileMapViewer.turnOnCursorColor();		//turningOnCurorColor (delcomment)
		statusBar.setText("Placing boat...");
		flag = MapMain.tileMapViewer.settingBoat();			//handleSetBoatRequest (delcomment)
		if (flag == 1) {
			bumbleBoat();
		}
		else if (flag == 2) {
			statusBar.setText("Boat coordinates updated.");
			boatPosition.setText(
					"Boat: (" + MapMain.tileMapViewer.cursor.cursorX + "," + MapMain.tileMapViewer.cursor.cursorY + ")");
		}
		else if (flag == 0) {
			statusBar.setText("Boat has been placed.");
			boatPosition.setText(
					"Boat: (" + MapMain.tileMapViewer.cursor.cursorX + "," + MapMain.tileMapViewer.cursor.cursorY + ")");
		}
	}
	
	@FXML
	public void startGameButtonPressed() {
		MapMain.primaryStage.hide();
		Game.main(null);
	}
	
	//each relevant key pressed by user will be reflected upon this method
	@FXML
	private void keyActions(KeyEvent event) {
		if (MapMain.tileMapViewer.cursorColor == false) {
			statusBar.setText("Z to zoom in\n X to zoom out");
		}

		if(event.getCode() == KeyCode.Z) {
			MapMain.tileMapViewer.zoomIn();			//zoomInImage (pls delete dis comment)
	    }
	    else if (event.getCode() == KeyCode.X) {
	    	MapMain.tileMapViewer.zoomOut();		//zoomOutImage (plz delete dis comment)
	    }
	    else if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) {
	    	MapMain.tileMapViewer.cursorUp();
	    	updateCursorCoordinates();
	    }
	    else if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) {
	    	MapMain.tileMapViewer.cursorDown();
	    	updateCursorCoordinates();
	    }
	    else if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
	    	MapMain.tileMapViewer.cursorLeft();
	    	updateCursorCoordinates();
	    }
	    else if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
	    	MapMain.tileMapViewer.cursorRight();
	    	updateCursorCoordinates();
	    }
	    else if (event.getCode() == KeyCode.O) {
	    	MapMain.tileMapViewer.turnCursorColor();		//turningOnCursorColor
	    	statusBar.setText("Placing Axe...");
	    }
	    else if (event.getCode() == KeyCode.P) {
	    	MapMain.tileMapViewer.turnOnCursorColor();
	    	statusBar.setText("Placing Boat...");
	    }
	}
	//updates statusBar and coordinates of tools based on user's actions
	@FXML
	private void updateFactors(KeyEvent event) {
		int flag;
		if (event.getCode() == KeyCode.O) {
			flag = MapMain.tileMapViewer.settingAxe();		//handleSetAxeRequest
			if (flag == 1) {
				bumbleAxe();
			}
			else if (flag == 2) {
				statusBar.setText("Axe coordinates updated.");
				axePosition.setText(
    					"Axe: (" + MapMain.tileMapViewer.cursor.cursorX + "," + MapMain.tileMapViewer.cursor.cursorY + ")");
			}
			else if (flag == 0) {
				statusBar.setText("Axe has been placed.");
    			axePosition.setText(
    					"Axe: (" + MapMain.tileMapViewer.cursor.cursorX + "," + MapMain.tileMapViewer.cursor.cursorY + ")");
			}
		}

		else if (event.getCode() == KeyCode.P) {
			flag = MapMain.tileMapViewer.settingBoat();    //handleSetBoatRequest
			if (flag == 1) {
				bumbleBoat();
			}
			else if (flag == 2) {
				statusBar.setText("Boat coordinates updated.");
				boatPosition.setText(
    					"Boat: (" + MapMain.tileMapViewer.cursor.cursorX + "," + MapMain.tileMapViewer.cursor.cursorY + ")");
			}
			else if (flag == 0) {
				statusBar.setText("Boat has been placed.");
    			boatPosition.setText(
    					"Boat: (" + MapMain.tileMapViewer.cursor.cursorX + "," + MapMain.tileMapViewer.cursor.cursorY + ")");
			}
		}
	}
	//Displays a guide on how to use Map Viewer
	@FXML private void helpInfo() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Instructions");
		alert.setHeaderText("Instructions on how to use Map Viewer");
		alert.setContentText("Click WASD/arrow keys to move cursor.\n"
				+ "Aside from the buttons, you could also use O key to place Axe, P key to place Boat.\n"
        		+ "Click Z/X keys to zoom in and out of map.\n\n"
        		+ "When you're ready, click Start Game!");
		alert.showAndWait();
		alert.setOnCloseRequest(event -> {alert.close();});
	}
	//Alert error upon wrong placement of Axe
	private void bumbleAxe() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Placement error");
		alert.setHeaderText("Placement error!");
		alert.setContentText("Axe cannot be placed there!\n" + "Find somewhere with no obstacles in the way and place the axe!");
		alert.showAndWait();
		alert.setOnCloseRequest(event -> {alert.close();});
	}
	//Alert error upon wrong placement of Boat
	private void bumbleBoat() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Placement error");
		alert.setHeaderText("Placement error!");
		alert.setContentText("Boat cannot be placed there!\n" + "Find somewhere with no obstacles in the way and place the boat!");
		alert.showAndWait();
		alert.setOnCloseRequest(event -> {alert.close();});
	}
	//Updates cursor coordinates every cursor move
	private void updateCursorCoordinates() {				//cursorCols-> cursorY
		cursorCoordinates.setText("(" + MapMain.tileMapViewer.cursor.cursorY + ", " + MapMain.tileMapViewer.cursor.cursorY + ")");
	}
}
