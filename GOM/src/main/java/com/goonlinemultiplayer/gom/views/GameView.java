package com.goonlinemultiplayer.gom.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.goonlinemultiplayer.gom.utils.GameInterface;
import com.goonlinemultiplayer.gom.utils.RenderThread;

/**
 * Created by shane on 5/14/14.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    RenderThread thread;
    SurfaceHolder holder;
    GameInterface game;
    long last_time = 0;
    long current_time = 0;


    public GameView(Context context, GameInterface game) {
        super(context);
        this.game = game;
        thread = new RenderThread(this);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setWillNotDraw(false);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {}

    @Override
    protected void onDraw(Canvas canvas) {
        if(last_time == 0) last_time = System.currentTimeMillis();

        game.render(canvas);
        current_time = System.currentTimeMillis();
        game.update((last_time - current_time) / 1000f);

        last_time = current_time;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setRunning(false);
        while(retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }
}
