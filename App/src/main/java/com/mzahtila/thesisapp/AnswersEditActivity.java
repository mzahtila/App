package com.mzahtila.thesisapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mzahtila.thesisapp.Data.GroupContract;
import com.mzahtila.thesisapp.Data.QuizDbHelper;

public class AnswersEditActivity extends AppCompatActivity {
    String answerChk1,
            answerChk2,
            answerChk3,
            answerChk4;
    private QuizDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); // allow screen rotation

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                  // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back)); // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.edit_answers_title);  // set Title for Toolbar
        setSupportActionBar(toolbar);          // Setting/replace toolbar as the ActionBar

        // Handling "Back" button functionality
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnswersEditActivity.this, AnswersMainActivity.class); // returns to preceding activity
                showFeedbackMessage("dismiss");
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
        getMenuInflater().inflate(R.menu.answers_edit, menu);
        return true;
    }

    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(AnswersEditActivity.this, AnswersMainActivity.class);
        showFeedbackMessage("save");
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    public void showFeedbackMessage(String action) {
        if (action.equals("save")) {
            saveUpdatedAnswers();
            final Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_changes_saved, Toast.LENGTH_LONG);
            toast.show(); // Show the Toast on app interface
        } else if (action.equals("dismiss")) {
            final Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_changes_dismissed, Toast.LENGTH_LONG);
            toast.show(); // Show the Toast on app interface
        }
    }

    private void saveUpdatedAnswers() {
        EditText editAnswer1 = (EditText) findViewById(R.id.answer1);
        EditText editAnswer2 = (EditText) findViewById(R.id.answer2);
        EditText editAnswer3 = (EditText) findViewById(R.id.answer3);
        EditText editAnswer4 = (EditText) findViewById(R.id.answer4);

        answerChk1 = editAnswer1.getText().toString().trim();
        answerChk2 = editAnswer2.getText().toString().trim();
        answerChk3 = editAnswer3.getText().toString().trim();
        answerChk4 = editAnswer4.getText().toString().trim();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        updateGroupDetails(db);
    }

    private void updateGroupDetails(SQLiteDatabase db) {
        String selection = GroupContract._ID + "=?";
        String[] selectionArgs = {"1"};

        // Insert a new row for pet in the database, returning the ID of that new row.
        db.update(GroupContract.TABLE_NAME, getContentValues(), selection, selectionArgs);
    }

    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(GroupContract.ANSW_CHK1, answerChk1);
        values.put(GroupContract.ANSW_CHK2, answerChk2);
        values.put(GroupContract.ANSW_CHK3, answerChk3);
        values.put(GroupContract.ANSW_CHK4, answerChk4);
        return values;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AnswersEditActivity.this, AnswersMainActivity.class);
        showFeedbackMessage("dismiss");
        startActivity(i);
    }
}
