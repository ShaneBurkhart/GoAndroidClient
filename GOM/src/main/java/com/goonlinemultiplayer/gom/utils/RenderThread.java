package com.goonlinemultiplayer.gom.utils;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.goonlinemultiplayer.gom.views.GameView;

/**
 * Created by shane on 5/14/14.
 */
public class RenderThread extends Thread{
    private static final int FPS = 40;
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

    int count = 0;

    @Override
    public void run() {
        Canvas c;

        while(isRunning){
            c = null;
            if(last_time == 0) last_time = System.currentTimeMillis();

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
                System.out.println(delta_time);
                if (delta_time < FRAME_LENGTH)
                    Thread.sleep(FRAME_LENGTH - delta_time);
                else
                    Thread.sleep(10); // Sleep some length
            } catch (InterruptedException e){}

        }
    }
}
