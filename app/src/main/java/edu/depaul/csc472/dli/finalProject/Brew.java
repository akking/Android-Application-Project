package edu.depaul.csc472.dli.finalProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;

public class Brew extends Activity implements BrewCurveViewV2.dataChangedListener{

    String brewMethod;
    TextView methodTitle;
    BrewCurveViewV2 brewCurveView;

    int totalTime;
    double totalWeight;
    ArrayList<Setting> settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew);
        Intent brewConfig = getIntent();
        settings = brewConfig.getParcelableArrayListExtra("brewConfig");
        brewMethod = brewConfig.getStringExtra("method");
        totalTime = brewConfig.getIntExtra("totalTime", 10);
        totalWeight = brewConfig.getDoubleExtra("totalWeight", 10.0);
        methodTitle = (TextView) findViewById(R.id.brew_brewMethod_title);
        methodTitle.setText(brewMethod);



    }

    @Override
    protected void onResume() {
        super.onResume();
        brewCurveView = (BrewCurveViewV2) findViewById(R.id.view);
        brewCurveView.setOnDataUpdate(new BrewCurveViewV2.dataChangedListener() {
            @Override
            public void onDataChanged(int interval, int intervalTime, double intervalWeight) {
                TextView remainTime = (TextView) findViewById(R.id.brew_intervalRemainTime);
                TextView targetWeight = (TextView) findViewById(R.id.brew_intervalTargetWeight);
                TextView intervalIndex = (TextView) findViewById(R.id.brew_intervalIndex);
                remainTime.setText(intervalTime+"");
                targetWeight.setText(intervalWeight+"");
                intervalIndex.setText("Interval " + interval);
            }
        });
        Button start = (Button) findViewById(R.id.brew_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                brewCurveView.getViews(intervalIndex, remainTimeForInterval, targetWeightForInterval);
                brewCurveView.prepareAnimation(settings, totalTime, totalWeight);
                brewCurveView.startAnimation();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Button goCupping = (Button) findViewById(R.id.cupping);
        goCupping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCupping = new Intent(Brew.this,Cupping.class);
                startActivity(goCupping);
            }
        });
        //final TextView intervalIndex = (TextView) findViewById(R.id.brew_intervalIndex);
        //final TextView remainTimeForInterval = (TextView) findViewById(R.id.brew_intervalRemainTime);
        //final TextView targetWeightForInterval = (TextView) findViewById(R.id.brew_intervalTargetWeight);


        //        Button pause = (Button) findViewById(R.id.brew_pause);
        //        pause.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                brewCurveView.pause();
        //            }
        //        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_brew, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChanged(int interval, int intervalTime, double intervalWeight) {
        TextView remainTime = (TextView) findViewById(R.id.brew_intervalRemainTime);
        TextView targetWeight = (TextView) findViewById(R.id.brew_intervalTargetWeight);
        TextView intervalIndex = (TextView) findViewById(R.id.brew_intervalIndex);
        remainTime.setText(intervalTime+"");
        targetWeight.setText(intervalWeight+"");
        intervalIndex.setText("Interval " + interval);
    }
}
