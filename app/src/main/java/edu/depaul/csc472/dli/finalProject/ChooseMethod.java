package edu.depaul.csc472.dli.finalProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class ChooseMethod extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_method);

        final ImageView v60Method = (ImageView) findViewById(R.id.v60);
        final ImageView kalitaMethod = (ImageView) findViewById(R.id.kalita);
        final ImageView aeropressMethod = (ImageView) findViewById(R.id.aeropress);
        final ImageView chemexMethod = (ImageView) findViewById(R.id.chemex);


        class ChooseMethodListener implements View.OnClickListener {
            String method;

            public ChooseMethodListener(String method) {
                this.method = method;
            }

            @Override
            public void onClick(View v) {
                Intent setup = new Intent(ChooseMethod.this, ConfigParameter.class);
                setup.putExtra("Method", method);
                startActivity(setup);
            }
        }

        v60Method.setOnClickListener(new ChooseMethodListener("v60"));
        kalitaMethod.setOnClickListener(new ChooseMethodListener("kalita"));
        aeropressMethod.setOnClickListener(new ChooseMethodListener("aero"));
        chemexMethod.setOnClickListener(new ChooseMethodListener("chemex"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_method, menu);
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
