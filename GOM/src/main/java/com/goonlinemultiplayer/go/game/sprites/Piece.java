package com.goonlinemultiplayer.go.game.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by shane on 5/26/14.
 */
public class Piece implements SpriteInterface {
    private static final String TAG = "Piece";

    public static final int BOARD_GRID_WIDTH = 19;
    public static final int BOARD_GRID_HEIGHT = 19;

    public int mBoardX, mBoardY, mPlayer;

    private float mPieceWidth;
    private boolean mIsSelected = false;

    public Piece(int x, int y, int player){
        this.mBoardX = x;
        this.mBoardY = y;
        this.mPlayer = player;
        Log.v(TAG, "" + player);
    }

    @Override
    public void draw(Canvas c, Paint p, float x_offset, float y_offset) {
        this.mPieceWidth = c.getWidth() / 19f;
        float x = mBoardX * mPieceWidth - x_offset;
        float y = mBoardY * mPieceWidth - y_offset;
        // TODO only draw if on screen
        if(mPlayer == 1)
            p.setColor(Color.WHITE);
        else if(mPlayer == 2)
            p.setColor(Color.BLACK);
        else
            p.setColor(Color.YELLOW);
        if(mIsSelected)
            p.setColor(Color.GREEN);
        c.drawRect(x, y, x + mPieceWidth, y + mPieceWidth, p);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean onTouch(float x, float y) {
        mIsSelected = false;
        if(mPlayer != 0) {
            Log.v(TAG, "Player Id: " + mPlayer);
            return false;
        }
        if(x <= (mBoardX + 1) * mPieceWidth && x >= mBoardX * mPieceWidth
                && y <= (mBoardY + 1) * mPieceWidth && y >= mBoardY * mPieceWidth) {
            mIsSelected = true;
            return true;
        }
        return false;
    }
}
