package games.laserbattle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener {
    static final int width = 600; // Width of window
    static final int height = 600; // Height of window
    private final int startOffset = 9; // How far the cubes start from the edges
    private boolean running = false; // Whether game is in play
    // TODO: Add top and left margins

    // Objects representing the two players
    Cube p1;
    Cube p2;

    private Game() {
        addKeyListener(this);
        setFocusable(true);
        reset();
    }

    private void reset() {
        p1 = new Cube(startOffset, startOffset,"RIGHT", this);
        p2 = new Cube(width - Cube.sl - startOffset, height - Cube.sl - startOffset, "LEFT", this);
        running = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Drawing cubes
        g2d.setFont(new Font("Verdana", Font.BOLD, 36));
        p1.paint(g2d);
        p2.paint(g2d);

        // Drawing border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(6));
        g2d.drawRect(0, 0, width, height);
    }

    // KeyListener methods (keyTyped not needed)
    @Override public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W:
                p1.pressUp();
                break;
            case KeyEvent.VK_S:
                p1.pressDown();
                break;
            case KeyEvent.VK_A:
                p1.pressLeft();
                break;
            case KeyEvent.VK_D:
                p1.pressRight();
                break;
            case KeyEvent.VK_UP:
                p2.pressUp();
                break;
            case KeyEvent.VK_DOWN:
                p2.pressDown();
                break;
            case KeyEvent.VK_LEFT:
                p2.pressLeft();
                break;
            case KeyEvent.VK_RIGHT:
                p2.pressRight();
                break;
            case KeyEvent.VK_SHIFT:
                if (keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) {
                    p1.fireLaser();
                } else {
                    p2.fireLaser();
                }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W:
                p1.releaseUp();
                break;
            case KeyEvent.VK_S:
                p1.releaseDown();
                break;
            case KeyEvent.VK_A:
                p1.releaseLeft();
                break;
            case KeyEvent.VK_D:
                p1.releaseRight();
                break;
            case KeyEvent.VK_UP:
                p2.releaseUp();
                break;
            case KeyEvent.VK_DOWN:
                p2.releaseDown();
                break;
            case KeyEvent.VK_LEFT:
                p2.releaseLeft();
                break;
            case KeyEvent.VK_RIGHT:
                p2.releaseRight();
                break;
        }
    }

    void endMsg(int winner) {
        running = false;
        String[] options = {"Retry", "Quit"};
        int choice = JOptionPane.showOptionDialog(
                this, // Parent frame
                "Player " + winner + " wins!!\nRetry?",
                "Game ended",
                JOptionPane.YES_NO_CANCEL_OPTION, // Types of buttons (probably useless)
                JOptionPane.WARNING_MESSAGE, // Type of message (affects message icon)
                null, // Icon (default icon if null)
                options, // Text in buttons
                options[0] // Initial button selected
        );

        if (choice == 1) {
            System.exit(ABORT);
        } else {
            reset();
        }
    }

    // main method (JFrame initialization)
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Laser Battle!");
        Game laserbattle = new Game();

        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.add(laserbattle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (true) {
            if (laserbattle.running) {
                laserbattle.repaint();
                laserbattle.p1.move();
                laserbattle.p2.move();
            }
            Thread.sleep(1000/60); // 60 fps (approx.)
        }
    }
}
