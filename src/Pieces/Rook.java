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
    protected void checkMove() {

    }
}
