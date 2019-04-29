package Game;

import Pieces.Piece;

import java.awt.*;

public class Tile {

    private String ChCoords;  // koordynaty szachowe np a5, b3 itd.
    private Point IntCoords = new Point();    // koordynaty tablicowe
    public final static int dimension = 75;   // wymiar boku kazdego pola
    private static Point boardOffset = new Point();
    public enum ColorEnum {black, white, blackSelected, whiteSelected}
    private ColorEnum color;
    private Color colorRGB;
    private boolean occupied = false;
    private Pieces.Piece piece = null;

    Tile(String coords, ColorEnum color) {
        changeColor(color);
        ChCoords = coords;
        convertPosToIndexed();
        boardOffset.x = 0;
        boardOffset.y = 0;
    }
    public boolean isOccupied() { return occupied; }

    void draw(Graphics g) {
        g.setColor(colorRGB);
        int x = IntCoords.x*dimension+boardOffset.x;
        int y = IntCoords.y*dimension+boardOffset.y;
        g.fillRect(x, y, dimension, dimension);

        if (piece != null) {
            Point offset = piece.getTileOffset();
            piece.draw(g, x + offset.x, y + offset.y);
        }
    }

    private void convertPosToIndexed() {
        char chx = ChCoords.charAt(0);
        int x = (int)chx - (int)'a';
        char chy = ChCoords.charAt(1);
        int y = Integer.parseInt(""+chy);
        y = Math.abs(8-y);
        IntCoords.x = x;
        IntCoords.y = y;
    }

    private void changeColor(ColorEnum tileColorEnum) {
        color = tileColorEnum;
        switch (tileColorEnum) {
            case black:
                colorRGB = new Color(156, 97, 41);
                break;
            case white:
                colorRGB = new Color(255, 209, 169);
                break;
            case blackSelected:
                colorRGB = new Color(156, 156, 0);
                break;
            case whiteSelected:
                colorRGB = new Color(254, 255, 0);
                break;
        }

    }

    void placePiece(Pieces.Piece piece) {
        this.piece = piece;
        occupied = true;
    }

    void removePiece() {
        this.piece = null;
        occupied = false;
    }

    void click() {
        switch (color)
        {
            case black:
                changeColor(ColorEnum.blackSelected);
                break;
            case white:
                changeColor(ColorEnum.whiteSelected);
                break;
            case blackSelected:
                changeColor(ColorEnum.black);
                break;
            case whiteSelected:
                changeColor(ColorEnum.white);
                break;
        }
    }

    boolean firstClick(Game game) {
        if (game.getTurn()==Player.Color.white)
        {
            if (piece != null &&
                    piece.getColor() == Piece.Color.white)
            {
                click();
                return true;
            }
            else return false;
        }
        else if (game.getTurn()==Player.Color.black)
        {
            if (piece != null &&
                    piece.getColor() == Piece.Color.black)
            {
                click();
                return true;
            }
            else return false;
        }
        return false;
    }

    boolean secondClick(Piece grabbedPiece, Point beginning, Board brd) {
        if (grabbedPiece.isLegal(beginning, IntCoords, brd))
        {
            click();
            return true;
        }
        else
            return false;
    }

    public Piece getPiece() {
        return piece;
    }

    String getChCoords() {
        return ChCoords;
    }

    public Point getIntCoords() {
        return IntCoords;
    }

}
