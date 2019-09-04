package games.pong;

import games.util.Sound;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;
import java.lang.InterruptedException;

// Class to control the ball
public class Ball {

    private Game pong;
    private final Random r = new Random();
    static final int d = 16; // Diameter
    private int x = r.nextInt(500 - d - 10) + 5;
    private int y = 400 - d / 2;
    private int xv = 7; // X Velocity
    private int yv = xv; // Y Velocity
    private int mid = x + d / 2; // X value of the middle of the ball
    
    enum Direction {
        UP, DOWN, RANDOM
    }

    Ball(Game pong, Direction startDirection) {
        this.pong = pong;
        if (startDirection == Direction.UP || startDirection == Direction.RANDOM && r.nextBoolean()) {
            yv *= -1;
        }
        if (r.nextBoolean()) {
            xv *= -1;
        }
    }

    void move() throws InterruptedException {
        Rectangle playerRect = pong.player.getRect();
        Rectangle computerRect = pong.computer.getRect();
        boolean playerHit = playerRect.contains(mid, y + d);
        boolean computerHit = computerRect.contains(mid, y);
        
        if (x <= 0 || x >= pong.getWidth() - d) {
            Sound.play("hit", "pong");
            xv *= -1;
        } else if (computerHit || playerHit) {
            Sound.play("hit", "pong");

            double loc; // Relative (left to right) location of ball from middle of paddle (ranges from -6 to 6)
            if (playerHit) {
                y = pong.getHeight() - 20 - d;
                loc = (mid - playerRect.x - Player.w / 2) / 5;
            } else {
                y = 20;
                loc = (mid - computerRect.x - Computer.w / 2) / 5;
            }

            if (loc > 0) {
                xv = (int) Math.ceil(loc);
            } else {
                xv = (int) Math.floor(loc);
            }

            yv *= -1;
        } else if (y >= pong.getHeight()) {
            pong.computerPoint();
        } else if (y <= -d) {
            pong.playerPoint();
        }

        x += xv;
        y += yv;
        mid = x + d / 2;
    }

    void paint(Graphics2D g) {
        g.fillOval(x, y, d, d);
    }

    Point getCoord() {
        return new Point(x, y);
    }

    int getXV() {
        return xv;
    }

    int getYV() {
        return yv;
    }
}