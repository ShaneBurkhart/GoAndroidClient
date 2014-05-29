package com.goonlinemultiplayer.gom.game.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by shane on 5/26/14.
 */
public interface SpriteInterface {
    public void draw(Canvas c, Paint p, float x_offset, float y_offset);
    public void update(float delta);
}
