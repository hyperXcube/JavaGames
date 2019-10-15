package games.tetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

class Block {
    private final Rectangle[] area = new Rectangle[4];
    private final Color color;
    private final AffineTransform transform = new AffineTransform();
    private final Game tetris;

    Block(Game parent) {
        double random = Math.random();
        int startPos = (int) (random * (Constants.WIDTH - Constants.TILE * 3)) - (int) (random * (Constants.WIDTH - Constants.TILE * 3)) % Constants.TILE;

        tetris = parent;

        area[0] = new Rectangle(Constants.TILE, Constants.TILE);
        area[1] = new Rectangle(Constants.TILE, Constants.TILE);
        area[2] = new Rectangle(Constants.TILE, Constants.TILE);
        area[3] = new Rectangle(Constants.TILE, Constants.TILE);

        int blockType = (int) (Math.random() * 7);
        switch (blockType) {
            case 0 -> {
                // I block
                color = new Color(135, 231, 235);
                area[0].setLocation(startPos, 0);
                area[1].setLocation(startPos + Constants.TILE, 0);
                area[2].setLocation(startPos + Constants.TILE * 2, 0);
                area[3].setLocation(startPos + Constants.TILE * 3, 0);
            }
            case 1 -> {
                // J block
                color = new Color(0, 0, 55);
                area[0].setLocation(startPos, 0);
                area[1].setLocation(startPos, Constants.TILE);
                area[2].setLocation(startPos + Constants.TILE, Constants.TILE);
                area[3].setLocation(startPos + Constants.TILE * 2, Constants.TILE);
            }
            case 2 -> {
                // L block
                color = new Color(255, 163, 51);
                area[0].setLocation(startPos, Constants.TILE);
                area[1].setLocation(startPos + Constants.TILE, Constants.TILE);
                area[2].setLocation(startPos + Constants.TILE * 2, Constants.TILE);
                area[3].setLocation(startPos + Constants.TILE * 2, 0);
            }
            case 3 -> {
                // O block
                color = new Color(253, 255, 0);
                area[0].setLocation(startPos, 0);
                area[1].setLocation(startPos + Constants.TILE, 0);
                area[2].setLocation(startPos, Constants.TILE);
                area[3].setLocation(startPos + Constants.TILE, Constants.TILE);
            }
            case 4 -> {
                // S block
                color = new Color(64, 255, 0);
                area[0].setLocation(startPos, Constants.TILE);
                area[1].setLocation(startPos + Constants.TILE, Constants.TILE);
                area[2].setLocation(startPos + Constants.TILE, 0);
                area[3].setLocation(startPos + Constants.TILE * 2, 0);
            }
            case 5 -> {
                // T block
                color = new Color(128, 0, 128);
                area[0].setLocation(startPos, 0);
                area[1].setLocation(startPos + Constants.TILE, 0);
                area[2].setLocation(startPos + Constants.TILE * 2, 0);
                area[3].setLocation(startPos + Constants.TILE, Constants.TILE);
            }
            case 6 -> {
                // Z block
                color = new Color(220, 20, 60);
                area[0].setLocation(startPos, 0);
                area[1].setLocation(startPos + Constants.TILE, 0);
                area[2].setLocation(startPos + Constants.TILE, Constants.TILE);
                area[3].setLocation(startPos + Constants.TILE * 2, Constants.TILE);
            }
            default -> throw new IllegalArgumentException("Unexpected random value: " + blockType);
        }
    }

    // Moves block one tile down, returns whether move was successful
    boolean moveDown() {
        // Checks to see if block can be moved one tile down
        for (Rectangle r : area) {
            Rectangle newRect = new Rectangle(r.x, r.y + Constants.TILE, r.width, r.height);

            // If moved block will go out of the screen
            if (!new Rectangle(Constants.WIDTH, Constants.HEIGHT).contains(newRect)) {
                return false;
            }
            // If moved block has reached the bottom area
            for (Rectangle b : tetris.bottomTiles.keySet()) {
                if (b.contains(newRect)) {
                    return false;
                }
            }
        }
        // If the method doesn't return, the block is moved one tile down
        for (int i = 0; i < 4; i++) area[i].y += Constants.TILE;
        return true;
    }

    // Moves the block one tile diagonally
    void moveLeft() {
        for (Rectangle r: area) {
            Rectangle newRect = new Rectangle(r.x - Constants.TILE, r.y, r.width, r.height);

            // If moved block will go out of the screen
            if (!new Rectangle(Constants.WIDTH, Constants.HEIGHT).contains(newRect)) {
                return;
            }
            // If moved block has reached the bottom area
            for (Rectangle b : tetris.bottomTiles.keySet()) {
                if (b.contains(newRect)) {
                    return;
                }
            }
        }
        // If the method doesn't return, the block is moved one tile right
        for (int i = 0; i < 4; i++) area[i].x -= Constants.TILE;
    }

    // Moves the block one tile right
    void moveRight() {
        for (Rectangle r: area) {
            Rectangle newRect = new Rectangle(r.x + Constants.TILE, r.y, r.width, r.height);

            // If moved block will go out of the screen
            if (!new Rectangle(Constants.WIDTH, Constants.HEIGHT).contains(newRect)) {
                return;
            }
            // If moved block has reached the right side
            for (Rectangle b : tetris.bottomTiles.keySet()) {
                if (b.contains(newRect)) {
                    return;
                }
            }
        }
        // If the method doesn't return, the block is moved one tile right
        for (int i = 0; i < 4; i++) area[i].x += Constants.TILE;
    }

    // Adds area of block to bottomTiles and replaces the active block
    void deactivate() {
        for (Rectangle r : area) tetris.bottomTiles.put(r, color);
    }

    void rotate() {
        // TODO: Add handling of rotation
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