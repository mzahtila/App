package com.mzahtila.thesisapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.mzahtila.thesisapp.Data.GroupContract;
import com.mzahtila.thesisapp.Data.QuizDbHelper;
import com.mzahtila.thesisapp.Managers.QuizScoreManager;
import com.mzahtila.thesisapp.QuizCheckpoints.QuizCheckpoint1.Chk1Q1Activity;
import com.mzahtila.thesisapp.QuizCheckpoints.QuizCheckpoint2.Chk2Q1Activity;
import com.mzahtila.thesisapp.QuizCheckpoints.QuizCheckpoint3.Chk3Q1Activity;
import com.mzahtila.thesisapp.QuizCheckpoints.QuizCheckpoint4.Chk4Q1Activity;

public class QuizMainActivity extends AppCompatActivity {
    int groupScore;
    String groupName = "Nameless :/";
    int chk1IsPlayed, chk2IsPlayed, chk3IsPlayed, chk4IsPlayed;
    private QuizDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        getDetails();
//        Log.i("QuizMainActivity", "PLAYED: Chk1 = " + chk1IsPlayed);
//        Log.i("QuizMainActivity", "PLAYED: Chk2 = " + chk2IsPlayed);
//        Log.i("QuizMainActivity", "PLAYED: Chk3 = " + chk3IsPlayed);
//        Log.i("QuizMainActivity", "PLAYED: Chk4 = " + chk4IsPlayed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                 // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back)); // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.quiz_main_title);   // set Title for Toolbar

        toolbar.setSubtitle(groupName);     // set name of the group as Subtitle for Toolbar
        setSupportActionBar(toolbar);       // Setting/replace toolbar as the ActionBar

        // Handling "Back" button functionality
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizMainActivity.this, MapActivity.class); // returns to preceding activity
                startActivity(i);
            }
        });

        // Set score result
        updateScore();
    }

    private void updateScore() {
        // Display updated score on the screen
        TextView scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText(groupScore + "/" + QuizScoreManager.getMaxScore());
    }

    /* ---------------------------------------------------------------------------------------------
    Setting onStart functionality
    */
    @Override
    protected void onStart() {
        super.onStart();
        checkPlayingStatus();
    }

    public void checkPlayingStatus() {
        TextView text;
        LinearLayout bttn;

        if (chk1IsPlayed == 1) {
            bttn = (LinearLayout) findViewById(R.id.chk1_vertical_wrapper);
            text = (TextView) findViewById(R.id.checkpoint1);
            styleButtonDisabled(bttn, text);
        }
        if (chk2IsPlayed == 1) {
            bttn = (LinearLayout) findViewById(R.id.chk2_vertical_wrapper);
            text = (TextView) findViewById(R.id.checkpoint2);
            styleButtonDisabled(bttn, text);
        }
        if (chk3IsPlayed == 1) {
            bttn = (LinearLayout) findViewById(R.id.chk3_vertical_wrapper);
            text = (TextView) findViewById(R.id.checkpoint3);
            styleButtonDisabled(bttn, text);
        }
        if (chk4IsPlayed == 1) {
            bttn = (LinearLayout) findViewById(R.id.chk4_vertical_wrapper);
            text = (TextView) findViewById(R.id.checkpoint4);
            styleButtonDisabled(bttn, text);
        }
        if (allCheckpointsPlayed()) {
            TextView answersBttn = (TextView) findViewById(R.id.answers_button);
            TextView submitBttn = (TextView) findViewById(R.id.submit);

            styleButtonEnabled(answersBttn);
            styleButtonEnabled(submitBttn);
        }
    }

    private void styleButtonDisabled(LinearLayout bttn, TextView text) {
        final int version = Build.VERSION.SDK_INT;
        if (version < Build.VERSION_CODES.JELLY_BEAN) {
            bttn.setBackgroundDrawable(getDrawable(R.drawable.square_border_color_gray));
            text.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));

        } else {
            bttn.setBackground(getDrawable(R.drawable.square_border_color_gray));
            text.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void styleButtonEnabled(TextView text) {
        text.setTextColor(Color.parseColor("white"));
        text.setBackground(getDrawable(R.drawable.square_fill_accent));
    }

    /* ---------------------------------------------------------------------------------------------
    Setting the toolbar
    */
    // Activity's overrided method used to set the menu file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz_main, menu);
        return true;
    }

    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_info:
                Intent i = new Intent(QuizMainActivity.this, InfoActivity.class);
//                String activity = this.getClass().getSimpleName();
//                i.putExtra("backToActivity", activity);
                i.putExtra("backToActivity", "3");
                startActivity(i);
                break;
            case R.id.action_reset_all:
                // Setting a dialog box
                AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
                exitDialog.setMessage(R.string.dialog_exit);

                exitDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    //@Override
                    public void onClick(DialogInterface dialog, int arg) {
                        // Reset parameters
                        resetResults();
                        QuizScoreManager.resetAll();
                        resetDatabase();
                        updateScore();

                        // Show feedback message
                        final Toast toast = Toast.makeText(getApplicationContext(), "You can now restart the quiz :)", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0); // Show the Toast on center
                        toast.show(); // Show the Toast on app interface

                        // Restart activity
                        Intent i = new Intent(QuizMainActivity.this, QuizMainActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                });

                exitDialog.setNegativeButton("Keep", new DialogInterface.OnClickListener() {
                    //@Override
                    public void onClick(DialogInterface dialog, int arg) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = exitDialog.create();
                dialog.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Handling "Reset results" button
    private void resetResults() {
        for (int i = 1; i > 4; i++) {
            QuizScoreManager.resetAll(); // Reset each checkpoint score QuizScoreManager
        }

        updateScore(); // Update screen score
    }

    /* ---------------------------------------------------------------------------------------------
    Handling button's actions
    */
    public void onClickStartNewActivity(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.chk1_vertical_wrapper:
                if (chk1IsPlayed == 1) {
                    showChkIsPlayedMessage();
                } else {
                    i = new Intent(QuizMainActivity.this, Chk1Q1Activity.class);
                    startActivity(i);
                }
                break;
            case R.id.chk2_vertical_wrapper:
                if (chk2IsPlayed == 1) {
                    showChkIsPlayedMessage();
                } else {
                    i = new Intent(QuizMainActivity.this, Chk2Q1Activity.class);
                    startActivity(i);
                }
                break;
            case R.id.chk3_vertical_wrapper:
                if (chk3IsPlayed == 1) {
                    showChkIsPlayedMessage();
                } else {
                    i = new Intent(QuizMainActivity.this, Chk3Q1Activity.class);
                    startActivity(i);
                }
                break;
            case R.id.chk4_vertical_wrapper:
                if (chk4IsPlayed == 1) {
                    showChkIsPlayedMessage();
                } else {
                    i = new Intent(QuizMainActivity.this, Chk4Q1Activity.class);
                    startActivity(i);
                }
                break;
            case R.id.answers_button:
                if (allCheckpointsPlayed()) {
                    i = new Intent(QuizMainActivity.this, AnswersMainActivity.class);
                    startActivity(i);
                } else
                    notAllowedMessage("review answers");
                break;
            case R.id.submit:
                if (allCheckpointsPlayed()) {
                    //showSubmitDialog();
                    submitResults();
                } else
                    notAllowedMessage("submit results and answers");
                break;
        }
    }

    public void submitResults() {
        String[] addresses = {"moris.zahtila@tu-dresden.de", "dirk.burghardt@tu-dresden.de", "benjamin.schroeter@tu-dresden.de"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);

        String[] grDet = mDbHelper.getGrNumMem();

        String grNum = "- NUMBER: " + grDet[0] + "\n";
        String grNam = "- NAME: " + groupName + "\n";
        String grMem = "- STUDENTS: " + grDet[1] + "\n\n";
        String grDetails = "Group details:" + "\n\n" + grNum +grNam + grMem;

        String score = "- Group score: " + groupScore + "/" + QuizScoreManager.getMaxScore() + "\n";
        String[] answers = getAnswers();
        String CHK1Answ = "- CHK1 ANSWER: " + answers[0] + "\n";
        String CHK2Answ = "- CHK2 ANSWER: " + answers[1] + "\n";
        String CHK3Answ = "- CHK3 ANSWER: " + answers[2]+ "\n";
        String CHK4Answ = "- CHK4 ANSWER: " + answers[3];

        String grResults = "Group results: " + "\n\n" + score + CHK1Answ + CHK2Answ + CHK3Answ + CHK4Answ;

        intent.putExtra(Intent.EXTRA_SUBJECT, "ACFS2019 Quiz results - Group nr. " + grDet[0]);
        intent.putExtra(Intent.EXTRA_TEXT   , grDetails + "\n" + grResults);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else
            Toast.makeText(QuizMainActivity.this, "There are no e-mail clients installed.", Toast.LENGTH_SHORT).show();

/*        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(QuizMainActivity.this, "There are no e-mail clients installed.", Toast.LENGTH_SHORT).show();
        }*/
    }

    private String[] getAnswers() {
        String[] answered = mDbHelper.getAnswers();

        String answerChk1 = answered[0];
        String answerChk2 = answered[1];
        String answerChk3 = answered[2];
        String answerChk4 = answered[3];
        String noAnswer = QuizScoreManager.setNoAnswer();

        if (answerChk1.equals(noAnswer)) {
            answerChk1 = getString(R.string.no_answer);
        }
        if (answerChk2.equals(noAnswer)) {
            answerChk2 = getString(R.string.no_answer);
        }
        if (answerChk3.equals(noAnswer)) {
            answerChk3 = getString(R.string.no_answer);
        }
        if (answerChk4.equals(noAnswer)) {
            answerChk4 = getString(R.string.no_answer);
        }

        String [] answers = {answerChk1, answerChk2, answerChk3, answerChk4};
        return answers;

    }

    private void showChkIsPlayedMessage() {
        // Show feedback message
        final Toast toast = Toast.makeText(getApplicationContext(), "You have already completed this lecture.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0); // Show the Toast on the center of the screen
        toast.show();
    }

    private void notAllowedMessage(String action) {
        // Show feedback message
        final Toast toast = Toast.makeText(getApplicationContext(), "You can " + action + " when you complete all lectures", Toast.LENGTH_LONG);
        toast.show();
    }

    private void showSubmitDialog() {
        AlertDialog.Builder submitDialog = new AlertDialog.Builder(this);
        submitDialog.setMessage(R.string.dialog_bummer); // Tells the users they have to answer the question

        submitDialog.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int arg) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = submitDialog.create();
        dialog.show();
    }

    /* ---------------------------------------------------------------------------------------------
    Checking if all lectures are completed
    */
    private boolean allCheckpointsPlayed() {
        boolean arePlayed = false;
        if (chk1IsPlayed == 1 && chk2IsPlayed == 1 && chk3IsPlayed == 1 && chk4IsPlayed == 1) {
            arePlayed = true;
        }
        return arePlayed;
    }

    /* ---------------------------------------------------------------------------------------------
    Get details from the database
    */
    private void getDetails() {
        // Create database helper
        mDbHelper = new QuizDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] columns = {
                GroupContract._ID,
                GroupContract.GR_NAM,
                GroupContract.SCORE,
                GroupContract.PLD_CHK1,
                GroupContract.PLD_CHK2,
                GroupContract.PLD_CHK3,
                GroupContract.PLD_CHK4};

        // Define conditions for selection: where column _ID equals 1
        String selection = GroupContract._ID + "=?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query(
                GroupContract.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        cursor.moveToPosition(0);

        int grNameColumnIndex = cursor.getColumnIndex(GroupContract.GR_NAM);
        int grScoreColumnIndex = cursor.getColumnIndex(GroupContract.SCORE);
        int chk1PlayedColumnIndex = cursor.getColumnIndex(GroupContract.PLD_CHK1);
        int chk2PlayedColumnIndex = cursor.getColumnIndex(GroupContract.PLD_CHK2);
        int chk3PlayedColumnIndex = cursor.getColumnIndex(GroupContract.PLD_CHK3);
        int chk4PlayedColumnIndex = cursor.getColumnIndex(GroupContract.PLD_CHK4);

        String grName = cursor.getString(grNameColumnIndex);
        if (!grName.equals("")) {
            this.groupName = grName;
        }

        groupScore = Integer.parseInt(cursor.getString(grScoreColumnIndex));

        this.chk1IsPlayed = Integer.parseInt(cursor.getString(chk1PlayedColumnIndex));
        this.chk2IsPlayed = Integer.parseInt(cursor.getString(chk2PlayedColumnIndex));
        this.chk3IsPlayed = Integer.parseInt(cursor.getString(chk3PlayedColumnIndex));
        this.chk4IsPlayed = Integer.parseInt(cursor.getString(chk4PlayedColumnIndex));

        cursor.close();
    }

    /* ---------------------------------------------------------------------------------------------
    Reset the database to restart the session
    */
    public void resetDatabase() {
        mDbHelper = new QuizDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues resetedValues = new ContentValues();
        String noAnswer = QuizScoreManager.setNoAnswer();

        resetedValues.put(GroupContract.GR_NUM, "");
        resetedValues.put(GroupContract.GR_NAM, "");
        resetedValues.put(GroupContract.GR_MEM, "");
        resetedValues.put(GroupContract.SCORE, 0);
        resetedValues.put(GroupContract.PLD_CHK1, 0);
        resetedValues.put(GroupContract.PLD_CHK2, 0);
        resetedValues.put(GroupContract.PLD_CHK3, 0);
        resetedValues.put(GroupContract.PLD_CHK4, 0);
        resetedValues.put(GroupContract.ANSW_CHK1, noAnswer);
        resetedValues.put(GroupContract.ANSW_CHK2, noAnswer);
        resetedValues.put(GroupContract.ANSW_CHK3, noAnswer);
        resetedValues.put(GroupContract.ANSW_CHK4, noAnswer);

        // Define conditions for selection: where column _ID equals 1
        String selection = GroupContract._ID + "=?";
        String[] selectionArgs = {"1"};

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.update(GroupContract.TABLE_NAME, resetedValues, selection, selectionArgs);
    }

    /* ---------------------------------------------------------------------------------------------
    On pressing back button go to Route map
    */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(QuizMainActivity.this, MapActivity.class);
        startActivity(i);
    }
}
