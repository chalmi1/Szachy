package Game;

import Pieces.Piece;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Klasa reprezentująca pojedyncze pole na szachownicy
 */
public class Tile {

    /**
     * koordynaty szachowe np a5, b3 itd.
     */
    private String ChCoords;  //
    /**
     * koordynaty tablicowe
     */
    private Point IntCoords = new Point();
    public final static int dimension = 75;
    private static Point boardOffset = new Point();
    private static Point fontOffsetV = new Point();
    private static Point fontOffsetH = new Point();

    public enum ColorEnum {black, white, blackSelected, whiteSelected}
    private ColorEnum color;
    private Color colorRGB;
    private boolean occupied = false;
    private boolean blackControlled = false;
    private boolean whiteControlled = false;
    private Pieces.Piece piece = null;
    private boolean dotted = false;
    private File dotfile = new File("src/img/kropka.png");
    private static Image dotimage;

    Tile(String coords, ColorEnum color) {
        changeColor(color);
        ChCoords = coords;
        convertPosToIndexed();
        boardOffset.x = 0;
        boardOffset.y = 0;
        fontOffsetV.x = 2;
        fontOffsetV.y = 15;
        fontOffsetH.x = dimension - 10;
        fontOffsetH.y = dimension - 5;
        try {
            dotimage = ImageIO.read(dotfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isOccupied() { return occupied; }

    void draw(Graphics g) {
        g.setColor(colorRGB);
        int x = IntCoords.x*dimension+boardOffset.x;
        int y = IntCoords.y*dimension+boardOffset.y;
        g.fillRect(x, y, dimension, dimension);

        Color fontColor;
        switch (color) {
            case black:
            case blackSelected:
                fontColor = new Color(255, 209, 169);
                break;
            default:
                fontColor = new Color(156, 97, 41);
                break;
        }

        if (ChCoords.substring(0,1).equals("a")) {
            g.setColor(fontColor);
            g.drawString(ChCoords.substring(1,2), x+fontOffsetV.x, y+fontOffsetV.y);
        }
        if (ChCoords.substring(1,2).equals("1")) {
            g.setColor(fontColor);
            g.drawString(ChCoords.substring(0,1), x+fontOffsetH.x, y+fontOffsetH.y);
        }

        if (piece != null) {
            Point offset = piece.getTileOffset();
            piece.draw(g, x + offset.x, y + offset.y);
        }

        if (dotted) {
            g.drawImage(dotimage, x+7,y+7, null);
        }
    }

    /**
     * Funkcja konwertująca koordynaty szachowe (a5, b6 itp.) na tablicowe
     */
    private void convertPosToIndexed() {
        char chx = ChCoords.charAt(0);
        int x = (int)chx - (int)'a';
        char chy = ChCoords.charAt(1);
        int y = Integer.parseInt(""+chy);
        y = Math.abs(8-y);
        IntCoords.x = x;
        IntCoords.y = y;
    }

    /**
     * @param tileColorEnum kolor płytki (czarny, biały, czarny zaznaczony, biały zaznaczony)
     */
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

    /**
     * funkcja umieszczająca bierkę na tym polu
     * @param piece bierka
     */
    public void placePiece(Pieces.Piece piece) {
        this.piece = piece;
        occupied = true;
    }

    /**
     * funkcja usuwająca bierkę z tego pola
     */
    public void removePiece() {
        this.piece = null;
        occupied = false;
    }

    /**
     * funkcja podświetlająca/gasząca pole
     */
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

    /**
     * Funkcja obsługująca pierwsze kliknięcie w turze. Musi być to kliknięcie gracza w bierkę własnego koloru.
     * @param game gra która się toczy na tej szachownicy
     * @return true gdy kliknięto w bierkę własnego koloru
     */
    boolean firstClick(Game game) {
        if (game.getTurn().getColor()==Player.Color.white)
        {
            if (piece != null &&
                    piece.getColor() == ColorEnum.white)
            {
                click();
                return true;
            }
            else return false;
        }
        else if (game.getTurn().getColor()==Player.Color.black)
        {
            if (piece != null &&
                    piece.getColor() == ColorEnum.black)
            {
                click();
                return true;
            }
            else return false;
        }
        return false;
    }

    /**
     * Funkcja obsługująca drugie kliknięcie w turze. Sprawdzana jest poprawność wykonanego ruchu.
     * @param grabbedPiece bierka chwycona w pierwszym kliknięciu
     * @param beginning koordynaty tablicowe pierwszego kliknięcia
     * @param brd plansza na której toczy się rozgrywka
     * @return true gdy ruch jest prawidłowy
     */
    boolean secondClick(Piece grabbedPiece, Point beginning, Board brd) {
        grabbedPiece.additional = false;
        if (grabbedPiece.isLegal(beginning, IntCoords, brd)) {
            if (grabbedPiece.getSymbol() == 'K' || grabbedPiece.getSymbol() == 'k') {
                Pieces.King king = (Pieces.King)grabbedPiece;
                king.moved();
            }
            if (grabbedPiece.getSymbol() == 'W' || grabbedPiece.getSymbol() == 'w') {
                Pieces.Rook rook = (Pieces.Rook)grabbedPiece;
                rook.moved();
            }
            if (grabbedPiece.additional)
                grabbedPiece.specialMove(IntCoords, brd);
            return true;
        }
        return false;
    }

    public Piece getPiece() {
        if (piece == null)
            throw new NullPointerException("Wezwano bierkę z pola na którym nie ma bierki!");
        return piece;
    }

    /**
     * @param color kolor
     * @return true gdy pole jest kontrolowane przez dowolną bierkę danego koloru (bierka "widzi" to pole)
     */
    public boolean getControlled(ColorEnum color) {
        if (color == ColorEnum.white)
        return whiteControlled;
        else
            return blackControlled;
    }

    /**
     * Funkcja ustawia kontrolę tego pola przez dany kolor na true
     * @param color kolor
     */
    public void setControlled(ColorEnum color) {
        if (color == ColorEnum.white)
            whiteControlled = true;
        else
            blackControlled = true;
    }

    /**
     * Funkcja resetująca obie flagi kontroli
     */
    void resetControlled() {
        whiteControlled = blackControlled = false;
    }
}
