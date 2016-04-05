package edu.depaul.csc472.dli.finalProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class ConfigParameter extends Activity {


    private String method;
    private double ratio;
    private int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_parameter);

        final TextView timeRemain = (TextView) findViewById(R.id.timeRemaining);
        final EditText editCoffeeWeight = (EditText) findViewById(R.id.groudWeight);
        final EditText totalTimeMin = (EditText) findViewById(R.id.editMinus);
        final EditText totalTimeSec = (EditText) findViewById(R.id.editSecs);
        final EditText totalWater = (EditText) findViewById(R.id.editTotalWater);
        final EditText intervalWeight = (EditText) findViewById(R.id.editIntervalWeight);
        final EditText intervalTime = (EditText) findViewById(R.id.editIntervalTime);
        final EditText editRatio = (EditText) findViewById(R.id.ratio);

        final TextView intervalIndex = (TextView) findViewById(R.id.intervalIndex);
        TextView title = (TextView) findViewById(R.id.brewTitle);

        Button addInterval = (Button) findViewById(R.id.addInterval);
        Button brew = (Button) findViewById(R.id.brew);


        Intent brewMethod = getIntent();
        if (brewMethod != null) {
            method = brewMethod.getStringExtra("Method");
        }
        switch (method) {
            case "v60":
                ratio = 16;
                totalTime = 3 * 60;
                break;
            case "kalita":
                ratio = 15;
                totalTime = 3 * 60;
                break;
            case "aero":
                ratio = 11.5;
                totalTime = 1 * 60 + 37;
                break;
            case "chemex":
                ratio = 17;
                totalTime = 4 * 60;
                break;
        }
        int totalTimeM = totalTime / 60;
        int totalTimeS = totalTime % 60;
        if (totalTimeM == 0) {
            totalTimeMin.setText("");
        } else if (totalTimeM == 1) {
            totalTimeMin.setText(totalTimeM + " Minute");
        } else {
            totalTimeMin.setText(totalTimeM + " Minutes");
        }

        if (totalTimeS == 0) {
            totalTimeSec.setText("");
        } else if (totalTimeS == 1) {
            totalTimeSec.setText(totalTimeS + " Second");
        } else {
            totalTimeSec.setText(totalTimeS + " Seconds");
        }
        editRatio.setText(ratio + "");

        title.setText("Set Parameters for " + method);


        TextWatcher coffeeWeightEditWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String weight = editCoffeeWeight.getText().toString();
                if (!weight.equals("")) {
                    String ratio = editRatio.getText().toString();
                    if (!ratio.equals("")) {
                        double computedTotalWater = (new Integer(weight)) * (new Double(ratio));
                        totalWater.setText(computedTotalWater + "");
                    }
                }
            }
        };
        editCoffeeWeight.addTextChangedListener(coffeeWeightEditWatcher);
        editRatio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String weight = editCoffeeWeight.getText().toString();
                if (!weight.equals("")) {
                    String ratio = editRatio.getText().toString();
                    if (!ratio.equals("")) {
                        double computedTotalWater = (new Integer(weight)) * (new Double(ratio));
                        totalWater.setText(computedTotalWater + "");
                    }
                }
            }
        });
        totalTimeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String totalTimeMinString = totalTimeMin.getText().toString();

                String totalTimeSecString = totalTimeSec.getText().toString();
                if (!totalTimeSecString.equals("") || !totalTimeMinString.equals("")) {
                    totalTime = Integer.valueOf(totalTimeMinString) * 60 + Integer.valueOf(totalTimeSecString);
                }
            }
        });
        totalTimeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalTime = Integer.valueOf(totalTimeMin.getText().toString()) * 60 + Integer.valueOf(totalTimeSec.getText().toString());
            }
        });
        final TextView intervalSettingTextView = (TextView) findViewById(R.id.intervalSettingTextView);
        final int[] intervalCount = {1, totalTime};//[interTimes][timeRemaining]
        final ArrayList<Setting> intervalSettings = new ArrayList<>();
        addInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String weightString = intervalWeight.getText().toString();
                String timeString = intervalTime.getText().toString();
                if (weightString.equals("") || timeString.equals("")) {
                    Toast.makeText(ConfigParameter.this, "Please enter the interval weight and time", Toast.LENGTH_SHORT).show();
                } else {
                    int interTime = Integer.valueOf(timeString);
                    double interWeight = Double.valueOf(weightString);
                    int remain = intervalCount[1] - interTime;
                    if (remain < 0) {
                        Toast.makeText(ConfigParameter.this, "Not that much time left for that interval!", Toast.LENGTH_SHORT).show();
                    } else {
                        intervalSettings.add(new Setting(interWeight, interTime));
                        //constant time add, will read in order
                        intervalCount[0]++;
                        intervalSettingTextView.append("Interval " + intervalCount[0] + ": Target: " + weightString + "g, for " + timeString + " secs" + "\n");
                        timeRemain.setText(remain + " secs left");
                        intervalIndex.setText("Interval" + intervalCount[0] + ": ");
                        intervalCount[1] = remain;
                    }
                    intervalWeight.setText("");
                    intervalTime.setText("");
                    intervalWeight.requestFocus();
                }
            }
        });
        brew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent brewConfig = new Intent(ConfigParameter.this, Brew.class);
                brewConfig.putParcelableArrayListExtra("brewConfig", intervalSettings);
                brewConfig.putExtra("method", method);
                brewConfig.putExtra("totalTime", totalTime);
                brewConfig.putExtra("totalWeight", Double.valueOf(totalWater.getText().toString()));

                startActivity(brewConfig);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confit_parameter, menu);
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
}
