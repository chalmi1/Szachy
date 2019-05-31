package Pieces;

import Game.Board;
import Game.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public abstract class Piece{
    private char symbol;
    File file;
    BufferedImage image;
    Tile.ColorEnum color;
    Tile.ColorEnum opposingColor;

    Piece(char symbol, Tile.ColorEnum color) {
        this.symbol = symbol;
        this.color = color;
        if (color == Tile.ColorEnum.black)
            this.symbol += 32;
        if (color == Tile.ColorEnum.black)
            opposingColor = Tile.ColorEnum.white;
        if (color == Tile.ColorEnum.white)
            opposingColor = Tile.ColorEnum.black;
    }
    public abstract boolean isLegal(Point start, Point destination, Game.Board brd);
    public abstract void updateControlled(Point start, Board brd);
    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }
    public Point getTileOffset() {  // zwraca przesuniecie narysowanej bierki względem pola szachownicy
        int x = (Game.Tile.dimension - image.getWidth())/2;
        int y = (Game.Tile.dimension - image.getHeight())/2;
        return new Point(x,y);
    }

    public char getSymbol() {
        return symbol;
    }

    public Tile.ColorEnum getColor() {
        return color;
    }



}
