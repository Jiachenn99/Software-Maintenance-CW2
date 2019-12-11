package com.neet.MapViewer.Main;

import javafx.scene.image.Image;

public class MyCursor {

    public Image[] cursorsOption;

    public int defaultCursor = 2;

    public int cursorColumns;
    public int cursorRows;

    public MyCursor()
    {
        cursorsOption = new Image[3];
        cursorsOption[0] = new Image(MyCursor.class.getResourceAsStream("/Sprites/cursor_green.gif"));
        cursorsOption[1] = new Image(MyCursor.class.getResourceAsStream("/Sprites/cursor_red.gif"));
        cursorsOption[2] = new Image(MyCursor.class.getResourceAsStream("/Sprites/cursor_normal.gif"));
        cursorColumns = 17;
        cursorRows = 17;
    }
}
