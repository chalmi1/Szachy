package Pieces;

import Game.Board;
import Game.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Abstrakcyjna klasa reprezentująca bierkę.
 * Implementacjami jej są klasy reprezentujące pionka, wieżę, skoczka, gońca, hetmana i króla.
 */
public abstract class Piece{
    private char symbol;
    /**
     * Obiekt klasy File przechowujący ścieżkę pliku z obrazkiem reprezentującym daną bierkę
     */
    File file;
    BufferedImage image;
    /**
     * Kolor danej bierki
     */
    Tile.ColorEnum color;
    /**
     * Kolor bierki przeciwnika
     */
    Tile.ColorEnum opposingColor;
    /**
     * Flaga sygnalizująca wymóg dodatkowych rzeczy po wykonaniu ruchu.
     * Używana by wykonywać ruchy specjalne takie jak:
     * roszada, promowanie pionka, bicie pionkiem w przelocie (zasada en passant)
     */
    public boolean additional = false;

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

    /**
     * @param start punkt startowy ruchu
     * @param destination punkt końcowy ruchu
     * @param brd szachownica na której ruch jest dokonywany
     * @return true gdy ruch jest dozwolony
     */
    public abstract boolean isLegal(Point start, Point destination, Game.Board brd);

    /**
     * Funkcja ustawiająca flagi kontroli na polach które dana bierka kontroluje ("widzi")
     * @param start punkt startowy
     * @param brd szachownica
     */
    public abstract void updateControlled(Point start, Board brd);

    /**
     * Funkcja wykonująca dodatkowe rzeczy przy ruchach specjalnych (roszada, promocja piona, bicie w przelocie).
     * Wykonywana tylko gdy flaga additional jest true
     * @param destination punkt docelowy ruchu
     * @param brd szachownica
     */
    public abstract void specialMove(Point destination, Board brd);
    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    /**
     * @return przesuniecie narysowanej bierki względem pola szachownicy
     */
    public Point getTileOffset() {
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
