package com.neet.MapViewer.Main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapDraw{
    
    public static String MAP_PATH = "/Maps/testmap.map";

    private Canvas wholeCanvas, currentCanvas;
    private GraphicsContext graphicsContext;
    private Image image, tilesets;
    private int TotalMapHeight, TotalMapWidth, tilesWidth;
    private int map[][];
    private int tileSize = 16;

    public void drawMap(Canvas canvas){

        try{
            // Obtain the data from the map file
            InputStream in = getClass().getResourceAsStream(MAP_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
    
            // Variables for canvas setting
            TotalMapWidth = Integer.parseInt(br.readLine());
            TotalMapHeight = Integer.parseInt(br.readLine());
            map = new int[TotalMapHeight][TotalMapWidth];
    
            // Processing the map file
            // Space is the delimiter across sprites/units in the testmap.map file
            String delimiters = " ";
            for(int i = 0; i < TotalMapHeight; i++){
                String line = br.readLine();
                String[] tokens = line.split(delimiters);
                for (int col = 0; col < TotalMapWidth; col++) {
                    map[TotalMapHeight][TotalMapWidth] = Integer.parseInt(tokens[col]);
                }
            }
            
        } catch(Exception e){
            e.printStackTrace();
        }
        
        }

    public void loadImages(String tilesetImage, String itemImage){
        try{
            Image tileset = new Image(MapDraw.class.getResourceAsStream(tilesetImage));
            Image items = new Image(MapDraw.class.getResourceAsStream(itemImage));
            tilesWidth = (int)tileset.getWidth() / tileSize; 
            
        } catch (Exception e){

        }
        
    }
                                             
}
    