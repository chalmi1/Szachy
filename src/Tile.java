import javax.swing.*;
import java.awt.*;

public class Tile {

    public String ChCoords;  // koordynaty szachowe np a5, b3 itd.
    private int[] IntCoords = new int[2];    // koordynaty tablicowe
    private final static int dimension = 75;
    private static int[] boardOffset = new int[2];
    public enum ColorEnum {black, white};
    ColorEnum tileColorEnum;
    Color tileColor;
    private boolean occupied = false;

    Tile(String coords, ColorEnum color) {
        tileColorEnum = color;
        convertEnumToColor();
        ChCoords = coords;
        convertPosToIndexed();
        boardOffset[0] = 0;
        boardOffset[1] = 0;
    }
    public boolean isOccupied() { return occupied; }

    public void draw(Graphics g) {
        g.setColor(tileColor);
        g.fillRect(IntCoords[0]*dimension+boardOffset[0], IntCoords[1]*dimension+boardOffset[1], dimension, dimension);
        }

    private void convertPosToIndexed() {
        char chx = ChCoords.charAt(0);
        int x = (int)chx - (int)'a';
        int result[] = new int[2];
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

}
