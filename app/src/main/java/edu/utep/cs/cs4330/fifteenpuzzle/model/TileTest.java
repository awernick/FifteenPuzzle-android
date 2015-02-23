package edu.utep.cs.cs4330.fifteenpuzzle.model;

import junit.framework.TestCase;

public class TileTest extends TestCase
{

    private int value;
    private Tile tile;

    public void setUp() throws Exception {
        super.setUp();

        value = 4;
        tile = new Tile(4);
    }


    public void testGetValue() throws Exception
    {
        // Ensure that tile has the same value it was initialized with.
        assertEquals(value, tile.getValue());
    }


    public void testSetValue() throws Exception
    {
        // Check that tile's value is same as in initialization
        assertEquals(value, tile.getValue());

        // Change tile's value
        int newvalue = 7;
        tile.setValue(newvalue);

        // Assert set value was changed with the specified value
        assertEquals(newvalue, tile.getValue());
    }
}