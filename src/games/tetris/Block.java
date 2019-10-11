package games.tetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

class Block {
    private Rectangle[][] rects = new Rectangle[4][4];
    private int rotation = 0;
    private Color color;

    Block(Tetromino blockType) {
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
                break;
        }

        TimerTask move = new TimerTask() {
            @Override
            public void run() {
                for (Rectangle[] obj : rects) {
                    for (Rectangle r : obj) r.y += Constants.TILE;
                }
            }
        };
        (new Timer()).schedule(move, 0, 500);
    }

    void paint(Graphics2D g2d) {
        g2d.setColor(color);
        for (Rectangle r : rects[rotation]) g2d.draw(r);
    }
}