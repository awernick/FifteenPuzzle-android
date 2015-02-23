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


public class BoardControllerActivity extends ActionBarActivity implements BoardListener {

    private BoardView boardView;
    private Board board;
    private int userMoves;
    private TextView movesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

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

        board = new Board(boardSize, boardSize);
        //boardView = new BoardView(this);


        board.addBoardListener(this);

        setContentView(R.layout.activity_board_controller);
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Semibold.ttf");
//        Button startButton = (Button)findViewById(R.id.start_button);
//        startButton.setTypeface(tf);
        Button newButton = (Button) findViewById(R.id.new_board_button);
        Button solveButton = (Button) findViewById(R.id.solve_board_button);
        TextView movesLabelTextView = (TextView) findViewById(R.id.moves_label_textview);
        movesTextView = (TextView) findViewById(R.id.moves_textview);

        newButton.setTypeface(tf);
        solveButton.setTypeface(tf);
        solveButton.getPaint().setAntiAlias(true);
        movesLabelTextView.setTypeface(tf);

        tf = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf");
        movesTextView.setTypeface(tf);

        ViewCompat.setElevation(newButton, 40);
        ViewCompat.setElevation(solveButton, 40);

        boardView = (BoardView) findViewById(R.id.board_view);
        boardView.requestFocus();

        userMoves = 0;

        getSupportActionBar().hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public int getBoardLength()
    {
       return board.getLength();
    }

    public int getBoardWidth()
    {
        return board.getWidth();
    }

    public void boardTileClicked(int x, int y, TileMovement direction)
    {
        board.tileClicked(x, y, direction);
    }

    public Iterable<Tile> getBoardTiles()
    {
       return board.getTiles();
    }

    @Override
    public void boardChanged()
    {
        userMoves++;
        movesTextView.setText(""+userMoves);
        boardView.invalidate();
    }

    @Override
    public void boardSolved()
    {
        Toast.makeText(this, "The Board was solved!", Toast.LENGTH_SHORT).show();
    }

    public void solveBoardConfiguration(View view)
    {
        board.solve();
    }

    public void newBoardConfiguration(View view)
    {
        userMoves = -1;
        board.scrambleBoard();
    }

    public void backButtonClicked(View view)
    {
        this.finish();
    }
}
