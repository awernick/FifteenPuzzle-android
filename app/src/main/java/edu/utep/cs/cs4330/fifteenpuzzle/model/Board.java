package edu.utep.cs.cs4330.fifteenpuzzle.model;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static edu.utep.cs.cs4330.fifteenpuzzle.model.TileMovement.*;

/**
 * Board is a model class that represents the FifteenPuzzle
 * board. A Board object encapsulate a set of 16 Tile objects
 * with unique values ranging from -1 to 15.
 *
 * @author Alan Wernick
 */
public class Board
{
    private ArrayList<BoardListener> eventListeners;
    private Tile[][] tiles; // Tile objects for the puzzle
    private int length; // Board length
    private int width; // Board width


    /** Constructs a Board with a default grid of 16 Tile objects */
    public Board(int length, int width)
    {

        this.length = length;
        this.width = width;

        tiles = new Tile[width][length];

        for(int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles[0].length; j++)
                tiles[i][j] = new Tile(0);

        eventListeners = new ArrayList<BoardListener>();

        scrambleBoard();

    }

    /**
     * Returns the Tile object at the specified x and y coordinates.
     *
     * @param x desired Tile's x coordinate
     * @param y desired Tile's y coordinate
     *
     * @return desired tile
     * @see Tile
     */
    public Tile getTile(int x, int y)
    {
        return tiles[x][y];
    }

    /**
     * Returns a 4x4 Tile array
     *
     * @return tile-based board
     */
    public Iterable<Tile> getTiles()
    {
        List tileList = new ArrayList<Tile>();

        for(int i = 0; i < length; i++)
        {
            for(int j = 0; j < width; j++)
            {
                tileList.add(tiles[j][i]);
            }
        }

        return tileList;
    }


    /**
     * Returns board's length
     *
     * @return length of board
     */
    public int getLength()
    {
        return length;
    }

    /**
     * Returns board's width
     *
     * @return width of board
     */
    public int getWidth()
    {
        return width;
    }


    /**
     * Check if a tile has an adjacent empty space to move to.
     *
     * @param x the tile x coordinate
     * @param y the tile y coordinate
     *
     * @return the direction that the tile can be moved to,
     * null otherwise
     */
    private TileMovement canMove(int x, int y)
    {

        if(x + 1 < width && getTile(x + 1, y).getValue() == -1) {
            System.out.println("Move right");
            return RIGHT;
        }

        else if( x - 1 >= 0 && getTile(x - 1, y).getValue() == -1) {
            System.out.println("Move left");
            return LEFT;
        }

        else if(y + 1 < length && getTile(x, y + 1).getValue() == -1) {
            System.out.println("Move down");
            return DOWN;
        }

        else if( y - 1 >= 0 &&  getTile(x, y - 1).getValue() == -1) {
            System.out.println("Move up");
            return UP;
        }

        else
        {
            System.out.println("Cannot move!");
            return NULL;
        }
    }

    private TileMovement canMove(int x, int y, TileMovement direction)
    {
        if(canMove(x, y) == direction)
            return direction;
        else
            return NULL;
    }


    /**
     * Check if the current board configuration is solved.
     * The board is considered solved if the values of the
     * tiles are in increasing values.
     *
     * @return true if the board's tiles are in increasing
     *         order
     */
    public boolean isBoardSolved()
    {
        Tile previousTile = null;

        for(int i = 0; i < length; i++)
        {
            for(int j = 0; j < width; j++)
            {

                if(previousTile == null)
                    previousTile = tiles[j][i];

                else if(tiles[j][i].getValue() > previousTile.getValue())
                    previousTile = tiles[j][i];

                else if(previousTile.getValue() == ((tiles.length * tiles[0].length) - 1) && tiles[j][i].getValue() == -1)
                    previousTile = tiles[j][i];

                else
                    return false;
            }
        }

        return true;
    }

    /**
     * Solve the current board configuration.
     */
    public void solve()
    {
        int counter = 1;

        for(int i = 0; i < tiles.length; i++)
        {
            for(int j = 0; j < tiles[0].length; j++)
            {
                if(counter == length * width)
                    counter = -1;

                tiles[j][i].setValue(counter++);
            }
        }

        notifyBoardChange();
    }


    /**
     * Generate a new game board and scramble it.
     * -1 value set to represent the blank tile.
     */
    public void scrambleBoard()
    {
        Random generator = new Random();

        ArrayList<Integer> numbersTaken = new ArrayList<Integer>(); // List of the numbers that have already been set in the model.Tile's

        int num; // Temporary variable to store random numbers

        for(int i = 0; i < tiles.length; i++)
        {
            for(int j = 0; j < tiles[0].length; j++)
            {
                do { num = generator.nextInt(length * width) + 1; } while(numbersTaken.contains(num)); // Generate random numbers (0 inclusive, 15 exclusive) while they are already used.

                if(num == length * width)
                    tiles[i][j].setValue(-1); // Generate the blank puzzle piece if the current rand number is 16
                else
                    tiles[i][j].setValue(num); // Set the current tile's value to the random number

                numbersTaken.add(num); // Add to the list of numbers taken
            }
        }

        notifyBoardChange();
    }


    /**
     * Update the board based on the user's action.
     * First check if the tile at the specified x and y
     * has a valid movement. If it does, update the
     * temporary board and check if the movement is
     * valid. If the movement is valid, update the
     * current board, and set the flag to draw the
     * board.
     *
     * @param x tile's x coordinate
     * @param y tile's y coordinate
     */
    public void tileClicked( int x, int y )
    {
        TileMovement direction = canMove(x, y);
        handleTileMovement(x, y,  direction);
    }

    public void tileClicked( int x, int y, TileMovement dir)
    {
       TileMovement direction = canMove(x, y, dir);
       handleTileMovement(x, y, direction);
    }

    private void handleTileMovement(int x, int y, TileMovement direction)
    {
        if( direction == NULL )
            return;

        System.out.println("Handle");

        switch (direction)
        {
            case DOWN:
                moveTileDown(x, y);
                break;
            case UP:
                moveTileUp(x, y);
                break;
            case LEFT:
                moveTileLeft(x, y);
                break;
            case RIGHT:
                moveTileRight(x, y);
                break;
        }

        notifyBoardChange();

        if(isBoardSolved())
            notifyBoardSolved();
    }

    /**
     * Slide a tile to the left
     *
     * @param x clicked tile's x coordinate
     * @param y clicked tile's y coordinate
     */
    private void moveTileLeft( int x, int y )
    {
        swapTile( x, y, x - 1, y );
    }

    /**
     * Slide a tile to the right
     *
     * @param x clicked tile's x coordinate
     * @param y clicked tile's y coordinate
     */
    private void moveTileRight( int x, int y )
    {
        swapTile( x, y,  x + 1, y );
    }

    /**
     * Slide a tile up
     *
     * @param x clicked tile's x coordinate
     * @param y clicked tile's y coordinate
     */
    private void moveTileUp( int x, int y )
    {
        swapTile( x, y, x, y - 1 );
    }

    /**
     * Slide a tile down
     *
     * @param x clicked tile's x coordinate
     * @param y clicked tile's y coordinate
     */
    private void moveTileDown( int x, int y )
    {
        swapTile( x, y, x, y + 1 );
    }

    /**
     * Swap two tile's positions (values)
     *
     * @param x1 current tile's x coordinate
     * @param y1 current tile's y coordinate
     * @param x2 target tile's x coordinate
     * @param y2 target tile's y coordinate
     */
    private void swapTile( int x1, int y1, int x2, int y2 )
    {
        Tile current = getTile(x1, y1);
        Tile swap = getTile(x2, y2);

        int temp = current.getValue();
        current.setValue(swap.getValue());
        swap.setValue(temp);
    }

    /**
     * Register a listener for board changes such as moved tiles
     * or board being solved
     *
     * @param listener event handler
     */

    public void addBoardListener(BoardListener listener)
    {
        eventListeners.add(listener);
    }

    /**
     * Notify registered listeners of a board change.
     * Triggered when a tile is moved or when it has
     * been reinitialized.
     */
    private void notifyBoardChange()
    {
        for(BoardListener listener : eventListeners)
        {
            listener.boardChanged();

            if(isBoardSolved())
                listener.boardSolved();
        }
    }

    /**
     * Notify registered listeners that the current board
     * configuration has been solved.
     */
    private void notifyBoardSolved()
    {
        for(BoardListener listener : eventListeners)
        {
            listener.boardSolved();
        }
    }
}
