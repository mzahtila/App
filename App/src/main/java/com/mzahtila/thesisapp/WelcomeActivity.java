package com.mzahtila.thesisapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.mzahtila.thesisapp.Data.QuizDbHelper;

public class WelcomeActivity extends AppCompatActivity {
    private static final int SPLASH_SHOW_TIME = 2000;
    private QuizDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        mDbHelper = new QuizDbHelper(this);
        Log.d("GroupDetailsActivity", "Welcome. Database empty = " + mDbHelper.isDatabaseEmpty());

        if (!mDbHelper.isDatabaseEmpty()) {
            Log.d("GroupDetailsActivity", "Welcome.. Database empty = " + mDbHelper.isDatabaseEmpty());
            Intent i = new Intent(WelcomeActivity.this, MapActivity.class);
            startActivity(i);
            finish();
        } else
            new BackgroundSplashTask().execute();
    }

    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(WelcomeActivity.this, GroupDetailsActivity.class);
            startActivity(i);
            finish();
        }
    }
}