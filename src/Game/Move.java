package Game;

import java.awt.*;

class Move { // prosta klasa przechowująca podstawowe informacje o ruchu (skąd i dokąd)
    Point from;
    Point to;
    Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }
}
