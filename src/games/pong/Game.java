package games.pong;

import games.pong.Ball.Direction;
import games.util.Sound;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Font;
import java.lang.InterruptedException;

// Main class which controls all the graphics and game loop
public class Game extends JPanel {
    
    Ball ball = new Ball(this, Direction.RANDOM);
    Player player = new Player(this);
    Computer computer = new Computer(this);
    private int playerScore, computerScore = 0;
    
    private Game() {
        setBackground(Color.BLACK);
        addKeyListener(player);
        setFocusable(true);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);

        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);

        ball.paint(g2d);
        player.paint(g2d);
        computer.paint(g2d);
        
        g2d.setFont(new Font("Verdana", Font.BOLD, 20));
        g2d.drawString(String.valueOf(computerScore), getWidth() - 40, getHeight() / 2 - 10);
        g2d.drawString(String.valueOf(playerScore), getWidth() - 40, getHeight() / 2 + 30);
    }

    void computerPoint() throws InterruptedException {
        computerScore++;
        if (computerScore == 10) {
            Sound.play("lose", "pong");
            repaint();
            endMsg("The opponent scored 10 points!", "You lost!");
        }
        ball = new Ball(this, Direction.UP);
        
        Thread.sleep(2000);
    }

    void playerPoint() throws InterruptedException {
        playerScore++;
        if (playerScore == 10) {
            repaint();
            endMsg("You scored 10 points!", "You Won!");
        }
        ball = new Ball(this, Direction.DOWN);

        Thread.sleep(2000);
    }

    private void endMsg(String message, String title) {
        String[] options = {"Retry", "Quit"};
        int choice = JOptionPane.showOptionDialog(
            this, // Parent frame
            message, // Message
            title, // Title
            JOptionPane.YES_NO_CANCEL_OPTION, // Types of buttons (probably useless)
            JOptionPane.WARNING_MESSAGE, // Type of message (affects message icon)
            null, // Icon (default icon if null)
            options, // Text in buttons
            options[0] // Initial button selected
        );

        if (choice == 1) {
            System.exit(ABORT); 
        } else {
            removeKeyListener(player);
            player = new Player(this);
            addKeyListener(player);
            computer = new Computer(this);
            playerScore = 0;
            computerScore = 0;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame f = new JFrame("Pong");
        
        f.setSize(500,800);
        Game pong = new Game();
        f.add(pong);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        while (true) {
            pong.player.move();
            pong.computer.move();
            pong.ball.move();

            pong.repaint();
            Thread.sleep(10);
        }
    }
}