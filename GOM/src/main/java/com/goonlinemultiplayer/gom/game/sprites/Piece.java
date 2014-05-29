package com.goonlinemultiplayer.gom.game.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by shane on 5/26/14.
 */
public class Piece implements SpriteInterface {
    public static final int BOARD_GRID_WIDTH = 19;
    public static final int BOARD_GRID_HEIGHT = 19;

    public int board_x, board_y, player;

    public Piece(int x, int y, int player){
        board_x = x;
        board_y = y;
        this.player = player;
    }

    @Override
    public void draw(Canvas c, Paint p, float x_offset, float y_offset) {
        float piece_width = c.getWidth() / 19f;
        float x = board_x * piece_width - x_offset;
        float y = board_y * piece_width - y_offset;
        if(player == 1)
            p.setColor(Color.WHITE);
        else
            p.setColor(Color.BLACK);
        c.drawRect(x, y, x + piece_width, y + piece_width, p);
    }

    @Override
    public void update(float delta) {
    }
}
