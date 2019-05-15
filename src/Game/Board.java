package Game;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel {
    public Tile[][] tile = new Tile[8][8];
    private Piece grabbedPiece = null;
    private Point firstClickCoords = null;
    private Game game;
    private Point LastMoveFrom = null;
    private Point LastMoveTo = null;

    Board(Game g) {
        game = g;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String coords = ""+((char)('a'+ col))+(8-row);
                if ((row+col)%2 == 0)
                    tile[row][col] = new Tile(coords, Tile.ColorEnum.white);
                else
                    tile[row][col] = new Tile(coords, Tile.ColorEnum.black);
            }
        }
        populate();
        ShowTextBoard();
        addMouseListener(new MouseAdapter() {
            Point coords;
            @Override
            public void mousePressed(MouseEvent e) {
                coords = getTileIndex(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point coords2 = getTileIndex(e.getPoint());
                if (coords.equals(coords2)) { // kursor nie opuscil pola
                    if (game.getTurnProgress() == 0 &&
                            tile[coords.y][coords.x].firstClick(game)) {
                        game.notifyClick(1);
                        grabbedPiece = tile[coords.y][coords.x].getPiece();
                        firstClickCoords = coords;
                        repaint();
                    }
                    else if (game.getTurnProgress() == 1) {
                        if (tile[coords.y][coords.x].secondClick(grabbedPiece, firstClickCoords, game.getBrd())) {
                            tile[firstClickCoords.y][firstClickCoords.x].removePiece();
                            if (LastMoveFrom != null)
                                tile[LastMoveFrom.y][LastMoveFrom.x].click();
                            LastMoveFrom = firstClickCoords;
                            tile[coords.y][coords.x].placePiece(grabbedPiece);
                            if (LastMoveTo != null)
                                tile[LastMoveTo.y][LastMoveTo.x].click();
                            LastMoveTo = coords;
                            game.notifyClick(2);
                            updateControlledTiles();

                            grabbedPiece = null;
                            firstClickCoords = null;
                            repaint();
                        }
                        else {
                            tile[firstClickCoords.y][firstClickCoords.x].click();
                            grabbedPiece = null;
                            firstClickCoords = null;
                            game.undoClick();
                            repaint();
                        }
                    }
                }
            }
        });
    }

    private void updateControlledTiles() {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                tile[row][col].resetControlled();
            }
        }
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece piece = tile[row][col].getPiece();
                if (piece == null)
                    continue;
                piece.updateControlled(new Point(row, col), this);
            }
        }

    }

    public void paint(Graphics g) {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                tile[row][col].draw(g);
            }
        }
    }

    private void populate() {
        // Wieże
        tile[0][0].placePiece(new Rook(Tile.ColorEnum.black));
        tile[0][7].placePiece(new Rook(Tile.ColorEnum.black));
        tile[7][0].placePiece(new Rook(Tile.ColorEnum.white));
        tile[7][7].placePiece(new Rook(Tile.ColorEnum.white));
        // Skoczki
        tile[0][1].placePiece(new Knight(Tile.ColorEnum.black));
        tile[0][6].placePiece(new Knight(Tile.ColorEnum.black));
        tile[7][1].placePiece(new Knight(Tile.ColorEnum.white));
        tile[7][6].placePiece(new Knight(Tile.ColorEnum.white));
        // Gońce
        tile[0][2].placePiece(new Bishop(Tile.ColorEnum.black));
        tile[0][5].placePiece(new Bishop(Tile.ColorEnum.black));
        tile[7][2].placePiece(new Bishop(Tile.ColorEnum.white));
        tile[7][5].placePiece(new Bishop(Tile.ColorEnum.white));
        // Hetmany
        tile[0][3].placePiece(new Queen(Tile.ColorEnum.black));
        tile[7][3].placePiece(new Queen(Tile.ColorEnum.white));
        // Królowie
        tile[0][4].placePiece(new King(Tile.ColorEnum.black));
        tile[7][4].placePiece(new King(Tile.ColorEnum.white));
        // Pionki
        tile[1][0].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][1].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][2].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][3].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][4].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][5].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][6].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][7].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[6][0].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][1].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][2].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][3].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][4].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][5].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][6].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][7].placePiece(new Pawn(Tile.ColorEnum.white));
    }

    private void ShowTextBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tile[row][col].getPiece() != null)
                System.out.print(tile[row][col].getPiece().getSymbol()+" ");
                else
                    System.out.print("- ");
            }
            System.out.print("\n");
        }
    }

    private Point getTileIndex(Point p) {
        return new Point(p.x/Tile.dimension, p.y/Tile.dimension);
    }

    private Point getTileIndex(String coords) {
        Point index = new Point();
        char chx = coords.charAt(0);
        int x = (int)chx - (int)'a';
        char chy = coords.charAt(1);
        int y = Integer.parseInt(""+chy);
        y = Math.abs(8-y);
        index.x = x;
        index.y = y;
        return index;
    }

    public boolean isInside(Point p) {
        return p.x <= 7 && p.x >= 0 && p.y >= 0 && p.y <= 7;
    }
}
