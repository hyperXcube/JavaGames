package games.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener {
    private Block activeBlock; // Block that is in motion
    private final HashMap<Rectangle, Color> bottomTiles = new HashMap<>(); // All of the tiles on the bottom

    private Game() {
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        activeBlock.paint(g2d);
        for (Rectangle r : bottomTiles.keySet()) g2d.draw(r);
    }

    // KeyListener methods (only keyPressed is needed)
    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // TODO: Handle rotation inputs
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

        while (true) {
            tetris.repaint();
            Thread.sleep(Constants.FRAME);
        }
    }
}
