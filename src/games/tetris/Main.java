package games.tetris;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Main {
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
