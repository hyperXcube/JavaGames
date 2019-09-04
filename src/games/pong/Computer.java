package games.pong;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

// Class to control the upper paddle, controlled by the computer
public class Computer {

    private Game pong;
    private final int v = 5; // Velocity
    static final int w = 60; // Width
    private int x = 250 - 30;
    private Random r = new Random();
    private boolean targetSet = false; // Whether the targetX value was set in move()
    private int targetX;
    private final int targetRange = 60;

    Computer(Game pong) {
        this.pong = pong;
    }

    void move() {
        Point ballCoord = pong.ball.getCoord(); // Current ball coordinate
        int ballYV = pong.ball.getYV();
        int ballXV = pong.ball.getXV();

        // Whether ball is coming in the direction of the computer and below the computer
        if (ballYV < 0) {
            if (!targetSet) {
                int collisionX = ballCoord.x + (20 - ballCoord.y) / ballYV * ballXV;

                // Fixing error of collisionX being out of range
                int collisionMod = Math.floorDiv(collisionX, pong.getWidth());
                if (collisionMod == -1) {
                    collisionX *= -1;
                } else if (collisionMod == 1) {
                    collisionX -= (collisionX - pong.getWidth()) * 2;
                } else {
                    collisionX %= pong.getWidth();
                }

                targetX = r.nextInt(targetRange) + collisionX + (Ball.d - targetRange - w) / 2;
                targetX = 5 * Math.round(targetX / 5);
                targetSet = true;
            }

            // Actual movement
            if (targetX < x && x > 0) {
                x -= v;
            } else if (targetX > x && x < pong.getWidth() - w) {
                x += v;
            }
        } else {
            targetSet = false;
        }
    }

    void paint(Graphics2D g) {
        g.fill(getRect());
    }

    Rectangle getRect() {
        return new Rectangle(x, 12 /*20 - 8*/, w, 8);
    }
}