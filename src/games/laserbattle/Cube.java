package games.laserbattle;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

class Cube {
    static final int sl = 48; // Side length of cube
    private final int maxAmmo = 2;
    private int x;
    private int y;
    private String dir; // Direction
    private boolean up = false; // Whether up button is pressed
    private boolean down = false; // Whether down button is pressed
    private boolean left = false; // Whether left button is pressed
    private boolean right = false; // Whether right button is pressed
    private int ammo = maxAmmo;

    private Game parentGame;
    private final ArrayList<Laser> lasers = new ArrayList<>();

    Cube(int x, int y, String dir, Game parentGame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.parentGame = parentGame;

        // Starting timer
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (ammo < maxAmmo) {
                    ammo++;
                }
            }
        };
        timer.schedule(task, 0, 2000);
    }

    void paint(Graphics2D g2d) {
        // Drawing cube
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, sl, sl);

        // Drawing ammo #
        FontMetrics fm = g2d.getFontMetrics();
        String ammoStr = String.valueOf(ammo);
        g2d.drawString(ammoStr, x + (sl - fm.stringWidth(ammoStr)) / 2, y + (sl + fm.getAscent()) / 2);

        // Drawing shooter
        switch (dir) {
            case "UP":
                g2d.fillRect(x + sl/4, y - sl/4, sl/2, sl/4);
                break;
            case "DOWN":
                g2d.fillRect(x + sl/4, y + sl, sl/2, sl/4);
                break;
            case "LEFT":
                g2d.fillRect(x - sl/4, y + sl/4, sl/4, sl/2);
                break;
            case "RIGHT":
                g2d.fillRect(x + sl, y + sl/4, sl/4, sl/2);
                break;
        }

        // Drawing lasers
        g2d.setColor(Color.RED);
        for (Laser l: lasers) {
            l.paint(g2d);
        }
    }

    // Moves cube based on user input
    void move() {
        // Moving cube
        if (up && y > 0) {
            y -= 3;
        }
        if (down && y < Game.height - sl) {
            y += 3;
        }
        if (left && x > 0) {
            x -= 3;
        }
        if (right && x < Game.width - sl) {
            x += 3;
        }

        // Moving lasers
        for (Laser l: lasers) {
            l.move();
        }
    }

    // Methods to be used in KeyListener methods in Game.java
    void pressUp() {
        up = true;
        dir = "UP";
    }
    void pressDown() {
        down = true;
        dir = "DOWN";
    }
    void pressLeft() {
        left = true;
        dir = "LEFT";
    }
    void pressRight() {
        right = true;
        dir = "RIGHT";
    }
    void releaseUp() {
        up = false;
    }
    void releaseDown() {
        down = false;
    }
    void releaseLeft() {
        left = false;
    }
    void releaseRight() {
        right = false;
    }
    void fireLaser() {
        if (ammo > 0) {
            switch (dir) {
                case "UP":
                    lasers.add(new Laser(x + sl / 2 - Laser.thickness / 2, y - sl / 4 - Laser.length, "UP", parentGame));
                    break;
                case "DOWN":
                    lasers.add(new Laser(x + sl / 2 - Laser.thickness / 2, y + sl * 5 / 4, "DOWN", parentGame));
                    break;
                case "LEFT":
                    lasers.add(new Laser(x - sl / 4 - Laser.length, y + sl / 2 - Laser.thickness / 2, "LEFT", parentGame));
                    break;
                case "RIGHT":
                    lasers.add(new Laser(x + sl * 5 / 4, y + sl / 2 - Laser.thickness / 2, "RIGHT", parentGame));
                    break;
            }
            ammo--;
        }
    }

    // Gets bounds of Cube (used in Laser.java)
    Rectangle getRect() {
        return new Rectangle(x, y, sl, sl);
    }
}
