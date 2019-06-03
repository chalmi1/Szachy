package Pieces;

import Game.Board;
import Game.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

/**
 * Klasa reprezentująca gońca
 */
public class Bishop extends Piece {

    public Bishop(Tile.ColorEnum c) {
        super('G', c);
        color = c;
        if (color == Tile.ColorEnum.white)
            file = new File("src/img/bishopw.png");
        else file = new File("src/img/bishopb.png");
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean isLegal(Point start, Point destination, Board brd) {
        if (start.equals(destination))  // nie można zrobić ruchu w miejscu
            return false;
        int stcol = start.x;
        int strow = start.y;
        int decol = destination.x;
        int derow = destination.y;

        if (brd.tile[derow][decol].isOccupied() &&  // nie można zbić bierki własnego koloru
                brd.tile[derow][decol].getPiece().color == brd.tile[strow][stcol].getPiece().color)
            return false;

        if (stcol+strow == decol+derow) // przekatna w kierunku /
        {
            for (Point i = new Point(min(stcol,  decol)+1, max(strow,  derow)-1);
                 !i.equals(destination) && !i.equals(start); i.x++, i.y--) {  // badanie czy między startem a końcem nie ma bierek
                if (brd.tile[i.y][i.x].isOccupied())
                    return false;
            }
            return true;
        }

        if (strow-stcol == derow-decol) // przekątna w kierunku \
        {
            for (Point i = new Point(min(stcol,  decol)+1, min(strow,  derow)+1 );
                 !i.equals(destination) && !i.equals(start); i.x++, i.y++) {  // badanie czy między startem a końcem nie ma bierek
                if (brd.tile[i.y][i.x].isOccupied())
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateControlled(Point start, Board brd) {
        for (Point i = new Point(start.x-1, start.y-1); brd.isInside(i); i.x--, i.y-- ) {
            brd.tile[i.x][i.y].setControlled(color);    // lewa góra
            if (brd.tile[i.x][i.y].isOccupied()) break;
        }
        for (Point i = new Point(start.x+1, start.y+1); brd.isInside(i); i.x++, i.y++ ) {
            brd.tile[i.x][i.y].setControlled(color);    // prawy dół
            if (brd.tile[i.x][i.y].isOccupied()) break;
        }
        for (Point i = new Point(start.x+1, start.y-1); brd.isInside(i); i.x++, i.y-- ) {
            brd.tile[i.x][i.y].setControlled(color);    // lewy dół
            if (brd.tile[i.x][i.y].isOccupied()) break;
        }
        for (Point i = new Point(start.x-1, start.y+1); brd.isInside(i); i.x--, i.y++ ) {
            brd.tile[i.x][i.y].setControlled(color);    // prawa góra
            if (brd.tile[i.x][i.y].isOccupied()) break;
        }
    }

    /**
     * brak ruchu specjalnego u gońca
     * @param destination punkt docelowy ruchu
     * @param brd         szachownica
     */
    @Override
    public void specialMove(Point destination, Board brd) {
        assert additional : "specialMove() wywołane gdy ruch nie jest specjalny";
    }
}
