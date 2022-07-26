package com.mzahtila.thesisapp.QuizCheckpoints.QuizCheckpoint3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mzahtila.thesisapp.Managers.BackgroundHelper;
import com.mzahtila.thesisapp.Managers.ButtonStyleHelper;
import com.mzahtila.thesisapp.Managers.CheckpointsHelper;
import com.mzahtila.thesisapp.Managers.QuizScoreManager;
import com.mzahtila.thesisapp.QuizMainActivity;
import com.mzahtila.thesisapp.R;

public class Chk3Q3Activity extends AppCompatActivity {
    ImageView myExample;
    Toolbar toolbar;
    Context context;

    private int countTrys = 0;
    private int tempScore = 3;
    private RadioButton chkBxAnsw1, chkBxAnsw2, chkBxAnsw3, chkBxAnsw4, chkBxAnsw5;
    // Initial state of answers
    private boolean answers = false;
    private boolean isChoiceAnswer1 = false;
    private boolean isChoiceAnswer2 = false;
    private boolean isChoiceAnswer3 = false;
    private boolean isChoiceAnswer4 = false;
    private boolean isChoiceAnswer5 = false;

    private CompoundButton.OnCheckedChangeListener chkCheckedListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk3_q3);

        toolbar = findViewById(R.id.toolbar); // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.exit)); // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.quiz_lecture3); // set Title for Toolbar

        int currentScore = QuizScoreManager.getChkScore(3);
        int maxChkScore = QuizScoreManager.getMaxChkScore(3);
        toolbar.setSubtitle("Current score: " + currentScore + "/" + maxChkScore);

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
        chkBxAnsw1 = (RadioButton) findViewById(R.id.answer1);
        chkBxAnsw2 = (RadioButton) findViewById(R.id.answer2);
        chkBxAnsw3 = (RadioButton) findViewById(R.id.answer3);
        chkBxAnsw4 = (RadioButton) findViewById(R.id.answer4);
        chkBxAnsw5 = (RadioButton) findViewById(R.id.answer5);
        setListeners();

        myExample = findViewById(R.id.example_map);
        context = getApplicationContext();
    }

    private void exitDialog() {
        // Setting a dialog box
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage(R.string.dialog_exit);

        exitDialog.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int arg) {
                resetVariables();
                QuizScoreManager.resetChkScore(3);
                Intent i = new Intent(Chk3Q3Activity.this, QuizMainActivity.class);
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

    private void resetVariables() {
        countTrys = 0;
        tempScore = 3;
    }

    /* ---------------------------------------------------------------------------------------------
    Setting the toolbar
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz_checkpoint, menu);
        return true;
    }

    // Handling EXAMPLE button functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Menu menu = toolbar.getMenu();

        switch (item.getItemId()) {
            case R.id.example:
                item.setVisible(false);
                menu.findItem(R.id.close).setVisible(true);
                showExample();
                return true;

            case R.id.close:
                item.setVisible(false);
                menu.findItem(R.id.example).setVisible(true);
                showExample();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void showExample() {

        if (myExample.getVisibility() == View.VISIBLE) {
            myExample.setVisibility(View.INVISIBLE); // Hiding the example from the screen
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Showing a navigation icon (Exit) in Toolbar again
            BackgroundHelper.backgroundOnOff(this, this, true, 5); // Enabling clicks on the background
        }
        else {
            myExample.setVisibility(View.VISIBLE); // Showing the example on the screen
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Hiding a navigation icon (Exit) in Toolbar
            BackgroundHelper.backgroundOnOff(this, this, false, 5); // Disabling clicks on the background
        }
    }

    /* ---------------------------------------------------------------------------------------------
    Handling checkboxes
    - If the checkbox is marked it's state is TRUE, otherwise is FALSE
    */
    public void setListeners() {

        // CheckBox listeners
        chkCheckedListner = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton selectedCheckBox, boolean isChecked) {
                TextView doneButton = findViewById(R.id.done);

                // Check which checkbox was clicked
                switch (selectedCheckBox.getId()) {
                    case R.id.answer1:
                        if (isChecked) {
                            isChoiceAnswer1 = true;
                            ButtonStyleHelper.styleButtonEnabled(context, doneButton);
                        } else
                            isChoiceAnswer1 = false;
                        break;
                    case R.id.answer2:
                        if (isChecked) {
                            isChoiceAnswer2 = true;
                            ButtonStyleHelper.styleButtonEnabled(context, doneButton);
                        } else
                            isChoiceAnswer2 = false;
                        break;
                    case R.id.answer3:
                        if (isChecked) {
                            isChoiceAnswer3 = true;
                            ButtonStyleHelper.styleButtonEnabled(context, doneButton);
                        } else
                            isChoiceAnswer3 = false;
                        break;
                    case R.id.answer4:
                        if (isChecked) {
                            isChoiceAnswer4 = true;
                            ButtonStyleHelper.styleButtonEnabled(context, doneButton);
                        } else
                            isChoiceAnswer4 = false;
                        break;
                    case R.id.answer5:
                        if (isChecked) {
                            isChoiceAnswer5 = true;
                            ButtonStyleHelper.styleButtonEnabled(context, doneButton);
                        } else
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
    Checking if the group has answered correctly
    */
    public void testAnswers(boolean answer1, boolean answer2, boolean answer3, boolean answer4, boolean answer5) {
        if (answer1 && (!answer2 && !answer3 && !answer4 && !answer5)) {
            answers = true;
        }
        Log.i("Chk3Q3Activity", "Answers: " + answer1 + ", " + answer2 + ", " + answer3 + ", " + answer4);
    }

    /* ---------------------------------------------------------------------------------------------
       Handling @Done button
       - On correct answer start next question, otherwise show the message and restart the same question
       */
    public void onDoneClick(View view) {
        Intent i;

        if (!chkBxAnsw1.isChecked() && !chkBxAnsw2.isChecked() && !chkBxAnsw3.isChecked() && !chkBxAnsw4.isChecked() && !chkBxAnsw5.isChecked()) {
            Toast.makeText(getApplicationContext(), "Choose an answer!", Toast.LENGTH_SHORT).show();
        }
        else {
            countTrys = countTrys + 1;

            if (countTrys > 2) {
                tempScore = 0;
            } else
                tempScore = tempScore - 1;

            testAnswers(isChoiceAnswer1, isChoiceAnswer2, isChoiceAnswer3, isChoiceAnswer4, isChoiceAnswer5);
            Log.i("Chk3Q2Activity", "Answered correctly: " + answers);

            if (answers) {
                QuizScoreManager.addChkScore(3, tempScore); // updates current score on current checkpoint
                i = new Intent(Chk3Q3Activity.this, Chk3Q4Activity.class); // start next question
                CheckpointsHelper.pointsNotification(context, tempScore);
                startActivity(i);
                overridePendingTransition(0, 0);
            } else
                wrongAnswerDialog();
        }
    }

    /* ---------------------------------------------------------------------------------------------
    Notification for wrong answer
    */
    private void wrongAnswerDialog() {
        // Setting a dialog box
        AlertDialog.Builder tryAgainDialog = new AlertDialog.Builder(this);
        tryAgainDialog.setMessage(R.string.dialog_wrong_answer);
        tryAgainDialog.setPositiveButton(R.string.dialog_okay, new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int arg) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = tryAgainDialog.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }
}
