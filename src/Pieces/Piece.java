package Pieces;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public abstract class Piece{
    private char symbol;
    File file;
    BufferedImage image;
    public enum Color {white, black}
    Color color;

    Piece(char symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }
    protected abstract void checkMove();
    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }
    public int[] getTileOffset() {  // zwraca przesuniecie narysowanej bierki względem pola szachownicy
        int x = (Game.Tile.dimension - image.getWidth())/2;
        int y = (Game.Tile.dimension - image.getHeight())/2;
        return new int[]{x,y};
    }

    public char getSymbol() {
        return symbol;
    }
}
