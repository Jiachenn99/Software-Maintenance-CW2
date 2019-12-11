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
    public int numofRows, numofCols, tilesWidth, currentNumCols, currentNumRows;
    public Cursors cursor; // Derived from MyCursor file
    public boolean cursorColor = false;
    public int magnification, offset;
    public int afterMoveSetColumns;
    public int afterMoveSetRows;
    
    public boolean axePlaced = false;
    public boolean boatPlaced = false;

    

    // Private methods
    private Canvas mainCanvas;
    private GraphicsContext graphicsContext;
    private Image mapImage, tileset, items;
    private int map[][], tileType[][];
    private int tileSize = 16;

    private final int BOAT = 0;
    private final int AXE = 1;
    private int boatRow = -1;
	private int boatCol = -1;
	private int axeRow = -1;
	private int axeCol = -1;

   

    public void drawMap(String canvas){

        try{
            // Obtain the data from the map file
            InputStream in = getClass().getResourceAsStream(MAP_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
    
            // Variables for canvas setting
            numofCols = Integer.parseInt(br.readLine());
            numofRows = Integer.parseInt(br.readLine());

            currentNumCols = numofCols;
            currentNumRows = numofRows;
            
            map = new int[numofRows][numofCols];
    
            // Processing the map file
            // Space is the delimiter across sprites/units in the testmap.map file
            String delimiters = " ";
            for(int i = 0; i < numofRows; i++){
                String line = br.readLine();
                String[] tokens = line.split(delimiters);
                for (int col = 0; col < numofCols; col++) {
                    map[numofRows][numofCols] = Integer.parseInt(tokens[col]);
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
		tileType = new int[numofRows][numofCols];
		cursor = new Cursors();

		for(int row = 0; row < numofRows; row++) {
			for(int col = 0; col < numofCols; col++) {
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
			currentNumCols /= 2;
			currentNumRows /= 2;
			setOffset(magnification);

			validCursor();
			updateCurrentCanvas();
		}
    }
    
    public void zoomOut() {   //zoomOut
		if (magnification > 1) {

			magnification /= 2;
			currentNumCols *= 2;
			currentNumRows *= 2;
			setOffset(magnification);

			validCursor();
			updateCurrentCanvas();
		}
    }
    
    private void validCursor() {	
		replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

		if (cursor.cursorRows < offset) {
			cursor.cursorRows = offset;
		}
		else if (cursor.cursorRows > offset + currentNumRows - 1) {
			cursor.cursorRows = offset + currentNumRows - 1;
		}

		if (cursor.cursorColumns < offset) {
			cursor.cursorColumns = offset;
		}
		else if (cursor.cursorColumns > offset + currentNumCols - 1) {
			cursor.cursorColumns = offset + currentNumCols - 1;
		}

		updateItemsDraw();
		drawCursorToMainCanvas();
		mapImage = mainCanvas.snapshot(null, null);
	}

    private void replaceTileInMainCanvasToOriginal(int col, int row) {
		mainCanvas.getGraphicsContext2D().drawImage(
            mapImage,
            col * tileSize,
            row * tileSize,
            tileSize, tileSize,
            col * tileSize,
            row * tileSize,
            tileSize, tileSize);
	}

    private void drawCursorToMainCanvas() {
		mainCanvas.getGraphicsContext2D().drawImage(
				cursor.cursorsOption[cursor.defaultCursor], 0, 0,
				tileSize, tileSize,
				cursor.cursorColumns * tileSize,
				cursor.cursorRows * tileSize,
				tileSize, tileSize);

    private void updateCurrentCanvas() {
        currentCanvas.getGraphicsContext2D().drawImage(
                mapImage,
                afterMoveSetColumns * tileSize, afterMoveSetRows * tileSize,
                currentNumCols * tileSize, currentNumRows * tileSize,
                0, 0, 640, 640);
    }

    private void setOffset(int magnification) {
		if (magnification == 1) {
			offset = 0;
			afterMoveSetColumns = offset;
			afterMoveSetRows = offset;
		}
		else if (magnification == 2){
			offset = 10;
			afterMoveSetColumns = offset;
			afterMoveSetRows = offset;
		}
		else {
			offset = 15;
			afterMoveSetColumns = offset;
			afterMoveSetRows = offset;
		}
    }
    
    private void updateMoveset() {
		if (afterMoveSetColumns > cursor.cursorColumns) {
			afterMoveSetColumns --;
		}
		else if (afterMoveSetRows > cursor.cursorRows) {
			afterMoveSetRows --;
		}
		else if (afterMoveSetColumns + currentNumCols - 1 < cursor.cursorColumns) {
			afterMoveSetColumns ++;
		}
		else if (afterMoveSetRows + currentNumRows - 1 < cursor.cursorRows) {
			afterMoveSetRows ++;
		}

    }
    
    private void changeCursorColor() {
		if (cursorColor == true) {
			cursor.defaultCursor = tileType[cursor.cursorRows][cursor.cursorColumns];
		}
		else {
			cursor.defaultCursor = 2;
		}
	}

    public void turningOnCursorColor() {		
		cursorColor = true;

		changeCursorColor();

		replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

		updateItemsDraw();
		drawCursorToMainCanvas();
		mapImage = mainCanvas.snapshot(null, null);
		updateCurrentCanvas();

    }
    
    // Updating items when they are placed
    private void updateItemsDraw() {
		if (axePlaced) {
			mainCanvas.getGraphicsContext2D().drawImage(
					items,
					AXE  * tileSize, tileSize, tileSize, tileSize,
					axeCol * tileSize,
					axeRow * tileSize,
					tileSize, tileSize);
		}
		if (boatPlaced) {
			mainCanvas.getGraphicsContext2D().drawImage(
					items,
					BOAT  * tileSize, tileSize, tileSize, tileSize,
					boatCol * tileSize,
					boatRow * tileSize,
					tileSize, tileSize);
		}
	}


    // Cursor movement functions
    public void cursorUp() {
		if (cursor.cursorRows > 0) {
			replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

			cursor.cursorRows --;
			changeCursorColor();

			updateItemsDraw();
			drawCursorToMainCanvas();

			mapImage = mainCanvas.snapshot(null, null);

			updateMoveset();
			updateCurrentCanvas();
		}
	}
	/**
	 * The method is used to move cursor down.
	 */
	public void cursorDown() {
		if (cursor.cursorRows < numofRows - 1 ) {
			replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

			cursor.cursorRows ++;
			changeCursorColor();

			updateItemsDraw();
			drawCursorToMainCanvas();

			mapImage = mainCanvas.snapshot(null, null);

			updateMoveset();
			updateCurrentCanvas();
		}
	}
	/**
	 * The method is used to move cursor left.
	 */
	public void cursorLeft() {
		if (cursor.cursorColumns > 0) {
			replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

			cursor.cursorColumns --;
			changeCursorColor();

			updateItemsDraw();
			drawCursorToMainCanvas();

			mapImage = mainCanvas.snapshot(null, null);

			updateMoveset();
			updateCurrentCanvas();
		}
	}
	/**
	 * The method is used to move cursor right.
	 */
	public void cursorRight() {
		if (cursor.cursorColumns < numofCols - 1 ) {
			replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

			cursor.cursorColumns ++;
			changeCursorColor();

			updateItemsDraw();
			drawCursorToMainCanvas();

			mapImage = mainCanvas.snapshot(null, null);

			updateMoveset();
			updateCurrentCanvas();
		}
	}
                                     
}


    