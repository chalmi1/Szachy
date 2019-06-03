package Game;

import java.awt.*;

/**
 * prosta klasa przechowująca podstawowe informacje o ruchu (skąd i dokąd)
 */
class Move {
    Point from;
    Point to;
    Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }
}
