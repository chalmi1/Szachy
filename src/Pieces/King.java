package Pieces;

import Game.Board;
import Game.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class King extends Piece {
    private boolean moved = false;
    private Rook rk;

    public King(Tile.ColorEnum c) {
        super('K', c);
        color = c;
        if (color == Tile.ColorEnum.white)
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
                    if (brd.tile[destination.y][destination.x].getControlled(opposingColor))
                        // pole kontrolowane przez przeciwnika
                        return false;
                    if (brd.tile[destination.y][destination.x].isOccupied())
                        // gdy pole zajęte, wejdź tylko gdy stoi tam przeciwnik
                        return brd.tile[destination.y][destination.x].getPiece().color != brd.tile[start.y][start.x].getPiece().color;
                    else
                        return true;
                }
            }
        }

        if (!moved) { // ROSZADA
            if (color == Tile.ColorEnum.white) {
                if (destination.equals(new Point(6,7))) {   // krótka roszada białego
                    if (brd.tile[7][5].isOccupied() || brd.tile[7][6].isOccupied() ||
                            brd.tile[7][5].getControlled(opposingColor) || brd.tile[7][6].getControlled(opposingColor))
                        return false;
                    if (brd.tile[7][0].isOccupied() &&
                            brd.tile[7][7].getPiece().getSymbol() == 'W') {
                        rk = (Rook)brd.tile[7][7].getPiece();
                        if (!rk.hasMoved()) {
                            additional = true;
                            return true;
                        }
                        else return false;
                    }
                }
                else if (destination.equals(new Point(2, 7))) { // długa roszada białego
                    if (brd.tile[7][3].isOccupied() || brd.tile[7][2].isOccupied() ||
                            brd.tile[7][3].getControlled(opposingColor) || brd.tile[7][2].getControlled(opposingColor))
                        return false;
                    if (brd.tile[7][0].isOccupied() &&
                            brd.tile[7][0].getPiece().getSymbol() == 'W') {
                        rk = (Rook)brd.tile[7][0].getPiece();
                        if (!rk.hasMoved()) {
                            additional = true;
                            return true;
                        }
                        else return false;
                    }
                }
            }
            else if (color == Tile.ColorEnum.black) {
                if (destination.equals(new Point(6,0))) {   // krótka roszada czarnego
                    if (brd.tile[0][5].isOccupied() || brd.tile[0][6].isOccupied() ||
                    brd.tile[0][5].getControlled(opposingColor) || brd.tile[0][6].getControlled(opposingColor))
                        return false;
                    if (brd.tile[7][0].isOccupied() &&
                            brd.tile[0][7].getPiece().getSymbol() == 'w') {
                        rk = (Rook)brd.tile[0][7].getPiece();
                        if (!rk.hasMoved()) {
                            additional = true;
                            return true;
                        }
                        else return false;
                    }
                }
                else if (destination.equals(new Point(2, 0))) { // długa roszada czarnego
                    if (brd.tile[0][3].isOccupied() || brd.tile[0][2].isOccupied()  ||
                            brd.tile[0][3].getControlled(opposingColor) || brd.tile[0][6].getControlled(opposingColor))
                        return false;
                    if (brd.tile[7][0].isOccupied() &&
                            brd.tile[0][0].getPiece().getSymbol() == 'w') {
                        rk = (Rook)brd.tile[0][0].getPiece();
                        if (!rk.hasMoved()) {
                            additional = true;
                            return true;
                        }
                        else return false;
                    }
                }
            }
        }


        return false;
    }

    @Override
    public void updateControlled(Point start, Board brd) {
        for (Point i = new Point(start.x-1, start.y-1); i.x<=start.x+1 && brd.isInside(i); i.x++) {
            for (; i.y <= start.y+1 && brd.isInside(i); i.y++) {
                if  (start.equals(i))
                    continue;
                brd.tile[i.x][i.y].setControlled(color);
                if (brd.tile[i.x][i.y].isOccupied()) break;
            }

        }
    }

    @Override
    public void specialMove(Point destination, Board brd) {
        assert additional : "specialMove() wywołane gdy ruch nie jest specjalny";
        if (color == Tile.ColorEnum.white && destination.equals(new Point(6,7))) {
            brd.tile[7][7].removePiece();
            brd.tile[7][5].placePiece(rk);
            rk.moved();
            moved = true;
        }
        if (color == Tile.ColorEnum.white && destination.equals(new Point(2,7))) {
            brd.tile[7][0].removePiece();
            brd.tile[7][3].placePiece(rk);
            rk.moved();
            moved = true;
        }
        if (color == Tile.ColorEnum.black && destination.equals(new Point(6,0))) {
            brd.tile[0][7].removePiece();
            brd.tile[0][5].placePiece(rk);
            rk.moved();
            moved = true;
        }
        if (color == Tile.ColorEnum.black && destination.equals(new Point(2,0))) {
            brd.tile[0][0].removePiece();
            brd.tile[0][3].placePiece(rk);
            rk.moved();
            moved = true;
        }
    }

    boolean hasMoved() {
        return moved;
    }

    public void moved() {
        this.moved = true;
    }
}
