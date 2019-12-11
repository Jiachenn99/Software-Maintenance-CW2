package com.neet.MapViewer.Main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMapViewer{

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


    /**
     * The function produces a two-dimensional array of row x col
     * after reading in the contents of the map file.
     * 
     * @param MAP_PATH A file that contains information of the map (coordinates)
     * @return NULL
     */
    
    public void drawMap(String MAP_PATH){

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

    /**
     * This method loads the images from the "Resource" folder
     * 
     * @param tilesetImage File that contains image of every tile
     * @param itemImage File that contains image of the axe and boat (referred to as items)
     * @return Nothing
     */

    public void loadImages(String tilesetImage, String itemImage){
        try{
            tileset = new Image(MapDraw.class.getResourceAsStream(tilesetImage));
            items = new Image(MapDraw.class.getResourceAsStream(itemImage));
            tilesWidth = (int)tileset.getWidth() / tileSize; 
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }

    /**
     * This method takes a snapshot (sort of saving state) of 
     * the drawn canvas for use
     * 
     * @return Nothing
     * 
     */

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
    

    /**
     * This method allows the user to zoom in onto the canvas.
     * The method updates the canvas everytime a zoom operation is performed.
     * 
     * @return Nothing
     */

    public void zoomIn() {		//zoomIn
		if (magnification < 4) {

			magnification *= 2;
			currentNumCols /= 2;
			currentNumRows /= 2;
			setOffset(magnification);

			correctCursor();
			updateCurrentCanvas();
		}
    }
    
     /**
     * This method allows the user to zoom out onto the canvas.
     * The method updates the canvas everytime a zoom operation is performed.
     * 
     * @return Nothing
     */

    public void zoomOut() {   //zoomOut
		if (magnification > 1) {

			magnification /= 2;
			currentNumCols *= 2;
			currentNumRows *= 2;
			setOffset(magnification);

			correctCursor();
			updateCurrentCanvas();
		}
    }
    
     /**
     * This method always keeps the cursor in view of the user.
     * In the event a zoom is performed and cursor is out of bounds,
     * this method will reposition the canvas.
     * 
     * @return Nothing
     */

    private void correctCursor() {	
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

    /**
     * This method is used to delete cursor from main canvas.
     * The action above is acheived by redrawing the tile where the cursor is on.
     * 
     * @param col Current column cursor is on
     * @param row Current row cursor is on
     * @return Nothing
     */
    
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

    /**
     * This method is used to draw the cursor to the main canvas.
     * 
     * @return Nothing
     */

    private void drawCursorToMainCanvas() {
		mainCanvas.getGraphicsContext2D().drawImage(
				cursor.cursorsOption[cursor.defaultCursor], 0, 0,
				tileSize, tileSize,
				cursor.cursorColumns * tileSize,
				cursor.cursorRows * tileSize,
                tileSize, tileSize);
    }


    /**
     * This method updates the current canvas on key press / each move
     * 
     * @return Nothing
     */
    private void updateCurrentCanvas() {
        currentCanvas.getGraphicsContext2D().drawImage(
                mapImage,
                afterMoveSetColumns * tileSize, afterMoveSetRows * tileSize,
                currentNumCols * tileSize, currentNumRows * tileSize,
                0, 0, 640, 640);
    }

    /**
     * This method sets the offset when the map is zoomed in.
     * To adjust for proper viewing 
     * 
     * @param magnification The zoom factor of the map 
     */

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
    
    /**
     * This method updates the boundaries of the screen when the user is zoomed in.
     * If the user is at the boundary of the screen, the next movement in the direction
     * of the boundary will move the screen accordingly.
     * 
     * @return Nothing
     */

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
    
    /**
     * This method changes the cursor color depending on the situation
     * - Whether it is a valid placement of object or not
     * 
     * @return Nothing
     */

    private void changeCursorColor() {
		if (cursorColor == true) {
			cursor.defaultCursor = tileType[cursor.cursorRows][cursor.cursorColumns];
		}
		else {
			cursor.defaultCursor = 2;
		}
	}

    /**
     * This method just changes the cursor color when user selects axe / boat
     * 
     * @return Nothing
     */

    public void turningOnCursorColor() {		
		cursorColor = true;

		changeCursorColor();

		replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

		updateItemsDraw();
		drawCursorToMainCanvas();
		mapImage = mainCanvas.snapshot(null, null);
		updateCurrentCanvas();

    }
    
    /**
     * This method draws the placed items in the event the map changes (by zoom)
     * 
     * @return Nothing
     */

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


    // Functions below are related to moving cursor in different directions

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
    
    /**
     * The method handles the result when user places the axe on a tile.
     * 
     * @return Integer that corresponds to a placement state (invalid or valid position)
     */

    public int handleSetAxe() {
		int handleType;
		cursorColor = false;
		changeCursorColor();

		replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

		if (tileType[cursor.cursorRows][cursor.cursorColumns] == 1) {
			handleType = 1;
		}
		else {
			if (axePlaced) {
				replaceTileInMainCanvasToOriginal(axeCol, axeRow);
				
				tileType[axeRow][axeCol] = 0;
				tileType[cursor.cursorRows][cursor.cursorColumns] = 1;
				
				handleType = 2;
			}
			else {
				handleType = 0;
			}
			
    		axePlaced = true;
	    	tileType[cursor.cursorRows][cursor.cursorColumns] = 1;

	    	axeRow = cursor.cursorRows;
	    	axeCol = cursor.cursorColumns;
		}

		updateItemsDraw();
    	drawCursorToMainCanvas();
		
    	mapImage = mainCanvas.snapshot(null, null);
    	updateCurrentCanvas();

    	return handleType;
    }
    
    /**
     * The method handles the result when user places the boat on a tile.
     * 
     * @return Integer that corresponds to a placement state (invalid or valid position)
     */

    public int handleSetBoat() {
		int handleType;
		cursorColor = false;
		changeCursorColor();

		replaceTileInMainCanvasToOriginal(cursor.cursorColumns, cursor.cursorRows);

		// return type: Position invalid
		if (tileType[cursor.cursorRows][cursor.cursorColumns] == 1) {
			handleType = 1;
		}
		// return type: Boat put successful 
		else {
			if (boatPlaced) {
				replaceTileInMainCanvasToOriginal(boatCol, boatRow);
				
				tileType[boatRow][boatCol] = 0;
				tileType[cursor.cursorRows][cursor.cursorColumns] = 1;
				
				handleType = 2;
			}
			else {
				handleType = 0;
			}

    		boatPlaced = true;   
	    	tileType[cursor.cursorRows][cursor.cursorColumns]= 1;
	    	boatRow = cursor.cursorRows;
	    	boatCol = cursor.cursorColumns;
	    	
		}
		updateItemsDraw();
		drawCursorToMainCanvas();
    	mapImage = mainCanvas.snapshot(null, null);
    	updateCurrentCanvas();
    	return handleType;
	}

}


    