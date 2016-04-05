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

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by DLI on 11/23/15.
 */
public class BrewCurveViewV2 extends View {
    private float x, y, dx, dy;
    private int width, height;
    private boolean paused = true;
    private Handler mHandler = new Handler();
    private Paint paint = new Paint();
    private ArrayList<Setting> settings;
    boolean started = false;
    private dataChangedListener listener;

    public interface dataChangedListener {
        public void onDataChanged(int interval, int intervalTime, double intervalWeight);
    }

    public void setOnDataUpdate(dataChangedListener callback) {
        this.listener = callback;
    }


    public BrewCurveViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrewCurveViewV2(Context context) {
        super(context);
    }

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public void pauseAnimation() {
        paused = true;
    }

    public void startAnimation() {
        paused = false;
        started = true;
        mHandler.post(update);
    }

    private int intervalCount = 0;
    private int[] intervalTimeSettings;
    private double[] intervalWeightSettings;
    private int settingSize;
    private double totalWeight;
    private int totalTime;

    public void prepareAnimation(ArrayList<Setting> settings, int totalTime, double totalWeight) {
        this.settings = settings;
        settingSize = settings.size();
        this.totalTime = totalTime;
        intervalTimeSettings = new int[settingSize + 1];
        this.totalWeight=totalWeight;
        intervalWeightSettings = new double[settingSize + 1];
        int accmulateTime = 0;
        double acculateWeight = 0;
        for (Setting setting : settings) {
            accmulateTime += setting.interTime;
            acculateWeight += setting.intervalWeight;
            intervalTimeSettings[intervalCount] = accmulateTime;
            intervalWeightSettings[intervalCount] = acculateWeight;
            intervalCount++;
        }
        if (accmulateTime < totalTime) {
            //setting not used up
            intervalTimeSettings[settingSize] = totalTime;
        }
        if(acculateWeight<totalWeight){
            intervalWeightSettings[settingSize] =totalWeight;
        }
        xIncresePerSec = width / ((float) totalTime);
        yIncreasePerSeec = (float) (height / totalWeight);
        Y_SCALE = totalWeight / height;

    }

    double Y_SCALE;

    float xIncresePerSec, yIncreasePerSeec;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        x = 0;
        y = height;
        //start at (o, height)
    }

    private LinkedList<Pair<Float, Float>> graphHistory = new LinkedList<>();
    Path path = new Path();
    int inThisInterval = 1;
    int time = 0;
    float totalIntervalWeight;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLUE);
        if (started) {
            time++;
            x = x + xIncresePerSec;
            float yIncreasePerSecInThisInterval;
            if (inThisInterval <= settingSize) {
                Setting settingForThisInterval = settings.get(inThisInterval - 1);
                totalIntervalWeight += settingForThisInterval.getIntervalWeight();
                yIncreasePerSecInThisInterval = (float) ((settingForThisInterval.getIntervalWeight() / settingForThisInterval.getInterTime()) * Y_SCALE);
            } else {
               yIncreasePerSecInThisInterval = (float) ((totalWeight - totalIntervalWeight)/(totalTime-time)*Y_SCALE);
            }
            y = y - yIncreasePerSecInThisInterval;
            graphHistory.add(new Pair<>(x, y));
            path.moveTo(0, height);
            for (Pair p : graphHistory) {
                path.lineTo((float) p.first, (float) p.second);
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            canvas.drawPath(path, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            listener.onDataChanged(inThisInterval, intervalTimeSettings[inThisInterval - 1] - time, intervalWeightSettings[inThisInterval - 1]);
            if (time == intervalTimeSettings[inThisInterval - 1]) {
                //move to next interval
                if (inThisInterval <= settingSize) {
                    inThisInterval++;
                } else {
                    paused = true;
                }
            }
            Log.d("debug", "did it!" + "x: " + x + " y: " + y);
            if (!paused) {
                mHandler.postDelayed(update, 1000);
            }
        }
    }
}
