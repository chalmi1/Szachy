package Pieces;

import Game.Board;
import Game.PromotionFrame;
import Game.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Pawn extends Piece {

    private boolean enPassant = false;
    // zmienna warunkowa oznaczajaca mozliwosc zbicia
    // tego pionka w przelocie (tylko po ruchu o dwa pola i tylko przez jedną turę)

    public Pawn(Tile.ColorEnum c) {
        super('P', c);
        color = c;
        if (color == Tile.ColorEnum.white)
            file = new File("src/img/pawnw.png");
        else file = new File("src/img/pawnb.png");
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLegal(Point start, Point destination, Board brd) {
        if (color == Tile.ColorEnum.white) {
            if (destination.equals(new Point(start.x, start.y-1)) &&
                    !brd.tile[start.y-1][start.x].isOccupied()) {
                if (destination.y == 0) {
                    additional = true;
                }
                return true;    // ruch o jeden do przodu
            }

            if (start.y == 6) { // ruch z pierwszego rzędu
                if (destination.equals(new Point(start.x, start.y-2)) &&
                        !brd.tile[start.y-2][start.x].isOccupied() &&
                        !brd.tile[start.y-1][start.x].isOccupied()) {
                    enPassant = true;
                    if (destination.y == 0) {
                        additional = true;
                    }
                    return true;    // ruch o dwa do przodu
                }
            }
            if (destination.equals(new Point(start.x-1, start.y-1))) { // bicie
                if (brd.tile[start.y-1][start.x-1].isOccupied() &&
                        brd.tile[start.y-1][start.x-1].getPiece().color != color) {
                    if (destination.y == 0) {
                        additional = true;
                    }
                    return true;    // zwykle bicie
                }

                if (!brd.tile[start.y-1][start.x-1].isOccupied() && // bicie w przelocie
                        brd.tile[start.y][start.x-1].isOccupied()) {
                    Piece pc = brd.tile[start.y][start.x-1].getPiece();
                    if (pc.getSymbol() == 'p') {
                        Pawn pwn = (Pawn)pc;
                        if (pwn.enPassant)
                        {
                            additional = true;
                            return true;
                        }
                    }
                }
            }

            if (destination.equals(new Point(start.x+1, start.y-1))) {   // bicie
                if (brd.tile[start.y-1][start.x+1].isOccupied() && // zwykle bicie
                        brd.tile[start.y-1][start.x+1].getPiece().color != color) {
                    if (destination.y == 0) {
                        additional = true;
                    }
                    return true;
                }

                if (!brd.tile[start.y-1][start.x+1].isOccupied() && // bicie w przelocie
                        brd.tile[start.y][start.x+1].isOccupied()) {
                    Piece pc = brd.tile[start.y][start.x+1].getPiece();
                    if (pc.getSymbol() == 'p') {
                        Pawn pwn = (Pawn)pc;
                        if (pwn.enPassant)
                        {
                            additional = true;
                            return true;
                        }
                    }
                }
            }
        }
        if (color == Tile.ColorEnum.black) {
            if (destination.equals(new Point(start.x, start.y+1)) &&
                    !brd.tile[start.y+1][start.x].isOccupied()) {
                if (destination.y == 7) {
                    additional = true;
                }
                return true;    // ruch o jeden do przodu
            }

            if (start.y == 1) { // ruch z pierwszego rzędu
                if (destination.equals(new Point(start.x, start.y+2)) &&
                        !brd.tile[start.y+2][start.x].isOccupied() &&
                        !brd.tile[start.y+1][start.x].isOccupied()) {
                    enPassant = true;
                    if (destination.y == 7) {
                        additional = true;
                    }
                    return true;    // ruch o dwa do przodu
                }
            }
            if (destination.equals(new Point(start.x-1, start.y+1))) { // bicie
                if (brd.tile[start.y+1][start.x-1].isOccupied() &&
                        brd.tile[start.y+1][start.x-1].getPiece().color != color) {
                    if (destination.y == 7) {
                        additional = true;
                    }
                    return true;    // zwykle bicie
                }

                if (!brd.tile[start.y+1][start.x-1].isOccupied() && // bicie w przelocie
                        brd.tile[start.y][start.x-1].isOccupied()) {
                    Piece pc = brd.tile[start.y][start.x-1].getPiece();
                    if (pc.getSymbol() == 'P') {
                        Pawn pwn = (Pawn)pc;
                        if (pwn.enPassant)
                        {
                            additional = true;
                            return true;
                        }
                    }
                }
            }
            if (destination.equals(new Point(start.x+1, start.y+1))) {   // bicie
                if (brd.tile[start.y+1][start.x+1].isOccupied() && // zwykle bicie
                        brd.tile[start.y+1][start.x+1].getPiece().color != color) {
                    if (destination.y == 7) {
                        additional = true;
                    }
                    return true;
                }

                if (!brd.tile[start.y+1][start.x+1].isOccupied() && // bicie w przelocie
                        brd.tile[start.y][start.x+1].isOccupied()) {
                    Piece pc = brd.tile[start.y][start.x+1].getPiece();
                    if (pc.getSymbol() == 'p') {
                        Pawn pwn = (Pawn)pc;
                        if (pwn.enPassant)
                        {
                            additional = true;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void updateControlled(Point start, Board brd) {
        if (color == Tile.ColorEnum.white) {
            Point destination = new Point(start.x-1, start.y-1);
            if (brd.isInside(destination))
                brd.tile[destination.x][destination.y].setControlled(color);
            destination = new Point(start.x-1, start.y+1);
            if (brd.isInside(destination))
                brd.tile[destination.x][destination.y].setControlled(color);
        }
        else if (color == Tile.ColorEnum.black){
            Point destination = new Point(start.x+1, start.y-1);
            if (brd.isInside(destination))
                brd.tile[destination.x][destination.y].setControlled(color);
            destination = new Point(start.x+1, start.y+1);
            if (brd.isInside(destination))
                brd.tile[destination.x][destination.y].setControlled(color);
        }
    }

    @Override
    public void specialMove(Point destination, Board brd) {
        assert additional : "specialMove() wywołane gdy ruch nie jest specjalny";
        if (color == Tile.ColorEnum.white && destination.y==0)
            new PromotionFrame("Promocja", this, brd, destination);

        if (color == Tile.ColorEnum.white && destination.y==2)
            brd.tile[destination.y+1][destination.x].removePiece();

        if (color == Tile.ColorEnum.black && destination.y==7)
            new PromotionFrame("Promocja", this, brd, destination);

        if (color == Tile.ColorEnum.white && destination.y==5)
            brd.tile[destination.y+1][destination.x].removePiece();


    }

    public void resetEnPassant() {
        enPassant = false;
    }
}
