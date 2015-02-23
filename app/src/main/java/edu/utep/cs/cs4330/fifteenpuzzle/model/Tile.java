package edu.utep.cs.cs4330.fifteenpuzzle.model;

/**
 * Tile is a model class that represents a block in the
 * FifteenPuzzle board.
 *
 * Each Tile can only hold one value at a given time.
 *
 * @author Alan Wernick
 */
public class Tile
{
    private int value; // Hold a number from -1 to 15


    /**
     * Constructs a Tile object that will hold the given value.
     *
     * @param value value for the FifteenPuzzle game
     */
    public Tile(int value)
    {
        this.value = value;
    }

    /**
     * Returns the tile's value
     *
     * @return tile's value
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Sets the tile's value
     *
     * @param value tile's desired value
     */
    public void setValue(int value)
    {
        this.value = value;
    }
}
