package games.tetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

class Block {
    private Rectangle[][] area = new Rectangle[4][4];
    private int rotation = 0;
    private Color color;
    private Game tetris;

    Block(Tetromino blockType, Game parent) {
        tetris = parent;

        // Dummy code to initialize block, this needs to be changed later
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) area[i][j] = new Rectangle(80, 80, 80, 80);
        }

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
    void move() {
        // Checks to see if block can be moved one tile down
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rect = area[i][j];
                Rectangle newRect = new Rectangle(rect.x, rect.y, rect.width, rect.height);
                newRect.y += Constants.TILE;
                if (!new Rectangle(Constants.WIDTH, Constants.HEIGHT).contains(newRect)) {
                    deactivate();
                    return;
                }
            }
        }
        // If the method doesn't return, the block is moved one tile down
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) area[i][j].y += Constants.TILE;
        }
    }

    // Moves area of block to bottomTiles and creates a new active block
    private void deactivate() {
        for (Rectangle r : area[rotation]) tetris.bottomTiles.put(r, color);
        tetris.newActiveBlock();
    }

    void paint(Graphics2D g2d) {
        for (Rectangle r : area[rotation]) {
            g2d.setColor(color);
            g2d.fill(r);
            g2d.setColor(Color.WHITE);
            g2d.draw(r);
        }
    }
}