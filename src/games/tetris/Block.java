package games.tetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

class Block {
    private Rectangle[] area = new Rectangle[4];
    private Color color;
    private final AffineTransform transform = new AffineTransform();
    private Game tetris;

    Block(Tetromino blockType, Game parent) {
        tetris = parent;

        // Dummy code to initialize block, this needs to be changed later
        area[0] = new Rectangle(80, 80, 40, 40);
        area[1] = new Rectangle(120, 80, 40, 40);
        area[2] = new Rectangle(160, 80, 40, 40);
        area[3] = new Rectangle(120, 120, 40, 40);

        switch (blockType) {
            case I:
                color = new Color(135, 231, 235);
                break;
            case J:
                color = new Color(0, 0, 55);
                break;
            case L:
                color = new Color(255, 163, 51);
                break;
            case O:
                color = new Color(253, 255, 0);
                break;
            case S:
                color = new Color(64, 255, 0);
                break;
            case T:
                color = new Color(128, 0, 128);
                break;
            case Z:
                color = new Color(220, 20, 60);
        }
    }

    // Moves block one tile down
    synchronized void move() {
        // Checks to see if block can be moved one tile down
        for (int i = 0; i < 4; i++) {
            Rectangle rect = area[i];
            Rectangle newRect = new Rectangle(rect.x, rect.y, rect.width, rect.height);
            newRect.y += Constants.TILE;
            if (!new Rectangle(Constants.WIDTH, Constants.HEIGHT).contains(newRect)) {
                deactivate();
                return;
            }
        }
        // If the method doesn't return, the block is moved one tile down
        for (int i = 0; i < 4; i++) area[i].y += Constants.TILE;
    }

    // Moves area of block to bottomTiles and creates a new active block
    private void deactivate() {
        for (Rectangle r : area) tetris.bottomTiles.put(r, color);
        tetris.newActiveBlock();
    }

    void rotate() {
        // Add handling of rotation
    }

    void paint(Graphics2D g2d) {
        for (Rectangle r : area) {
            g2d.setColor(color);
            g2d.fill(r);
            g2d.setColor(Color.WHITE);
            g2d.draw(r);
        }
    }
}