package edu.depaul.csc472.dli.finalProject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by DLI on 11/20/15.
 */
public class BrewCurveView extends View {

    final private static double SCALE = 2 / 3;
    private int width, height;
    private int x;
    private float y;
    private LinkedList<Pair<Integer, Float>> graphHistory = new LinkedList<>();
    private Paint paint = new Paint();
    private Handler mHandler = new Handler();
    private List<Setting> intervalConfigures;
    private int totalTime;


    //pair contains the interval configs; eg: Pair<int intervalWeight, int intervalTime
    //eg: I want to pour intervalWeight in intervalTime seconds
    //the total intervalTime will be the targetTime and total intervalWeight will be targetWeight


    public BrewCurveView(Context context) {
        super(context);
    }

    public BrewCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void prepareAnimation(List<Setting> intervalsSettings, int totalTime) {
        intervalConfigures = intervalsSettings;
        this.totalTime = totalTime;
    }

    public void startAnimation() {
        paused = false;
        mHandler.post(update);
    }


    public void pause() {
        paused = true;
    }

    private boolean paused;

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TextView intervalIndex = (TextView) findViewById(R.id.brew_intervalIndex);
        //TextView remainTimeForInterval = (TextView) findViewById(R.id.brew_intervalRemainTime);
        //TextView targetWeightForInterval = (TextView) findViewById(R.id.brew_intervalTargetWeight);
        canvas.drawColor(Color.WHITE);
        Log.d("test","in onDraw");
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        Path graphPath = new Path();
        int timeLapsed = 0;
        int intervalCount = 0;
        float yMovePerSec = (float) (height * SCALE / totalTime);
        if (intervalConfigures != null) {
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
                    for (Pair dot : graphHistory) {
                        graphPath.lineTo((int) dot.first, (float) dot.second);
                    }
                    canvas.drawPath(graphPath, paint);
                    //remainTimeForInterval.setText(intervalTime - i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //wait for one second to update
                }
            }
        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        x = 0;
        y = height;
    }


}
