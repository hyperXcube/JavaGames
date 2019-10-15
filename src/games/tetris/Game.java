package games.tetris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener {
    private Block activeBlock; // Block that is in motion
    final HashMap<Rectangle, Color> bottomTiles = new HashMap<>(); // All of the tiles on the bottom

    Game() {
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        activeBlock = new Block(this);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                blockDown();
            }
        };
        timer.schedule(task, 0, 500);
    }

    // Moves block down, checking to see if the active block should be replaced. Returns result of moveDown
    private boolean blockDown() {
        boolean moveSuccessful = activeBlock.moveDown();
        if (!moveSuccessful) {
            activeBlock.deactivate();
            activeBlock = new Block(this);
        }
        return moveSuccessful;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        activeBlock.paint(g2d);
        for (Rectangle r : bottomTiles.keySet()) {
            g2d.setColor(bottomTiles.get(r));
            g2d.fill(r);
            g2d.setColor(Color.WHITE);
            g2d.draw(r);
        }
    }

    // KeyListener methods (only keyPressed is needed)
    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP -> activeBlock.rotate();
            case KeyEvent.VK_DOWN -> blockDown();
            case KeyEvent.VK_SPACE -> {
                boolean keepMovingDown = true;
                while (keepMovingDown) {
                    keepMovingDown = blockDown();
                }
            }
            case KeyEvent.VK_LEFT -> activeBlock.moveLeft();
            case KeyEvent.VK_RIGHT -> activeBlock.moveRight();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }
}
