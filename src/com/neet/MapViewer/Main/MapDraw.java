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

    // Public methods
    public Canvas currentCanvas;
    public int TotalMapHeight, TotalMapWidth, tilesWidth;
    public Cursors cursor; // Derived from MyCursor file
    public boolean cursorColor = false;
    public int magnification;

    // Private methods
    private Canvas mainCanvas;
    private GraphicsContext graphicsContext;
    private Image mapImage, tileset, items;
    private int map[][], tileType[][];
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
            tileset = new Image(MapDraw.class.getResourceAsStream(tilesetImage));
            items = new Image(MapDraw.class.getResourceAsStream(itemImage));
            tilesWidth = (int)tileset.getWidth() / tileSize; 
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }

    public void initialiseCanvas() {
		mainCanvas = new Canvas(640,640);
		currentCanvas = new Canvas(640, 640);
		tileType = new int[TotalMapHeight][TotalMapWidth];
		cursor = new Cursors();

		for(int row = 0; row < TotalMapHeight; row++) {
			for(int col = 0; col < TotalMapWidth; col++) {
				if(map[row][col] == 0) continue;

				int rc = map[row][col];

				int r = rc / tilesWidth;
				int c = rc % tilesWidth;

				if (r == 0) {
					mainCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, 0, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					currentCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, 0, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					tileType[row][col] = 0;
				}
				else {
					mainCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, tileSize, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					currentCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, tileSize, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					tileType[row][col] = 1;
				}

			}
		}
		mapImage = mainCanvas.snapshot(null, null);
		drawCursorToMainCanvas();
		currentCanvas.getGraphicsContext2D().drawImage(
				cursor.cursorsOption[cursor.defaultCursor], 0, 0, tileSize, tileSize,
				 cursor.cursorColumns * tileSize, cursor.cursorRows * tileSize,
				 tileSize, tileSize);
		mapImage = mainCanvas.snapshot(null, null);
	}
    

    public void zoomIn() {		//zoomIn
		if (magnification < 4) {

			magnification *= 2;
			TotalMapWidth /= 2;
			TotalMapHeight /= 2;
			setOffset(magnification);

			validCursor();
			updateCurrentCanvas();
		}
    }
    
    public void zoomOut() {   //zoomOut
		if (magnification > 1) {

			magnification /= 2;
			TotalMapWidth *= 2;
			TotalMapHeight *= 2;
			setOffset(magnification);

			validCursor();
			updateCurrentCanvas();
		}
    }
    




                                             
}
    