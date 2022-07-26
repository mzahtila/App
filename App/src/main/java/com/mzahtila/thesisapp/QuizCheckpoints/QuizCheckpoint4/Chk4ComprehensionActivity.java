package com.mzahtila.thesisapp.QuizCheckpoints.QuizCheckpoint4;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mzahtila.thesisapp.Data.GroupContract;
import com.mzahtila.thesisapp.Data.QuizDbHelper;
import com.mzahtila.thesisapp.QuizMainActivity;
import com.mzahtila.thesisapp.Managers.QuizScoreManager;
import com.mzahtila.thesisapp.R;

public class Chk4ComprehensionActivity extends AppCompatActivity {
    String  answerChk;
    private QuizDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk4_comprehension);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.exit)); // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.quiz_lecture4); // set Title for Toolbar
        toolbar.setSubtitle(R.string.chekcpoint_comprehension_question_subtitle);
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        // Handling EXIT button functionality
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
            }
        });
        // -----------------------------------------------------------------------------------------

        // Create database helper
        mDbHelper = new QuizDbHelper(this);
    }

    private void exitDialog() {
        // Setting a dialog box
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage(R.string.dialog_exit);

        exitDialog.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int arg) {
                QuizScoreManager.resetChkScore(4);
                Intent i = new Intent(Chk4ComprehensionActivity.this, QuizMainActivity.class);
                startActivity(i);
            }
        });

        exitDialog.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int arg) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = exitDialog.create();
        dialog.show();
    }

    /* ---------------------------------------------------------------------------------------------
    Setting the toolbar
    */
    // Activity's overrided method used to set the menu file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz_comprehension_question, menu);
        return true;
    }

    /* ---------------------------------------------------------------------------------------------
    Handling @Done button
    - On entered answer finish the chapter, on empty answer request and answer
    */
    public void onDoneClick(View view) {
        Intent i;
        EditText answered = (EditText) findViewById(R.id.answer);
        String answered2 = answered.getText().toString();

        if (answered2.equals("")) {
            showDoneDialogBox("answerMissing");
        }
        else {
            int scoredChk = QuizScoreManager.getChkScore(4);

            mDbHelper = new QuizDbHelper(this);
            int groupOldScore = mDbHelper.getScore();
            int groupNewScore = groupOldScore + scoredChk;

            saveResults(groupNewScore);
            showDoneDialogBox("answerEntered");
        }
    }

    // Dialog box that is shown on "Done" button click
    private void showDoneDialogBox(String answered) {
        // Setting a dialog box
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);

        if (answered.equals("answerMissing")) {
            exitDialog.setMessage(R.string.answer_missing); // Tells the users they have to answer the question

            exitDialog.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int arg) {
                    dialog.dismiss();
                }
            });
        }
        else if (answered.equals("answerEntered")) {
            exitDialog.setMessage(R.string.dialog_successfully_finished); // Tells the users they finished the lecture

            exitDialog.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int arg) {
                    showDoneDialogBox("showScoreFeedback");
                }
            });
        }

        else if (answered.equals("showScoreFeedback")) {
            int scoredAtCheckpoint = QuizScoreManager.getChkScore(4);
            final String scoreFeedback =  " You scored " + scoredAtCheckpoint + " points at this checkpoint.";

            exitDialog.setMessage(scoreFeedback); // Tells the users they finished the lecture

            exitDialog.setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int arg) {
                    Intent i = new Intent(Chk4ComprehensionActivity.this, QuizMainActivity.class);
                    startActivity(i);
                }
            });
        }

        AlertDialog dialog = exitDialog.create();
        dialog.show();
    }

    private void saveResults(int newScore) {
        EditText typeAnswer = (EditText) findViewById(R.id.answer);
        answerChk = typeAnswer.getText().toString().trim();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GroupContract.SCORE, newScore);
        values.put(GroupContract.PLD_CHK4, 1);
        values.put(GroupContract.ANSW_CHK4, answerChk);

        String selection = GroupContract._ID + "=?";
        String[] selectionArgs = {"1"};

        long newRowId = db.update(GroupContract.TABLE_NAME, values, selection, selectionArgs);

        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }

}
