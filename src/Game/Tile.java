package Game;

import java.awt.*;

public class Tile {

    String ChCoords;  // koordynaty szachowe np a5, b3 itd.
    private int[] IntCoords = new int[2];    // koordynaty tablicowe
    public final static int dimension = 75;   // wymiar boku kazdego pola
    private static int[] boardOffset = new int[2];
    public enum ColorEnum {black, white}
    private ColorEnum tileColorEnum;
    private Color tileColor;
    private boolean occupied = false;
    Pieces.Piece piece = null;

    Tile(String coords, ColorEnum color) {
        tileColorEnum = color;
        convertEnumToColor();
        ChCoords = coords;
        convertPosToIndexed();
        boardOffset[0] = 0;
        boardOffset[1] = 0;
    }
    public boolean isOccupied() { return occupied; }

    void draw(Graphics g) {
        g.setColor(tileColor);
        int x = IntCoords[0]*dimension+boardOffset[0];
        int y = IntCoords[1]*dimension+boardOffset[1];
        g.fillRect(x, y, dimension, dimension);

        if (piece != null) {
            int[] offset = piece.getTileOffset();
            piece.draw(g, x + offset[0], y + offset[1]);
        }
    }

    private void convertPosToIndexed() {
        char chx = ChCoords.charAt(0);
        int x = (int)chx - (int)'a';
        char chy = ChCoords.charAt(1);
        int y = Integer.parseInt(""+chy);
        y = Math.abs(8-y);
        IntCoords[0] = x;
        IntCoords[1] = y;
    }

    private void convertEnumToColor() {
        if (tileColorEnum == ColorEnum.black)
            tileColor = new Color(156, 97, 41);
        else
            tileColor = new Color(255, 209, 169);
    }

    void placePiece(Pieces.Piece piece) {
        this.piece = piece;
    }
}
