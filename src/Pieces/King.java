package Pieces;

import Game.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class King extends Piece {
    private boolean moved = false;

    public King(Color c) {
        super('K', c);
        color = c;
        if (color == Color.white)
            file = new File("src/img/kingw.png");
        else file = new File("src/img/kingb.png");
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

        for (int y = start.y-1; y <= start.y+1; y++) {
            for (int x = start.x-1; x <= start.x+1; x++) {
                if (destination.equals(new Point(x,y))) {
                    if (brd.tile[destination.y][destination.x].isOccupied())
                    {
                        return brd.tile[destination.y][destination.x].getPiece().color != brd.tile[start.y][start.x].getPiece().color;
                    }
                    else
                        return true;
                }
            }
        }

        if (!moved) { // ROSZADA
            if (color == Color.white) {
                if (destination.equals(new Point(6,7)) ||
                    destination.equals(new Point(7,7))) {   // krótka roszada białego
                    if (brd.tile[7][5].isOccupied() || brd.tile[7][6].isOccupied())
                        return false;
                    if (brd.tile[7][7].getPiece().getSymbol() == 'W') {
                        Rook rk = (Rook)brd.tile[7][7].getPiece();
                        if (!rk.hasMoved()) {
                            destination.x = 6;
                            brd.tile[7][7].removePiece();
                            brd.tile[7][5].placePiece(rk);
                            rk.moved();
                            moved = true;
                            return true;
                        }
                        else return false;
                    }

                }
            }
        }


        return false;
    }
}
