package Game;

import Pieces.Piece;
import Pieces.Rook;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private Tile[][] tiles = new Tile[8][8];
    Board() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String coords = ""+((char)('a'+ col))+(8-row);
                if ((row+col)%2 == 0)
                    tiles[row][col] = new Tile(coords, Tile.ColorEnum.white);
                else
                    tiles[row][col] = new Tile(coords, Tile.ColorEnum.black);
            }
        }
        populate();
        ShowTextBoard();
    }

    public void paint(Graphics g) {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                tiles[row][col].draw(g);
            }
        }
    }

    private void populate() {
        tiles[0][0].placePiece(new Rook(Piece.Color.black));
        tiles[0][7].placePiece(new Rook(Piece.Color.black));
        tiles[7][0].placePiece(new Rook(Piece.Color.white));
        tiles[7][7].placePiece(new Rook(Piece.Color.white));
    }

    private void ShowTextBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tiles[row][col].piece != null)
                System.out.print(tiles[row][col].piece.getSymbol()+tiles[row][col].ChCoords+" ");
                else
                    System.out.print("-"+tiles[row][col].ChCoords+" ");


            }
            System.out.print("\n");
        }
    }
}
