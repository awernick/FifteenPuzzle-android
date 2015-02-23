package edu.utep.cs.cs4330.fifteenpuzzle.model;
/**
 *
 * Interface to handle BoardEvents such as change, and solved
 *
 * This interface is used to implement the Observer design pattern.
 * By firing BoardEvent in my BoardController, I can notify listeners of
 * that the board has been changed, or solved.
 *
 * This pattern was chosen to reduce coupling between the controller
 * and the view of the game. This means that the controller and models
 * for this game can be reused with another view implementation without
 * having to modify them.
 *
 * @author Alan Wernick
 */
public interface BoardListener
{
        public void boardChanged();
        public void boardSolved();
}
