package com.goonlinemultiplayer.go.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.goonlinemultiplayer.go.game.GameInterface;
import com.goonlinemultiplayer.go.game.GoGame;
import com.goonlinemultiplayer.go.game.RenderThread;

/**
 * Created by shane on 5/14/14.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    RenderThread thread;
    SurfaceHolder holder;
    GameInterface game;
    ScaleGestureDetector scaleDetector;
    long last_time = 0;
    long current_time = 0;


    public GameView(Context context, GameInterface game) {
        super(context);
        this.game = game;
        this.scaleDetector = new ScaleGestureDetector(context, new CanvasScaleDetector());
        thread = new RenderThread(this);
        holder = getHolder();
        holder.addCallback(this);
        this.setOnTouchListener(new CanvasTouchListener(this.game));
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
        if(last_time == 0){
            last_time = System.currentTimeMillis();
            ((GoGame) game).setScreen(canvas.getWidth(), canvas.getHeight());
            game.init();
        }

        game.render(canvas);
        current_time = System.currentTimeMillis();
        game.update((current_time - last_time) / 1000f);

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

    private class CanvasScaleDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            game.onScale(detector.getScaleFactor());
            return true;
        }
    }

    private class CanvasTouchListener implements OnTouchListener {
        private GameInterface g;

        public CanvasTouchListener(GameInterface g){
            this.g = g;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            scaleDetector.onTouchEvent(motionEvent);
            int action = motionEvent.getActionMasked();

            switch(action) {
                case MotionEvent.ACTION_DOWN:
                    return game.touchDown(motionEvent);
                case MotionEvent.ACTION_UP:
                    return game.touchUp(motionEvent);
                case MotionEvent.ACTION_MOVE:
                    return game.touchMove(motionEvent);
                case MotionEvent.ACTION_CANCEL:
                    game.touchCancel();
                    return true;
                case MotionEvent.ACTION_POINTER_DOWN:
                    return game.touchPointerDown(motionEvent);
                case MotionEvent.ACTION_POINTER_UP:
                    return game.touchPointerUp(motionEvent);
            }
            return false;
        }
    }
}
