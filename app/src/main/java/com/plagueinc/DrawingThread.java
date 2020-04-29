package com.plagueinc;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;

import com.plagueinc.model.ScenceFrame;

public class DrawingThread extends Thread {

    private static final String TAG = DrawingThread.class.getSimpleName();

    private ScenceFrame scenceFrame;

    private SurfaceHolder holder;
    private boolean isRunning = false;
    private long previousTime;
    //private final int fps = 70;

    public DrawingThread(SurfaceHolder holder) {
        this.holder = holder;
        previousTime = System.currentTimeMillis();
    }

    public void setScenceFrame(ScenceFrame scenceFrame){
        this.scenceFrame = scenceFrame;
    }

    public void setRunning(boolean run) {
        isRunning = run;
    }

    @Override
    public void run() {
        int fps = 0;
        long fps_start_time = System.currentTimeMillis();

        while (isRunning) {

            long currentTimeMillis = System.currentTimeMillis();

            if(currentTimeMillis - fps_start_time>=1000){
                Log.d(TAG,"fps : "+fps);
                fps_start_time = currentTimeMillis;
                fps = 0;
            }else{
                fps++;
            }


            long elapsedTimeMs = currentTimeMillis - previousTime;
            long sleepTimeMs = (long) (1000f/ 60 - elapsedTimeMs);



            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();

                if (canvas == null) {
                    Thread.sleep(1);
                    continue;
                }else if (sleepTimeMs > 0){
                    Thread.sleep(sleepTimeMs);
                }

                scenceFrame.doLogic();

                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                scenceFrame.drawOn(canvas);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                    previousTime = System.currentTimeMillis();
                }
            }
        }
    }
}
