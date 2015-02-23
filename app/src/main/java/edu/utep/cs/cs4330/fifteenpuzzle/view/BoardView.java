package edu.utep.cs.cs4330.fifteenpuzzle.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;


import edu.utep.cs.cs4330.fifteenpuzzle.BoardControllerActivity;
import edu.utep.cs.cs4330.fifteenpuzzle.R;
import edu.utep.cs.cs4330.fifteenpuzzle.model.Tile;
import edu.utep.cs.cs4330.fifteenpuzzle.model.TileMovement;

import static edu.utep.cs.cs4330.fifteenpuzzle.model.TileMovement.*;
/**
 *
 *
 * Created by awernick on 2/19/15.
 */
public class BoardView extends View implements View.OnTouchListener
{

    private Paint paint;
    private GestureDetectorCompat motionDetector;
    private LayerDrawable tileDrawable  = (LayerDrawable) getResources().getDrawable(R.drawable.tile_bevel);
    private BoardControllerActivity boardController;
    private final GestureDetector gestureDetector;
    private int boardLength;
    private int boardWidth;
    private int tileSize;
    private Rect[][] tiles;


    public BoardView(Context context)
    {
        this(context, null);
    }


    public BoardView(Context context, AttributeSet attributeSet)
    {
        this(context, attributeSet, 0);
    }


    public BoardView(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);

        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);

        gestureDetector = new GestureDetector(context, new GestureListener());
        setOnTouchListener(this);

        boardController = (BoardControllerActivity) context;
        boardLength = boardController.getBoardLength();
        boardWidth = boardController.getBoardWidth();


        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        tileSize = width > height ? height / boardLength : width / boardWidth;
        tiles = new Rect[boardWidth][boardLength];


        for(int i = 0; i < boardLength; i++)
        {
            for (int j = 0; j < boardWidth; j++)
            {
                System.out.println("SW:" + j * tileSize +  " SH:" + i * tileSize +  " PW:" + (j+1) * tileSize  +  " PH:" + (i+1) * tileSize);

                tiles[j][i] = new Rect(j * tileSize, i * tileSize, (j + 1) * tileSize, (i+1) * tileSize);
            }
        }

        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf");
        paint.setTextSize(tileSize/4);
        paint.setTypeface(tf);
    }

   @Override
   public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
   {
        setMeasuredDimension(tileSize * boardWidth, tileSize * boardLength);
   }

    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawARGB(255,111,164,168);                // Background color
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        Iterable<Tile> tileList = boardController.getBoardTiles();
        Iterator<Tile> tileIterator = tileList.iterator();


        int i = 0;
        int j = 0;

        while(tileIterator.hasNext())
        {
            if(i >=  boardWidth)
            {
                i = 0;
            }

            if( j >= boardLength )
            {
                i++;
                j = 0;
            }


            Rect current = tiles[j][i];
            tileDrawable.setBounds(current.left, current.top, current.right, current.bottom);
            tileDrawable.draw(canvas);




            String value = "";
            Tile currentTile = tileIterator.next();

            if(currentTile.getValue() != -1)                     // Omit the -1 value for the blank square
                value += currentTile.getValue();                 // Store the current tile's value in a string


            float width = paint.measureText(value);              // Pixel measurement of the value string
            Rect bounds = new Rect();
            paint.getTextBounds(value,0,value.length(),bounds);  // Store the measurements in a Rect
            float x = current.centerX() - width/2;
            float y = current.centerY() + bounds.height()/2;
            canvas.drawText(value, x, y, paint);                  // Center the text drawing

            j++;
        }
    }

    /**
     * Helper method to convert between screen x and y coordinates to
     * board tile locations using the on-screen tile dimensions.
     *
     * @param x screen x coordinate
     * @param y screen y coordinate
     *
     * @return Point with converted Board coordinates
     */
    public Point locateBoardPlace(float x, float y)
    {
        return new Point((int)(x/tileSize),(int)(y/tileSize));
    }

    /**
     * Swipe event handler in the upward direction.
     *
     * @param e1 Initial touch event information
     * @param e2 Swipe event information
     */
    public void onSwipeLeft(MotionEvent e1, MotionEvent e2)
    {
       onSwipeEvent(e1.getX(), e1.getY(), LEFT);
    }

    /**
     * Swipe event handler in the upward direction.
     *
     * @param e1 Initial touch event information
     * @param e2 Swipe event information
     */
    public void onSwipeRight(MotionEvent e1, MotionEvent e2)
    {
        onSwipeEvent(e1.getX(), e1.getY(), RIGHT);
    }

    /**
     * Swipe event handler in the upward direction.
     *
     * @param e1 Initial touch event information
     * @param e2 Swipe event information
     */
    public void onSwipeDown(MotionEvent e1, MotionEvent e2)
    {
        onSwipeEvent(e1.getX(), e1.getY(), DOWN);
    }

    /**
     * Swipe event handler in the upward direction.
     *
     * @param e1 Initial touch event information
     * @param e2 Swipe event information
     */
    public void onSwipeUp(MotionEvent e1, MotionEvent e2)
    {
        onSwipeEvent(e1.getX(), e1.getY(), UP);
    }

    /**
     * DRY method for the multiple swipe events. Helper method to
     * reduce repetiton.
     *
     * @param x the swipe event's starting x
     * @param y the swipe event's starting y
     * @param direction direction in which the swipe ocurred
     */
    public void onSwipeEvent(float x, float y, TileMovement direction)
    {
        Point touch = locateBoardPlace(x, y);


        if(touch.x < boardWidth && touch.x >= 0 && touch.y < boardLength && touch.y >= 0)
            boardController.boardTileClicked(touch.x, touch.y, direction);
    }

    /**
     *
     * @param v
     * @param event
     * @return
     */
    public boolean onTouch(View v, MotionEvent event)
    {
        return gestureDetector.onTouchEvent(event);
    }

    /**
     *
     */
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        // Pixel threshold to distinguish between a touch and gesture event
        private static final int SWIPE_DISTANCE_THRESHOLD = 25;
        private static final int SWIPE_VELOCITY_THRESHOLD = 25;

        /**
         *
         * @param e
         * @return
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         *
         * @param e1
         * @param e2
         * @param velocityX
         * @param velocityY
         * @return
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();

            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight(e1, e2);
                else
                    onSwipeLeft(e1, e2);
                return true;
            }


            else if(Math.abs(distanceX) < Math.abs(distanceY) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
            {
                if(distanceY > 0)
                    onSwipeDown(e1, e2);
                else
                    onSwipeUp(e1, e2);
            }

            return false;
        }
    }
}
