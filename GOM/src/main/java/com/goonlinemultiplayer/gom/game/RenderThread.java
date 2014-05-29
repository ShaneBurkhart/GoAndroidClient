package com.goonlinemultiplayer.gom.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.goonlinemultiplayer.gom.views.GameView;

/**
 * Created by shane on 5/14/14.
 */
public class RenderThread extends Thread{
    private static final int FPS = 30;
    private static final long FRAME_LENGTH = (long) ((1f / FPS) * 1000L);
    private boolean isRunning = false;
    private GameView view;
    private SurfaceHolder holder;
    private long current_time;
    private long last_time = 0;
    private long delta_time = 0;

    public RenderThread(GameView view){
        this.view = view;
        this.holder = view.getHolder();
    }

    public void setRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        Canvas c;
        last_time = System.currentTimeMillis();

        while(isRunning){
            c = null;

            try {
                c = holder.lockCanvas();
                synchronized (holder) {
                    view.postInvalidate();
                }
            } finally {
                if(c != null)
                    view.getHolder().unlockCanvasAndPost(c);
            }

            current_time = System.currentTimeMillis();
            delta_time = current_time - last_time;
            last_time = current_time;
            try {
                if (delta_time < FRAME_LENGTH)
                    Thread.sleep(FRAME_LENGTH - delta_time);
                else
                    Thread.sleep(10); // Sleep some length
            } catch (InterruptedException e){}

        }
    }
}
