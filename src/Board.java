import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    Tile tiles[][] = new Tile[8][8];
    Board() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String coords = ""+((char)('a'+ row))+(col+1);
                if ((row+col)%2 == 1)
                    tiles[row][col] = new Tile(coords, Tile.ColorEnum.white);
                else
                    tiles[row][col] = new Tile(coords, Tile.ColorEnum.black);
            }
        }
    }
    public void paint(Graphics g) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col].draw(g);
            }
        }
    }

}
