package com.goonlinemultiplayer.go.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.goonlinemultiplayer.go.GameBoardActivity;
import com.goonlinemultiplayer.go.game.sprites.Piece;
import com.goonlinemultiplayer.go.game.sprites.SpriteInterface;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by shane on 5/26/14.
 */
public class GoGame implements GameInterface {
    private static final String TAG = "GoGame";

    private static final float SCALE_MIN = 1;
    private static final float SCALE_MAX = 5;

    boolean mIsPanning = false;

    GameBoardActivity a;
    Paint p;
    ArrayList<SpriteInterface> sprites;
    float mScale = SCALE_MIN;
    float mXOffset = 0;
    float mYOffset = 0;
    float mScreenX = 0;
    float mScreenY = 0;

    public GoGame(GameBoardActivity a){
        this.a = a;
        p = new Paint();
        sprites = new ArrayList<SpriteInterface>();
    }

    public void setScreen(float x, float y){
        this.mScreenX = x;
        this.mScreenY = y;
    }

    @Override
    public void init() {
        mYOffset = - mScreenY/ 2 + mScreenX / 2;
        Random rand = new Random();
        for(int x = 0; x < Piece.BOARD_GRID_WIDTH; x++)
            for(int y = 0; y < Piece.BOARD_GRID_HEIGHT; y++)
                sprites.add(new Piece(x, y, rand.nextInt(3)));
    }

    @Override
    public void render(Canvas c) {
        p.setColor(Color.GRAY);
        c.scale(mScale, mScale);
        updateOffsets();
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), p);
        for(int i = 0; i < sprites.size(); i++)
            sprites.get(i).draw(c, p, mXOffset, mYOffset);
    }

    @Override
    public void update(float delta) {
        for(int i = 0; i < sprites.size(); i++)
            sprites.get(i).update(delta);
    }

    private final static long DOUBLE_TAP_DURATION = 200;
    private final static int INVALID_POINTER_ID = -1;
    float last_x, last_y;
    long last_time = 0;
    int pointId = INVALID_POINTER_ID;

    @Override
    public boolean touchUp(MotionEvent e) {
        pointId = INVALID_POINTER_ID;
        float x = translateX(e.getX(), mScale);
        float y = translateY(e.getY(), mScale);
        Log.v(TAG, "Touch X: " + x);
        Log.v(TAG, "Touch Y: " + y);
        if(mIsPanning) {
            Log.v(TAG, "Ignoring touch up. Was panning.");
            return false;
        }
        if(x < 0 || x > mScreenX || y < 0 || y > mScreenX) {
            Log.v(TAG, "Touch was off the board.");
            return false;
        }
        for(int i = 0; i < sprites.size(); i++) {
            // TODO Should be a better way to do this.
            // Dummy call to let all update
            sprites.get(i).onTouch(-1, -1);
        }

        for(int i = 0; i < sprites.size(); i++) {
            if(sprites.get(i).onTouch(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        if(System.currentTimeMillis() - last_time < DOUBLE_TAP_DURATION) {
            float oldScale = mScale;

            if(mScale > (SCALE_MAX + SCALE_MIN) / 2)
                mScale = SCALE_MIN;
             else
                mScale = SCALE_MAX;
            if(mScale == SCALE_MAX) {
                // TODO Test and refactor this shit.
                mXOffset = translateX(e.getX(), oldScale) - mScreenX / SCALE_MAX / 2;
                mYOffset = translateY(e.getY(), oldScale) - mScreenY / SCALE_MAX / 2;
                updateOffsets();
            }
            last_time = 0;
        } else {
            last_time = System.currentTimeMillis();
        }
        last_x = x;
        last_y = y;
        mIsPanning = false;
        return true;
    }

    @Override
    public boolean touchMove(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        mXOffset += (last_x - x) / mScale;
        mYOffset += (last_y - y) / mScale;
        mIsPanning = true;

        updateOffsets();

        last_x = x;
        last_y = y;
        return false;
    }

    @Override
    public boolean touchPointerDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean touchPointerUp(MotionEvent e) {
        return false;
    }

    @Override
    public void touchCancel () {
        pointId = INVALID_POINTER_ID;
    }

    @Override
    public void onScale(float mScaleFactor) {
        mScale *= mScaleFactor;
        mScale = Math.max(SCALE_MIN, Math.min(SCALE_MAX, mScale));
    }

    private float translateX(float viewX, float scale){
        return viewX / scale + mXOffset;
    }

    private float translateY(float viewY, float scale){
        if(mScreenX * scale > mScreenY) {
            return viewY / scale + mYOffset;
        } else {
            return viewY / scale - (mScreenY - mScreenX) / 2;
        }
    }

    private void updateOffsets(){
        if(mXOffset < 0) mXOffset = 0;
        if(mXOffset > mScreenX / mScale * (mScale - 1)) mXOffset = mScreenX / mScale * (mScale - 1);

        if(mScreenX * mScale < mScreenY) {
            mYOffset = mScreenX / 2 - mScreenY / mScale / 2;
        } else {
            // Adjust as if the screen is filled with board.
            // The rest is adjusted in the render loop
            if(mYOffset < 0) mYOffset = 0;
            if(mYOffset > mScreenX - mScreenY / mScale) mYOffset = mScreenX - mScreenY / mScale;
        }
    }
}
