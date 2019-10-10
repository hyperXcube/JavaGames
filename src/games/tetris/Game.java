package games.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel {
    private Game() {
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Do nothing for now
    }

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
            Thread.sleep(Constants.DELAY);
        }
    }
}
