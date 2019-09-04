package games.pong;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// Class to control lower paddle, controlled by the player
public class Player implements KeyListener {

    private Game pong;
    private final int v = 5; // Velocity
    static final int w = 60; // Width
    private int x = 250 - 30;
    private boolean left = false; // Whether left arrow key is pressed 
    private boolean right = false; // Whether right arrow key is pressed

    Player(Game pong) {
        this.pong = pong;
    }

    void move() {
        if (left && x > 0) {
            x -= v;
        }
        if (right && x < pong.getWidth() - w) {
            x += v;
        }
    }

    void paint(Graphics2D g) {
        g.fill(getRect());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }

    Rectangle getRect() {
        return new Rectangle(x, pong.getHeight() - 20, w, 8);
    }

    void reset() {
        left = false;
        right = false;
    }
}