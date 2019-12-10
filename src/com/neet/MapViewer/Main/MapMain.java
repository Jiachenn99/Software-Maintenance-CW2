package com.neet.MapViewer.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;


public class MapMain extends Application {

    public static Stage primaryStage;

    public static TileMapViewer tileMapViewer;

    public TilePane tileOverview;

    @Override
    public void start(Stage stage) throws Exception {
        MapMain.primaryStage = primaryStage;
        MapMain.primaryStage.setTitle("Map Viewer");

    }

    public void setRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("Layout.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void setMapViewer() {
        tileMapViewer = new TileMapViewer();
        tileMapViewer.loadMapFile("/Maps/testmap.map");
        tileMapViewer.loadImagesFiles("/Tilesets/testtileset.gif", "/Sprites/items.gif");
        FXMLLoader loader = new FXMLLoader();
        tileOverview = loader.load(getClass().getResource("MapOverview.fxml"));

        tileOverview.setPrefColumns(tileMapViewer.numCols);
        tileOverview.setPrefRows(tileMapViewer.numRows);
        tileMapViewer.initialiseCanvas();
        tileOverview.getChildren().add(tileMapViewer.currentCanvas);

        root.setCenter(tileOverview);
    }


}