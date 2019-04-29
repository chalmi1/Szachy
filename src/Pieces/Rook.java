package Pieces;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Rook extends Piece {

    public Rook(Color c) {
        super('R', c);
        color = c;
        if (color == Color.white)
        file = new File("src/img/rookw.png");
        else file = new File("src/img/rookb.png");
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLegal(String start, String destination) {
        if (start.equals(destination))
            return false;
        char stx = start.charAt(0);
        char sty = start.charAt(1);
        char dex = destination.charAt(0);
        char dey = destination.charAt(1);

        if (stx == dex)
            return true;
        return sty == dey;
    }
}
