package Pieces;

import Game.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class Queen extends Piece {

    public Queen(Color c) {
        super('H', c);
        color = c;
        if (color == Color.white)
            file = new File("src/img/queenw.png");
        else file = new File("src/img/queenb.png");
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

        if (stcol == decol)
        {
            for (int i = min(strow, derow)+1; i < max(strow, derow); i++) {  // badanie czy między startem a końcem nie ma bierek
                if (brd.tile[i][stcol].isOccupied())
                    return false;
            }
            return true;
        }

        if (strow == derow)
        {
            for (int i = min(stcol, decol)+1; i < max(stcol, decol); i++) {  // badanie czy między startem a końcem nie ma bierek
                if (brd.tile[strow][i].isOccupied())
                    return false;
            }
            return true;
        }

        return false;
    }
}
