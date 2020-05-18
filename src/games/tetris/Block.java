package games.tetris;

import static games.tetris.Constants.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

class Block {
    private final Rectangle[] area = new Rectangle[4];
    private final Color color;
    private final Game tetris;
    private final int blockType;

    // Constructors
    Block(Game parent) {
        tetris = parent;

        area[0] = new Rectangle(TILE, TILE);
        area[1] = new Rectangle(TILE, TILE);
        area[2] = new Rectangle(TILE, TILE);
        area[3] = new Rectangle(TILE, TILE);

        blockType = (int) (Math.random() * 7);

        int startPos = (int) (Math.random() * (WIDTH / TILE - (blockType == 0 ? 4 : 3))) * TILE;

        switch (blockType) {
            case 0 -> {
                // I block
                color = new Color(135, 231, 235);
                area[0].setLocation(startPos, 0);
                area[1].setLocation(startPos + TILE, 0);
                area[2].setLocation(startPos + TILE * 2, 0);
                area[3].setLocation(startPos + TILE * 3, 0);
            }
            case 1 -> {
                // J block
                color = new Color(0, 0, 133);
                area[0].setLocation(startPos + TILE * 2, TILE);
                area[1].setLocation(startPos + TILE, TILE);
                area[2].setLocation(startPos, TILE);
                area[3].setLocation(startPos, 0);
            }
            case 2 -> {
                // L block
                color = new Color(255, 163, 51);
                area[0].setLocation(startPos, TILE);
                area[1].setLocation(startPos + TILE, TILE);
                area[2].setLocation(startPos + TILE * 2, TILE);
                area[3].setLocation(startPos + TILE * 2, 0);
            }
            case 3 -> {
                // O block
                color = new Color(253, 255, 0);
                area[0].setLocation(startPos, 0);
                area[1].setLocation(startPos + TILE, 0);
                area[2].setLocation(startPos, TILE);
                area[3].setLocation(startPos + TILE, TILE);
            }
            case 4 -> {
                // S block
                color = new Color(64, 255, 0);
                area[0].setLocation(startPos, TILE);
                area[1].setLocation(startPos + TILE, TILE);
                area[2].setLocation(startPos + TILE, 0);
                area[3].setLocation(startPos + TILE * 2, 0);
            }
            case 5 -> {
                // T block
                color = new Color(128, 0, 128);
                area[0].setLocation(startPos, TILE);
                area[1].setLocation(startPos + TILE, TILE);
                area[2].setLocation(startPos + TILE * 2, TILE);
                area[3].setLocation(startPos + TILE, 0);
            }
            case 6 -> {
                // Z block
                color = new Color(220, 20, 60);
                area[0].setLocation(startPos + TILE * 2, TILE);
                area[1].setLocation(startPos + TILE, TILE);
                area[2].setLocation(startPos + TILE, 0);
                area[3].setLocation(startPos, 0);
            }
            default -> throw new IllegalArgumentException("Unexpected random value: " + blockType);
        }
    }

    // Moves block one tile down, returns whether move was successful
    boolean moveDown() {
        // Checks to see if block can be moved one tile down
        for (Rectangle r : area) {
            Rectangle newRect = new Rectangle(r.x, r.y + TILE, r.width, r.height);
            if (isInvalid(newRect)) return false;
        }
        // If the method doesn't return, the block is moved one tile down
        for (int i = 0; i < 4; i++) area[i].y += TILE;
        return true;
    }

    // Moves block one tile left
    void moveLeft() {
        for (Rectangle r: area) {
            Rectangle newRect = new Rectangle(r.x - TILE, r.y, r.width, r.height);
            if (isInvalid(newRect)) return;
        }
        // If the method doesn't return, the block is moved one tile right
        for (int i = 0; i < 4; i++) area[i].x -= TILE;
    }

    // Moves block one tile right
    void moveRight() {
        for (Rectangle r: area) {
            Rectangle newRect = new Rectangle(r.x + TILE, r.y, r.width, r.height);
            if (isInvalid(newRect)) return;
        }
        // If the method doesn't return, the block is moved one tile right
        for (int i = 0; i < 4; i++) area[i].x += TILE;
    }

    // Checks if given rectangle can exist
    private boolean isInvalid(Rectangle r) {
        // If moved block will go out of the screen
        if (!new Rectangle(WIDTH, HEIGHT).contains(r)) return true;
        // If moved block has reached the bottom area
        for (Rectangle b : tetris.bottomTiles.keySet()) {
            if (b.contains(r)) return true;
        }
        return false;
    }

    // Adds area of block to bottomTiles and replaces the active block
    void deactivate() {
        for (Rectangle r : area) {
            tetris.bottomTiles.put(r, color);
            if (r.y == 0) {
                tetris.lose();
                break;
            }
        }
    }

    //Checks whether rotating a block doesn't cause it to run into any bottom tiles
    boolean canRotate() {
        rotateClockwise();
        for (Rectangle r: area) {
            if (isInvalid(r)) {
                rotateCounterClockwise();
                return false;
            }
        }
        rotateCounterClockwise();
        return true;
    }

    //Rotates the block clockwise using dot products from linear algebra
    void rotateClockwise() {
        if (blockType != 3) {
            Rectangle[] relativeArea = new Rectangle[4];
            Point center = new Point(area[1].x, area[1].y);
            for (int i = 0; i < 4; i++) {
                relativeArea[i] = new Rectangle((area[i].x - center.x) / TILE, (area[i].y - center.y) / TILE, 1, 1);
                int temp = relativeArea[i].x;
                relativeArea[i].x = relativeArea[i].y * (-1);
                relativeArea[i].y = temp;
            }
            for (int i = 0; i < 4; i++) {
                area[i].x = relativeArea[i].x * TILE + center.x;
                area[i].y = relativeArea[i].y * TILE + center.y;
            }
        }
    }

    //Rotates the block counter clockwise
    private void rotateCounterClockwise() {
        for (int i = 0; i < 3; i++) {
            rotateClockwise();
        }
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