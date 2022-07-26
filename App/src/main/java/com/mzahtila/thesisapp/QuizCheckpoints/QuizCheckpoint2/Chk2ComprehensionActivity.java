package com.mzahtila.thesisapp.QuizCheckpoints.QuizCheckpoint2;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mzahtila.thesisapp.Data.GroupContract;
import com.mzahtila.thesisapp.Data.QuizDbHelper;
import com.mzahtila.thesisapp.Managers.QuizScoreManager;
import com.mzahtila.thesisapp.QuizMainActivity;
import com.mzahtila.thesisapp.R;

public class Chk2ComprehensionActivity extends AppCompatActivity {
    String answerChk;
    private QuizDbHelper mDbHelper;
    private CheckBox chkBxAnsw1, chkBxAnsw2, chkBxAnsw3, chkBxAnsw4, chkBxAnsw5;

    // Initial state of answers
    private boolean isChoiceAnswer1 = false;
    private boolean isChoiceAnswer2 = false;
    private boolean isChoiceAnswer3 = false;
    private boolean isChoiceAnswer4 = false;
    private boolean isChoiceAnswer5 = false;

    private CompoundButton.OnCheckedChangeListener chkCheckedListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk2_comprehension);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.exit)); // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.quiz_lecture2); // set Title for Toolbar
        toolbar.setSubtitle("Choose your definition");
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        // Handling EXIT button functionality
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
            }
        });
        // -----------------------------------------------------------------------------------------

        // Initialize the CheckBoxes
        chkBxAnsw1 = (CheckBox) findViewById(R.id.answer1);
        chkBxAnsw2 = (CheckBox) findViewById(R.id.answer2);
        chkBxAnsw3 = (CheckBox) findViewById(R.id.answer3);
        chkBxAnsw4 = (CheckBox) findViewById(R.id.answer4);
        chkBxAnsw5 = (CheckBox) findViewById(R.id.answer5);
        setListeners();

        // Create database helper
        mDbHelper = new QuizDbHelper(this);
    }

    /* ---------------------------------------------------------------------------------------------
     * Setting the toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz_comprehension_question, menu);
        return true;
    }

    /* ---------------------------------------------------------------------------------------------
     * Handling checkboxes
     * If the checkbox is marked it's state is TRUE, otherwise is FALSE
     */
    public void setListeners() {

        // CheckBox listeners
        chkCheckedListner = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton selectedCheckBox, boolean isChecked) {
                // Check which checkbox was clicked
                switch (selectedCheckBox.getId()) {
                    case R.id.answer1:
                        if (isChecked) {
                            isChoiceAnswer1 = true;
                        }
                        else
                            isChoiceAnswer1 = false;
                        break;
                    case R.id.answer2:
                        if (isChecked) {
                            isChoiceAnswer2 = true;
                        }
                        else
                            isChoiceAnswer2 = false;
                        break;
                    case R.id.answer3:
                        if (isChecked) {
                            isChoiceAnswer3 = true;
                        }
                        else
                            isChoiceAnswer3 = false;
                        break;
                    case R.id.answer4:
                        if (isChecked) {
                            isChoiceAnswer4 = true;
                        }
                        else
                            isChoiceAnswer4 = false;
                        break;
                    case R.id.answer5:
                        if (isChecked) {
                            isChoiceAnswer5 = true;
                        }
                        else
                            isChoiceAnswer5 = false;
                        break;
                }
            }
        };

        // Set check change listeners
        chkBxAnsw1.setOnCheckedChangeListener(chkCheckedListner);
        chkBxAnsw2.setOnCheckedChangeListener(chkCheckedListner);
        chkBxAnsw3.setOnCheckedChangeListener(chkCheckedListner);
        chkBxAnsw4.setOnCheckedChangeListener(chkCheckedListner);
        chkBxAnsw5.setOnCheckedChangeListener(chkCheckedListner);
    }

    /* ---------------------------------------------------------------------------------------------
    Handling @Done button
    - On entered answer finish the chapter, on empty answer request and answer
    */
    public void onDoneClick(View view) {
        if (!isChoiceAnswer1 && !isChoiceAnswer2 && !isChoiceAnswer3 && !isChoiceAnswer4 && !isChoiceAnswer5) {
            showDoneDialogBox("answerMissing");
        } else {
            answerChk = getAnswer(isChoiceAnswer1, isChoiceAnswer2, isChoiceAnswer3, isChoiceAnswer4, isChoiceAnswer5);
            int scoredChk = QuizScoreManager.getChkScore(2);

            mDbHelper = new QuizDbHelper(this);
            int groupOldScore = mDbHelper.getScore();
            int groupNewScore = groupOldScore + scoredChk;

            saveResults(groupNewScore);
            showDoneDialogBox("answerEntered");
        }
    }

    /* ---------------------------------------------------------------------------------------------
    Checking if the group has selected an answer
    */
    public String getAnswer(boolean answer1, boolean answer2, boolean answer3, boolean answer4, boolean answer5) {
        CheckBox checkBox;
        String answer = "";

        if (answer1) {
            checkBox = (CheckBox) findViewById(R.id.answer1);
            String definition1 = checkBox.getText().toString();
            answer = answer + "1. " + definition1;
        }
        if (answer2) {
            checkBox = (CheckBox) findViewById(R.id.answer2);
            String definition2 = checkBox.getText().toString();
            answer = answer + " 2. " + definition2;
        }
        if (answer3) {
            checkBox = (CheckBox) findViewById(R.id.answer3);
            String definition3 = checkBox.getText().toString();
            answer = answer + " 3. " + definition3;
        }
        if (answer4) {
            checkBox = (CheckBox) findViewById(R.id.answer4);
            String definition4 = checkBox.getText().toString();
            answer = answer + " 4. " + definition4;
        }
        if (answer5) {
            checkBox = (CheckBox) findViewById(R.id.answer5);
            String definition5 = checkBox.getText().toString();
            answer = answer + " 5. " + definition5;
        }

        //Log.i("Chk3comprehensionActivity", "Answers: " + answer1 + ", " + answer2 + ", " + answer3 + ", " + answer4);
        return answer;
    }

    private void saveResults(int newScore) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GroupContract.SCORE, newScore);
        values.put(GroupContract.PLD_CHK2, 1);
        values.put(GroupContract.ANSW_CHK2, answerChk);

        String selection = GroupContract._ID + "=?";
        String[] selectionArgs = {"1"};

        long newRowId = db.update(GroupContract.TABLE_NAME, values, selection, selectionArgs);

        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving details", Toast.LENGTH_SHORT).show();
        }
    }

    private void exitDialog() {
        // Setting a dialog box
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage(R.string.dialog_exit);

        exitDialog.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int arg) {
                QuizScoreManager.resetChkScore(2);
                Intent i = new Intent(Chk2ComprehensionActivity.this, QuizMainActivity.class);
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
     * Dialog box that is shown on "Done" button click
     */
    private void showDoneDialogBox(String answered) {
        // Setting a dialog box
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);

        if (answered.equals("answerMissing")) {
            exitDialog.setMessage(R.string.selection_missing); // Tells the users they have to answer the question

            exitDialog.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int arg) {
                    dialog.dismiss();
                }
            });
        } else if (answered.equals("answerEntered")) {
            exitDialog.setMessage(R.string.dialog_successfully_finished); // Tells the users they finished the lecture

            exitDialog.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int arg) {
                    showDoneDialogBox("showScoreFeedback");
                }
            });
        } else if (answered.equals("showScoreFeedback")) {
            int scoredAtCheckpoint = QuizScoreManager.getChkScore(2);
            final String scoreFeedback = " You scored " + scoredAtCheckpoint + " points at this checkpoint.";

            exitDialog.setMessage(scoreFeedback); // Tells the users they finished the lecture

            exitDialog.setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int arg) {
                    Intent i = new Intent(Chk2ComprehensionActivity.this, QuizMainActivity.class);
                    startActivity(i);
                }
            });
        }

        AlertDialog dialog = exitDialog.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }
}
