package games.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener {
    private Block activeBlock; // Block that is in motion
    final HashMap<Rectangle, Color> bottomTiles = new HashMap<>(); // All of the tiles on the bottom

    private Game() {
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
    }

    void newActiveBlock() {
        int random = (int) (Math.random() * 7);
        Tetromino blockType = switch (random) {
            case 0 -> Tetromino.I;
            case 1 -> Tetromino.J;
            case 2 -> Tetromino.L;
            case 3 -> Tetromino.O;
            case 4 -> Tetromino.S;
            case 5 -> Tetromino.T;
            case 6 -> Tetromino.Z;
            default -> throw new IllegalArgumentException("Unexpected random value: " + random);
        };
        activeBlock = new Block(blockType, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
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
            case KeyEvent.VK_DOWN -> activeBlock.move();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }

    // Main method
    public static void main(String[] args) throws InterruptedException {
        // Initialization
        JFrame f = new JFrame("Tetris");
        Game tetris = new Game();

        // Frame setup
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        f.pack();
        f.add(tetris);
        f.setVisible(true);

        tetris.newActiveBlock();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                tetris.activeBlock.move();
            }
        };
        timer.schedule(task, 0, 500);

        while (true) {
            tetris.repaint();
            Thread.sleep(Constants.FRAME);
        }
    }
}
