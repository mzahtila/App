package com.mzahtila.thesisapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mzahtila.thesisapp.Data.QuizDbHelper;
import com.mzahtila.thesisapp.Managers.QuizScoreManager;

public class AnswersMainActivity extends AppCompatActivity {
    String answerChk1,
            answerChk2,
            answerChk3,
            answerChk4;
    private QuizDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); // allow screen rotation

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                  // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back)); // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.answers_main_title); // set Title for Toolbar
        setSupportActionBar(toolbar);    // Setting/replace toolbar as the ActionBar

        // Handling "Back" button functionality
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnswersMainActivity.this, QuizMainActivity.class); // returns to preceding activity
                startActivity(i);
            }
        });

        // Create database helper
        mDbHelper = new QuizDbHelper(this);
        showAnswers();
    }

    private void showAnswers() {
        String[] answered = mDbHelper.getAnswers();
        answerChk1 = answered[0];
        answerChk2 = answered[1];
        answerChk3 = answered[2];
        answerChk4 = answered[3];
        String noAnswer = QuizScoreManager.setNoAnswer();

        if (answerChk1.equals(noAnswer)) {
            answerChk1 = getString(R.string.answer_missing);
        }
        if (answerChk2.equals(noAnswer)) {
            answerChk2 = getString(R.string.answer_missing);
        }
        if (answerChk3.equals(noAnswer)) {
            answerChk3 = getString(R.string.answer_missing);
        }
        if (answerChk4.equals(noAnswer)) {
            answerChk4 = getString(R.string.answer_missing);
        }

        TextView answ1TextView = (TextView) findViewById(R.id.answer1);
        TextView answ2TextView = (TextView) findViewById(R.id.answer2);
        TextView answ3TextView = (TextView) findViewById(R.id.answer3);
        TextView answ4TextView = (TextView) findViewById(R.id.answer4);

        answ1TextView.setText(answerChk1);
        answ2TextView.setText(answerChk2);
        answ3TextView.setText(answerChk3);
        answ4TextView.setText(answerChk4);
    }

    // Activity's overrided method used to set the menu file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.answers_main, menu);
        return true;
    }

    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks
        // on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        Intent i = new Intent(AnswersMainActivity.this, AnswersEditActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AnswersMainActivity.this, QuizMainActivity.class);
        startActivity(i);
    }
}
