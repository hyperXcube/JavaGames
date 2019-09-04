package games.minesweeper;

class Tile {
    int value = 0; // Number of nearby mines, equals 9 if this tile is a mine
    boolean open = false; // Whether this tile has been opened (is visible)
    boolean flagged = false; // Whether a flag has been placed on this tile
}