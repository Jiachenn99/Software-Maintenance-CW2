package com.neet.MapViewer.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MapMain extends Application {

    public static Stage primaryStage;

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

    }


}