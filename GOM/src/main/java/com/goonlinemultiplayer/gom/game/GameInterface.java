package com.goonlinemultiplayer.gom.game;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by shane on 5/14/14.
 */
public interface GameInterface {
    public void render(Canvas c);
    public void update(float delta);
    public void init();

    public boolean touchUp(MotionEvent e);
    public boolean touchDown(MotionEvent e);
    public boolean touchMove(MotionEvent e);
    public boolean touchPointerDown(MotionEvent e);
    public boolean touchPointerUp(MotionEvent e);
    public void touchCancel();
    public void onScale(float scaleFactor);
}
