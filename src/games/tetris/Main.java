package games.tetris;

import static games.tetris.Constants.*;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Initialization
        JFrame f = new JFrame("Tetris");
        Game tetris = new Game();

        // Frame setup
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        f.pack();
        f.add(tetris);
        f.setVisible(true);

        while (true) {
            tetris.repaint();
            Thread.sleep(FRAME);
        }
    }
}
