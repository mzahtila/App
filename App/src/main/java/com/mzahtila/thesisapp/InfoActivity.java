package com.mzahtila.thesisapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); // allow screen rotation

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back)); // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.info_title);      // set Title for Toolbar
        setSupportActionBar(toolbar);   // Setting/replace toolbar as the ActionBar

        // Handling "Back" button functionality
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /* ---------------------------------------------------------------------------------------------
On pressing back button go to Route map
*/
    @Override
    public void onBackPressed() {
        String previousActivity = getIntent().getStringExtra("backToActivity"); // Getting the code number of previous activity: 1-GroupDetailsActivity, 2-MapActivity, 3-MainQuizActivity
        Log.i("InfoActivity", "Previous activity: " + previousActivity);
        Intent i = null;

        if (previousActivity.equals("1")) {
            i = new Intent(InfoActivity.this, GroupDetailsActivity.class);
        }
        else if (previousActivity.equals("2")) {
            i = new Intent(InfoActivity.this, MapActivity.class);
        }
        else if (previousActivity.equals("3")) {
            i = new Intent(InfoActivity.this, QuizMainActivity.class);
        }

        startActivity(i);
    }
}
