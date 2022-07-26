package com.mzahtila.thesisapp.QuizCheckpoints.QuizCheckpoint2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.mzahtila.thesisapp.Managers.BackgroundHelper;
import com.mzahtila.thesisapp.QuizMainActivity;
import com.mzahtila.thesisapp.Managers.QuizScoreManager;
import com.mzahtila.thesisapp.R;

public class Chk2Q4Activity extends AppCompatActivity {
    ImageView myExample;
    Toolbar toolbar;

    private CompoundButton.OnCheckedChangeListener chkCheckedListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk2_q4);

        toolbar = findViewById(R.id.toolbar); // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.exit)); // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.quiz_lecture2); // set Title for Toolbar

        int currentScore = QuizScoreManager.getChkScore(2);
        int maxChkScore = QuizScoreManager.getMaxChkScore(2);
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

        myExample = findViewById(R.id.example_map);
    }

    private void exitDialog() {
        // Setting a dialog box
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage(R.string.dialog_exit);

        exitDialog.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int arg) {
                QuizScoreManager.resetChkScore(2);
                Intent i = new Intent(Chk2Q4Activity.this, QuizMainActivity.class);
                startActivity(i);
            }
        });

        exitDialog.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int arg) {
                //finish();
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
            BackgroundHelper.backgroundOnOff(this, this, true, 0); // Enabling clicks on the background
        } else {
            myExample.setVisibility(View.VISIBLE); // Showing the example on the screen
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Hiding a navigation icon (Exit) in Toolbar
            BackgroundHelper.backgroundOnOff(this, this, false, 0); // Disabling clicks on the background
        }
    }
    // ---------------------------------------------------------------------------------------------

    /* ---------------------------------------------------------------------------------------------
    Handling @Done button
    - On correct answer start next question, otherwise show the message and restart the same question
    */
    public void onDoneClick(View view) {
        Intent i;
        i = new Intent(Chk2Q4Activity.this, Chk2ComprehensionActivity.class); // start next question
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }
}
