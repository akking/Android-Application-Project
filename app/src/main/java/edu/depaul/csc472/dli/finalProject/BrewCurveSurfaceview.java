package edu.depaul.csc472.dli.finalProject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by DLI on 11/22/15.
 */
public class BrewCurveSurfaceview extends SurfaceView implements SurfaceHolder.Callback {
    TextView intervalIndex;
    TextView remainTimeForInterval;
    TextView targetWeightForInterval;

    final private static double SCALE = 2 / 3.0;
    private int x;
    private float y;
    private LinkedList<Pair<Integer, Float>> graphHistory = new LinkedList<>();
    private Paint paint = new Paint();
    private List<Setting> intervalConfigures;
    private int totalTime;


    private int width, height;
    private boolean surfaceAvailble;
    private SurfaceHolder holder;

    public BrewCurveSurfaceview(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    public BrewCurveSurfaceview(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    public void prepareAnimation(List<Setting> intervalsSettings, int totalTime) {
        intervalConfigures = intervalsSettings;
        this.totalTime = totalTime;
    }

    public void getViews(TextView tv1, TextView textview2, TextView textview3) {
        this.intervalIndex = tv1;
        this.remainTimeForInterval = textview2;
        this.targetWeightForInterval = textview3;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceAvailble = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        done = true;
    }

    private boolean done;

    public void startAnimation() {
        done = false;
        if (surfaceAvailble)
            startRenderingThread();
    }

    private void startRenderingThread() {
        new Thread(new Runnable() {
            public void run() {
                while (!done) {
                    Canvas c = null;
                    c = holder.lockCanvas();
                    synchronized (holder) {
                        doDraw(c);
                    }
                }
            }
        }).start();

    }


    protected void doDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        Path graphPath = new Path();
        int timeLapsed = 0;
        int intervalCount = 0;
        float yMovePerSec = (float) (height * SCALE / totalTime);
        y = height;
        for (Setting setting : intervalConfigures) {
            intervalCount++;
            int intervalTime = setting.interTime;

            //remainTimeForInterval.setText(intervalTime + "");
            double intervalWeight = setting.intervalWeight;
            //targetWeightForInterval.setText(intervalWeight + "");
            //intervalIndex.setText(intervalCount + "");
            for (int i = 1; i <= intervalTime; i++) {
                timeLapsed++;
                x = timeLapsed;
                y -= yMovePerSec;
                graphHistory.add(new Pair(x, y));
                //for (Pair dot : graphHistory) {
                graphPath.lineTo(x, y);
                //}
                Log.d("test", "in onDraw");
                canvas.drawText("x:" + x + "    y: " + y, 20, 40, paint);
                canvas.drawPath(graphPath, paint);
                //remainTimeForInterval.setText(intervalTime - i);
                //wait for one second to update

                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                canvas = holder.lockCanvas();
            }
        }


    }
}
