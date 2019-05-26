package Pieces;

import Game.Board;
import Game.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Knight extends Piece{

    public Knight(Tile.ColorEnum c) {
        super('S', c);
        color = c;
        if (color == Tile.ColorEnum.white)
            file = new File("src/img/knightw.png");
        else file = new File("src/img/knightb.png");
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLegal(Point start, Point destination, Board brd) {
        if (!brd.tile[destination.y][destination.x].isOccupied() ||
                brd.tile[destination.y][destination.x].getPiece().getColor() != color) {
            if (destination.equals(new Point(start.x-1,  start.y-2)))
                return true;
            if (destination.equals(new Point(start.x+1, start.y-2)))
                return true;
            if (destination.equals(new Point(start.x-1, start.y+2)))
                return true;
            if (destination.equals(new Point(start.x+1, start.y+2)))
                return true;
            if (destination.equals(new Point(start.x-2, start.y-1)))
                return true;
            if (destination.equals(new Point(start.x-2, start.y+1)))
                return true;
            if (destination.equals(new Point(start.x+2, start.y-1)))
                return true;
            return destination.equals(new Point(start.x+2, start.y+1));
        }
        return false;
    }

    @Override
    public void updateControlled(Point start, Board brd) {
        Point destination = new Point(start.x-1,  start.y-2);   // 2 lewo + 1 góra
        if (brd.isInside(destination))
            brd.tile[destination.x][destination.y].setControlled(color);
        destination = new Point(start.x+1, start.y-2);  // 2 lewo + 1 dół
        if (brd.isInside(destination))
            brd.tile[destination.x][destination.y].setControlled(color);
        destination = new Point(start.x-1, start.y+2);  // 2 prawo + 1 góra
        if (brd.isInside(destination))
            brd.tile[destination.x][destination.y].setControlled(color);
        destination = new Point(start.x+1, start.y+2);  // 2 prawo + 1 dół
        if (brd.isInside(destination))
            brd.tile[destination.x][destination.y].setControlled(color);
        destination = new Point(start.x-2, start.y-1);  // 2 góra + 1 lewo
        if (brd.isInside(destination))
            brd.tile[destination.x][destination.y].setControlled(color);
        destination = new Point(start.x-2, start.y+1);  // 2 góra + 1 prawo
        if (brd.isInside(destination))
            brd.tile[destination.x][destination.y].setControlled(color);
        destination = new Point(start.x+2, start.y-1);  // 2 dół + 1 lewo
        if (brd.isInside(destination))
            brd.tile[destination.x][destination.y].setControlled(color);
        destination = new Point(start.x+2, start.y+1);  // 2 dół + 1 prawo
        if (brd.isInside(destination))
            brd.tile[destination.x][destination.y].setControlled(color);
    }
}
