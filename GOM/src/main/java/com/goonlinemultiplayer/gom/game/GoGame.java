package com.goonlinemultiplayer.gom.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.goonlinemultiplayer.gom.GameBoardActivity;
import com.goonlinemultiplayer.gom.game.sprites.Piece;
import com.goonlinemultiplayer.gom.game.sprites.SpriteInterface;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by shane on 5/26/14.
 */
public class GoGame implements GameInterface {
    private static final float SCALE_MIN = 1;
    private static final float SCALE_MAX = 3;

    GameBoardActivity a;
    Paint p;
    ArrayList<SpriteInterface> sprites;
    float scale = SCALE_MIN;
    float x_offset = 0;
    float y_offset = 0;
    float screen_x = 0;
    float screen_y = 0;


    public GoGame(GameBoardActivity a){
        this.a = a;
        p = new Paint();
        sprites = new ArrayList<SpriteInterface>();
    }

    public void setScreen(float x, float y){
        this.screen_x = x;
        this.screen_y = y;
    }

    @Override
    public void init() {
        y_offset = - screen_y / 2 + screen_x / 2;
        Random rand = new Random();
        for(int x = 0; x < Piece.BOARD_GRID_WIDTH; x++)
            for(int y = 0; y < Piece.BOARD_GRID_HEIGHT; y++)
                sprites.add(new Piece(x, y, rand.nextInt() % 2 + 1));
    }

    @Override
    public void render(Canvas c) {
        p.setColor(Color.GRAY);
        c.scale(scale, scale);
        updateOffsets();
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), p);
        for(int i = 0; i < sprites.size(); i++)
            sprites.get(i).draw(c, p, x_offset, y_offset);
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
        return false;
    }

    @Override
    public boolean touchDown(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        if(System.currentTimeMillis() - last_time < DOUBLE_TAP_DURATION) {
            if(scale > (SCALE_MAX + SCALE_MIN) / 2)
                scale = SCALE_MIN;
             else
                scale = SCALE_MAX;
            if(scale == SCALE_MAX) {
                x_offset = e.getX() - screen_x / SCALE_MAX / 2;
                // TODO This doesn't work correctly.
                y_offset = e.getY() - (screen_y - screen_x) / 2 - screen_x / SCALE_MAX / 2;
                updateOffsets();
            }
            last_time = 0;
        } else {
            last_time = System.currentTimeMillis();
        }
        last_x = x;
        last_y = y;
        return true;
    }

    @Override
    public boolean touchMove(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        x_offset += (last_x - x) / scale;
        y_offset += (last_y - y) / scale;

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
    public void onScale(float scaleFactor) {
        scale *= scaleFactor;
        scale = Math.max(SCALE_MIN, Math.min(SCALE_MAX, scale));
    }

    private void updateOffsets(){
        if(x_offset < 0) x_offset = 0;
        if(x_offset > screen_x / scale * (scale - 1)) x_offset = screen_x / scale * (scale - 1);

        if(screen_x * scale < screen_y) {
            y_offset = screen_x / 2 - screen_y / scale / 2;
        } else {
            // Adjust as if the screen is filled with board.
            // The rest is adjusted in the render loop
            if(y_offset < 0) y_offset = 0;
            if(y_offset > screen_x - screen_y / scale) y_offset = screen_x - screen_y / scale;
        }
    }
}
