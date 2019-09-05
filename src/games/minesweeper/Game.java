package games.minesweeper;

import games.util.Stopwatch;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game extends JPanel implements MouseListener {
    // Dimensions
    private final int width = 18; // Width of the window in tiles
    private final int height = 14; // Height of the window in tiles
    private final int tileSize = 40; // Side length of tile (in pixels)
    private final int topMargin = 40; // Height of top margin
    private final int windowWidth = width * tileSize;
    private final int windowHeight = height * tileSize;

    // Images (image setup in init)
    private BufferedImage mine = null;
    private BufferedImage flag = null;

    // Misc.
    private final int numOfMines = 40;
    private boolean running = false; // Whether a game has actually started
    private final Stopwatch stopwatch = new Stopwatch();
    private Tile[][] tiles = new Tile[width][height];

    // Colors for all the tiles
    private final Color[] colors = {
            null, // Index 0 will never be accessed as a tile with 0 nearby mines is empty
            new Color(0,0,255),      // 1: blue
            new Color(107,160,35),   // 2: green
            new Color(255,0,0),      // 3: red
            new Color(75,0,130),     // 4: purple
            new Color(90,0,0),       // 5: maroon
            new Color(32, 112, 104), // 6: turquoise
            new Color(0,0,0),        // 7: black
            new Color(128, 128, 128) // 8: gray
    };

    private Game() {
        // Setting up MouseListener
        addMouseListener(this);
        setFocusable(true);

        // Setting up images
        try {
            System.out.println(System.getProperty("user.dir"));
            mine = ImageIO.read(new File("assets\\minesweeper\\mine.png"));
            flag = ImageIO.read(new File("assets\\minesweeper\\flag.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Resetting board
        newBoard();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setFont(new Font("Verdana", Font.BOLD, tileSize - 10));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Drawing title
        g2d.drawString("Minesweeper", 8, topMargin - 8);

        // Drawing stopwatch
        int timeWidth = g2d.getFontMetrics().stringWidth("00:00");
        g2d.drawString(stopwatch.getTime(), (windowWidth - timeWidth) / 2, topMargin - 8);

        // Drawing pause button
        g2d.fillRect(windowWidth - topMargin + 5, 5, topMargin / 2 - 10, topMargin - 10);
        g2d.fillRect(windowWidth - (topMargin / 2) + 5, 5, topMargin / 2 - 10, topMargin - 10);

        // Drawing main board
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile tile = tiles[i][j];

                // Drawing tile BG
                g2d.setColor(Color.BLACK);
                g2d.drawRect(i*tileSize, j*tileSize + topMargin, tileSize, tileSize);
                if (running && stopwatch.isPaused()) {
                    g2d.setColor(Color.GRAY);
                    g2d.fillRect(i*tileSize, j*tileSize + topMargin, tileSize, tileSize);
                } else {
                    if (tile.open) {
                        g2d.setColor(Color.LIGHT_GRAY);
                    } else {
                        g2d.setColor(Color.GRAY);
                    }
                    g2d.fillRect(i * tileSize, j * tileSize + topMargin, tileSize, tileSize);

                    // Drawing contents of tile
                    if (tile.open && tile.value != 0) {
                        if (tile.value == 9) {
                            g2d.drawImage(mine, i * tileSize, j * tileSize + topMargin, tileSize, tileSize, null);
                        } else {
                            g2d.setColor(colors[tile.value]);
                            g2d.drawString(String.valueOf(tile.value), i * tileSize + 8, (j + 1) * tileSize - 8 + topMargin);
                        }
                    } else if (tile.flagged) {
                        g2d.drawImage(flag, i * tileSize, j * tileSize + topMargin, tileSize, tileSize, null);
                    }
                }
            }
        }
    }

    private void newBoard() {
        // Sets up empty board
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Tile();
            }
        }

        // Generates random list of tiles to be mines
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);

        // Fills in mines, then adds +1 value to each nearby square for each mine
        for (int i = 0; i < numOfMines; i++) {
            int num = nums.get(i);
            int tileX = num / height;
            int tileY = num % height;
            tiles[tileX][tileY].value = 9;
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    try {
                        Tile nearbyTile = tiles[tileX + j][tileY + k];
                        if (nearbyTile.value != 9) {
                            nearbyTile.value++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) { }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getY() > topMargin) {
            int tileX = e.getX() / tileSize;
            int tileY = (e.getY() - topMargin) / tileSize;
            Tile tile = tiles[tileX][tileY];

            if (SwingUtilities.isLeftMouseButton(e) && !tile.flagged) /* Can't open a flagged tile */ {
                // Start stopwatch on first click
                if (stopwatch.isPaused()) {
                    stopwatch.start();
                    running = true;
                }

                // Opening tile that was clicked
                openTile(tileX, tileY);

                // Checking if player won
                boolean winCondition = true;
                for (Tile[] column : tiles) {
                    for (Tile t : column) {
                        if (!t.open && t.value != 9) {
                            winCondition = false;
                            break;
                        }
                    }
                }
                if (winCondition) {
                    win();
                }
            } else if (SwingUtilities.isRightMouseButton(e) && !tile.open) /* Can't flag an open tile */ {
                tile.flagged = !tile.flagged;
            }
        } else if (windowWidth - topMargin < e.getX() && e.getX() < windowWidth) /* Pause button is pressed */ {
            System.out.println("Paused.");
            stopwatch.pause();
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Game Paused. Closing this window will resume the game.",
                    "Paused",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    new String[]{"Resume", "Quit"},
                    "Resume"
            );
            if (choice == 1) {
                System.exit(ABORT);
            } else {
                stopwatch.start();
            }
        }
    }

    // Required methods for MouseListener, not needed
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }

    // Opens tile given it's position in the array
    private void openTile(int tileX, int tileY) {
        Tile tile = tiles[tileX][tileY];
        if (!tile.open) {
            tile.open = true;
            if (tile.value == 9) {
                // Clicks bomb
                die();
            } else if (tile.value == 0) {
                // Opening nearby tiles if current tile is empty (tile.value = 0)
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        try {
                            if (!tiles[tileX + i][tileY + j].open) {
                                openTile(tileX + i, tileY + j);
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) { }
                    }
                }
            }
        }
    }

    // Runs when the player wins
    private void win() {
        System.out.println("YOU WON!!");
        endMsg("Good Job!!\nYou completed Minesweeper in " + stopwatch.getTime() + "!","You avoided all the mines!!");
    }

    // Runs when the player dies (clicks a mine)
    private void die() {
        System.out.println("YOU DIED!!");
        endMsg("Better luck next time...", "You blew up!!");
    }

    private void endMsg(String message, String title) {
        running = false;
        stopwatch.pause();
        String[] options = {"Retry", "Quit"};
        int choice = JOptionPane.showOptionDialog(
                this, // Parent frame
                message,
                title,
                JOptionPane.YES_NO_CANCEL_OPTION, // Types of buttons (probably useless)
                JOptionPane.WARNING_MESSAGE, // Type of message (affects message icon)
                null, // Icon (default icon if null)
                options, // Text in buttons
                options[0] // Initial button selected
        );

        if (choice == 1) {
            System.exit(ABORT);
        } else {
            newBoard();
            stopwatch.reset();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Minesweeper");
        Game minesweeper = new Game();

        frame.setSize(minesweeper.windowWidth, minesweeper.windowHeight);
        frame.add(minesweeper);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (true) {
            minesweeper.repaint();
        }
    }
}