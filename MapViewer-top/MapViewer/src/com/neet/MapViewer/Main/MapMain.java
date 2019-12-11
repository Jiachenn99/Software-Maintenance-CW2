package com.neet.MapViewer.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;


public class MapMain extends Application {

    public static Stage primaryStage;

    public static TileMapViewer tileMapViewer;

    public TilePane tileOverview;
    
    public BorderPane root;
    
    public static boolean viewerLaunch;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MapMain.primaryStage = primaryStage;
        MapMain.primaryStage.setTitle("Map Viewer");
        
        setRootLayout();
        setMapViewer();
        viewerLaunch = true;

    }

    public void setRootLayout() {
    	try {
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MapMain.class.getResource("/com/neet/MapViewer/View/RootLayout.fxml"));
            root = (BorderPane) loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
    	}
        catch(Exception e) {
        	e.printStackTrace();
        }
        
    }

    public void setMapViewer() {
        tileMapViewer = new TileMapViewer();
        tileMapViewer.drawMap("/Maps/testmap.map");
        tileMapViewer.loadImages("/Tilesets/testtileset.gif", "/Sprites/items.gif");
        
        try {
        	FXMLLoader loader = new FXMLLoader();
//          tileOverview = (TilePane)loader.load(MapMain.class.getResource("/com/neet/MapViewer/View/MapOverview.fxml"));
        	loader.setLocation(MapMain.class.getResource("/com/neet/MapViewer/View/MapOverview.fxml"));
    		
			tileOverview = (TilePane) loader.load();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
       
        tileOverview.setPrefColumns(tileMapViewer.numofCols);
        tileOverview.setPrefRows(tileMapViewer.numofRows);
        tileMapViewer.initialiseCanvas();
        tileOverview.getChildren().add(tileMapViewer.currentCanvas);

        root.setCenter(tileOverview);
    }

    public static void main(String[] args) {
    	launch(args);
    }

}