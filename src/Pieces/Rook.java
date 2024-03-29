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
 * Klasa reprezentująca wieżę
 */
public class Rook extends Piece {

    /**
     * Flaga oznaczająca czy ta wieża poruszyła się w tej grze (ma to znaczenie przy ewentualnej roszadzie)
     */
    private boolean moved = false;
    public Rook(Tile.ColorEnum c) {
        super('W', c);
        color = c;
        if (color == Tile.ColorEnum.white)
        file = new File("src/img/rookw.png");
        else file = new File("src/img/rookb.png");
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLegal(Point start, Point destination, Game.Board brd) {
        if (start.equals(destination))  // nie można zrobić ruchu w miejscu
            return false;
        int stcol = start.x;
        int strow = start.y;
        int decol = destination.x;
        int derow = destination.y;

        if (brd.tile[derow][decol].isOccupied() &&  // nie można zbić bierki własnego koloru
                brd.tile[strow][stcol].isOccupied() &&
                brd.tile[derow][decol].getPiece().color == brd.tile[strow][stcol].getPiece().color)
            return false;

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

    @Override
    public void updateControlled(Point start, Board brd) {
        for (Point i = new Point(start.x, start.y-1); brd.isInside(i); i.y--) {
            brd.tile[i.x][i.y].setControlled(color);    // lewo
            if (brd.tile[i.x][i.y].isOccupied()) break;
        }
        for (Point i = new Point(start.x, start.y+1); brd.isInside(i); i.y++) {
            brd.tile[i.x][i.y].setControlled(color);    // prawo
            if (brd.tile[i.x][i.y].isOccupied()) break;
        }
        for (Point i = new Point(start.x-1, start.y); brd.isInside(i); i.x--) {
            brd.tile[i.x][i.y].setControlled(color);    // góra
            if (brd.tile[i.x][i.y].isOccupied()) break;
        }
        for (Point i = new Point(start.x+1, start.y); brd.isInside(i); i.x++) {
            brd.tile[i.x][i.y].setControlled(color);    // dół
            if (brd.tile[i.x][i.y].isOccupied()) break;
        }
    }

    /**
     * Brak ruchów specjalnych wieżą (roszadę wykonuje się ruszając królem o dwa pola)
     * @param destination punkt docelowy ruchu
     * @param brd         szachownica
     */
    @Override
    public void specialMove(Point destination, Board brd) {
        assert additional : "specialMove() wywołane gdy ruch nie jest specjalny";
    }

    boolean hasMoved() {
        return moved;
    }

    public void moved() {
        this.moved = true;
    }
}
