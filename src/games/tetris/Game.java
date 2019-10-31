package games.tetris;

import static games.tetris.Constants.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class Game extends JPanel implements KeyListener {
    private Block activeBlock; // Block that is in motion
    final HashMap<Rectangle, Color> bottomTiles = new HashMap<>(); // All of the tiles on the bottom
    private Timer timer;

    // Constructors
    Game() {
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        reset();
    }

    // Resets game board
    private void reset() {
        activeBlock = null;
        bottomTiles.clear();
        activeBlock = new Block(this);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                blockDown();
                if (!bottomTiles.isEmpty()) checkLine();
            }
        };
        timer.schedule(task, 0, 500);
    }

    // Moves block down, checking to see if the active block should be replaced. Returns result of moveDown
    private boolean blockDown()  {
        boolean moveSuccessful = activeBlock.moveDown();
        if (!moveSuccessful) {
            activeBlock.deactivate();
            activeBlock = new Block(this);
        }
        return moveSuccessful;
    }

    //Comparator to sort the blocks in bottom tiles by y value
    private Comparator<Entry<Rectangle, Color>> tileComparator = Comparator.comparingInt(e -> e.getKey().y * (-1));

    //Checks if there is a line of blocks in bottomTiles to be cleared
    private void checkLine() {
        List<Entry<Rectangle, Color>> bottomTilesCopy = new ArrayList<>(bottomTiles.entrySet());
        bottomTilesCopy.sort(tileComparator);
        int yLevel = bottomTilesCopy.get(0).getKey().y;
        int yLevelTileCount = 0;
        for (Entry<Rectangle, Color> e : bottomTilesCopy) {
            if (e.getKey().y == yLevel) yLevelTileCount++;
            else {
                yLevelTileCount = 1;
                yLevel = e.getKey().y;
            }
            if (yLevelTileCount == 10) {
                clearLine(e.getKey().y);
                yLevelTileCount = 0;
            }
        }
    }

    //Clears all blocks with a certain y value
    private void clearLine(int yValue) {
        bottomTiles.entrySet().removeIf(e -> e.getKey().y == yValue);
        Iterator<Entry<Rectangle, Color>> iterator = bottomTiles.entrySet().iterator();
        HashMap<Rectangle, Color> temp = new HashMap<>();
        while (iterator.hasNext()) {
            Entry<Rectangle, Color> entry = iterator.next();
            if (entry.getKey().y < yValue) {
                iterator.remove();
                entry.getKey().y += TILE;
                temp.put(entry.getKey(), entry.getValue());
            }
        }
        for (Entry<Rectangle, Color> e: temp.entrySet()) {
            bottomTiles.put(e.getKey(), e.getValue());
        }
    }

    // Display lose message
    void lose() {
        timer.cancel();
        int choice = JOptionPane.showOptionDialog(
                this, // Parent frame
                "You lost!",
                "You lost!",
                JOptionPane.YES_NO_CANCEL_OPTION, // Types of buttons (probably useless)
                JOptionPane.WARNING_MESSAGE, // Type of message (affects message icon)
                null, // Icon (default icon if null)
                new String[]{"Retry", "Quit"}, // Text in buttons
                "Retry" // Initial button selected
        );

        if (choice == 1) System.exit(ABORT);
        else reset();
    }

    // Paints board (called every frame)
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
            case KeyEvent.VK_UP -> {
                if (activeBlock.canRotate()) activeBlock.rotateClockwise();
            }
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
