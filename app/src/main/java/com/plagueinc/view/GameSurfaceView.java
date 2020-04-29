package com.plagueinc.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.plagueinc.DrawingThread;
import com.plagueinc.model.ScenceFrame;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GameSurfaceView.class.getSimpleName();

    private ScenceFrame scenceFrame;
    private DrawingThread drawingThread;
    public GameSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // do something
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawingThread = new DrawingThread(holder);
        drawingThread.setRunning(true);
        drawingThread.setScenceFrame(scenceFrame);
        drawingThread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        drawingThread.setRunning(false);

        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setScenceFrame(ScenceFrame scenceFrame) {
        this.scenceFrame = scenceFrame;
        if (drawingThread != null) drawingThread.setRunning(false);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

}
