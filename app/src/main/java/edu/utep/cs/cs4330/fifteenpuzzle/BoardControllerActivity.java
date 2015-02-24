package edu.utep.cs.cs4330.fifteenpuzzle;

import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import edu.utep.cs.cs4330.fifteenpuzzle.model.Board;
import edu.utep.cs.cs4330.fifteenpuzzle.model.BoardListener;
import edu.utep.cs.cs4330.fifteenpuzzle.model.Tile;
import edu.utep.cs.cs4330.fifteenpuzzle.model.TileMovement;
import edu.utep.cs.cs4330.fifteenpuzzle.view.BoardView;


/**
 * Activity that acts as the controller for the FifteenPuzzle game.
 * This controller coordinates the events between the View and the Model
 * while driving the game's logic.
 *
 * @author Alan Wernick
 */
public class BoardControllerActivity extends ActionBarActivity implements BoardListener {

    private BoardView boardView;
    private Board board;
    private int userMoves;
    private TextView movesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Start Activity as FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // Fetch Intent Extra values for Board difficulty
        Bundle extra = getIntent().getExtras();
        BoardDifficulty difficulty = extra != null
                                     ? (BoardDifficulty) extra.getSerializable("difficulty")
                                     : BoardDifficulty.EASY;

        int boardSize = 0;

        switch (difficulty)
        {
            case HARD:
                boardSize = 6;
                break;

            case MEDIUM:
                boardSize = 4;
                break;

            case EASY:
            default:
                boardSize = 2;
                break;

        }

        // Instantiate board and register the activity to listen to board events.
        board = new Board(boardSize, boardSize);
        board.addBoardListener(this);

        // Set Content View and hide ActionBar
        setContentView(R.layout.activity_board_controller);
        getSupportActionBar().hide();

        // Set OpenSans as default typeface
        setActivityTypeface();

        // Fetch BoardView to invalidate upon board change
        boardView = (BoardView) findViewById(R.id.board_view);
        boardView.requestFocus();

        // Clear User moves
        userMoves = 0;

    }

    /**
     * Sets OpenSans as the default typeface for the view.
     */
    private void setActivityTypeface()
    {
        // Open OpenSans Semibold
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Semibold.ttf");

        // Fetch View's buttons and textviews
        Button newButton = (Button) findViewById(R.id.new_board_button);
        Button solveButton = (Button) findViewById(R.id.solve_board_button);

        TextView movesLabelTextView = (TextView) findViewById(R.id.moves_label_textview);
        movesTextView = (TextView) findViewById(R.id.moves_textview);


        // Set OpenSans Semibold as the view's typeface
        newButton.setTypeface(tf);
        solveButton.setTypeface(tf);
        movesLabelTextView.setTypeface(tf);

        // Add Elevation for Lollipop devices (not supported pre-lollipop)
        ViewCompat.setElevation(newButton, 40);
        ViewCompat.setElevation(solveButton, 40);


        // Set OpenSans Regular for the Moves counter
        tf = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf");
        movesTextView.setTypeface(tf);
    }

    /**
     *  Update game values and view based on the board's
     *  changes.
     */
    @Override
    public void boardChanged()
    {
        userMoves++;
        movesTextView.setText(""+userMoves);
        boardView.invalidate();
    }

    /**
     * Notify the user that the board has been solved.
     */
    @Override
    public void boardSolved()
    {
        Toast.makeText(this, "The Board was solved!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 'Solve' button's action.
     * Solves the current board's configuration.
     *
     * @param view 'solve' button
     */
    public void solveBoardConfiguration(View view)
    {
        board.solve();
    }

    /**
     * 'New' button's action.
     * Creates a new board configuration.
     *
     * @param view 'new' button
     */
    public void newBoardConfiguration(View view)
    {
        userMoves = -1;
        board.scrambleBoard();
    }

    /**
     * Fetch the current board's length.
     *
     * @return board's length.
     */
    public int getBoardLength()
    {
       return board.getLength();
    }

    /**
     * Fetch the current board's width.
     *
     * @return board's width
     */
    public int getBoardWidth()
    {
        return board.getWidth();
    }

    /**
     * Fetch current board's tile configuration.
     *
     * @return board's tiles.
     */
    public Iterable<Tile> getBoardTiles()
    {
        return board.getTiles();
    }

    /**
     * Notify board when a tile was swiped, including
     * direction.
     *
     * @param x swipe event's initial x
     * @param y swipe event's initial y
     * @param direction swipe event's direction
     */
    public void boardTileClicked(int x, int y, TileMovement direction)
    {
        board.tileClicked(x, y, direction);
    }
}
