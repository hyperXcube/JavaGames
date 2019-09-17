package games.laserbattle;

import java.awt.Graphics2D;
import java.awt.Rectangle;

class Laser {
    static final int length = Cube.sl;
    static final int thickness = Cube.sl / 4;
    private int x;
    private int y;
    private String dir; // Direction
    private Game parentGame;

    Laser(int x, int y, String dir, Game parentGame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.parentGame = parentGame;
    }

    void paint(Graphics2D g2d) {
        if (dir.equals("UP") || dir.equals("DOWN")) {
            g2d.fillRect(x, y, thickness, length);
        } else {
            g2d.fillRect(x, y, length, thickness);
        }
    }

    void move() {
        // Moving lasers
        switch (dir) {
            case "UP":
                y -= 4;
                break;
            case "DOWN":
                y += 4;
                break;
            case "LEFT":
                x -= 4;
                break;
            case "RIGHT":
                x += 4;
                break;
        }

        // Checking if any player died
        Rectangle lRect; // Laser rectangle
        if (dir.equals("UP") || dir.equals("DOWN")) {
            lRect = new Rectangle(x, y, thickness, length);
        } else {
            lRect = new Rectangle(x, y, length, thickness);
        }
        if (parentGame.p1.getRect().intersects(lRect)) {
            parentGame.endMsg(2);
        }
        if (parentGame.p2.getRect().intersects(lRect)) {
            parentGame.endMsg(1);
        }
    }
}