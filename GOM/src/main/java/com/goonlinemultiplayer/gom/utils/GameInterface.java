package com.goonlinemultiplayer.gom.utils;

import android.graphics.Canvas;

/**
 * Created by shane on 5/14/14.
 */
public interface GameInterface {
    public void render(Canvas c);
    public void update(float delta);
    public void init();
}
