package com.neet.MapViewer.Main;

import javafx.scene.image.Image;

public class Cursors {

    public Image[] cursorsOption;

    public int defaultCursor = 2;

    public int cursorColumns;
    public int cursorRows;

    public Cursors()
    {
        cursorsOption = new Image[3];
        cursorsOption[0] = new Image(Cursors.class.getResourceAsStream("/Sprites/cursor_green.gif"));
        cursorsOption[1] = new Image(Cursors.class.getResourceAsStream("/Sprites/cursor_red.gif"));
        cursorsOption[2] = new Image(Cursors.class.getResourceAsStream("/Sprites/cursor_normal.gif"));
        cursorColumns = 17;
        cursorRows = 17;
    }
}
