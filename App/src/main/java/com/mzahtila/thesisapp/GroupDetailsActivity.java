package com.mzahtila.thesisapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mzahtila.thesisapp.Data.GroupContract;
import com.mzahtila.thesisapp.Data.QuizDbHelper;
import com.mzahtila.thesisapp.Managers.QuizScoreManager;

public class GroupDetailsActivity extends AppCompatActivity {
    String groupNr,
            groupName,
            groupMembers;
    int groupScore,
            maxQuizScore;
    private QuizDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        toolbar.setTitle(R.string.group_details_title);                     // set Title for Toolbar
        setSupportActionBar(toolbar);                         // Setting/replace toolbar as the ActionBar

        // Create database helper
        mDbHelper = new QuizDbHelper(this);
    }

    /* ---------------------------------------------------------------------------------------------
    Setting the toolbar
    */
    // Activity's overrided method used to set the menu file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_details, menu);
        return true;
    }

    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(GroupDetailsActivity.this, InfoActivity.class);
        i.putExtra("backToActivity", "1");
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Log.d("GroupDetailsActivity", "Database empty = " + mDbHelper.isDatabaseEmpty());
        if (!mDbHelper.isDatabaseEmpty()) {
            showGroupDetails();
        }
    }

    /* ---------------------------------------------------------------------------------------------
    Get details from the database
    */
    private void showGroupDetails() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] columns = getColumns();
        String selection = GroupContract._ID + "=?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query(
                GroupContract.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToPosition(0);
        int grNumColumnIndex = cursor.getColumnIndex(GroupContract.GR_NUM);
        int grNameColumnIndex = cursor.getColumnIndex(GroupContract.GR_NAM);
        int grMemColumnIndex = cursor.getColumnIndex(GroupContract.GR_MEM);

        String grNum = cursor.getString(grNumColumnIndex);
        String grName = cursor.getString(grNameColumnIndex);
        String grMem = cursor.getString(grMemColumnIndex);

        EditText editNumber = (EditText) findViewById(R.id.group_number);
        EditText editName = (EditText) findViewById(R.id.group_name);
        EditText editMembers = (EditText) findViewById(R.id.group_members);

        editNumber.setText(grNum);
        editName.setText(grName);
        editMembers.setText(grMem);

        cursor.close();
    }

    private String[] getColumns() {
        String[] columns = {
                GroupContract._ID,
                GroupContract.GR_NUM,
                GroupContract.GR_NAM,
                GroupContract.GR_MEM};

        return columns;
    }

    /* ---------------------------------------------------------------------------------------------
    Handling buttons' actions
    */
    public void onClickStartNewActivity(View view) {
        EditText editNumber = (EditText) findViewById(R.id.group_number);
        groupNr = editNumber.getText().toString().trim();
        Log.d("GroupDetailsActivity", "Group number = " + groupNr);

        EditText editName = (EditText) findViewById(R.id.group_name);
        groupName = editName.getText().toString().trim();
        Log.d("GroupDetailsActivity", "Group name = " + groupName);

        EditText editMembers = (EditText) findViewById(R.id.group_members);
        groupMembers = editMembers.getText().toString().trim();
        Log.d("GroupDetailsActivity", "Group Members = " + groupMembers);

        //groupScore = String.valueOf(QuizScoreManager.getFinalScore());
        //mDbHelper = new QuizDbHelper(this);
        //groupScore = String.valueOf(mDbHelper.getScore());
        maxQuizScore = QuizScoreManager.getMaxScore();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (mDbHelper.isDatabaseEmpty()) {
            /* Start new group or change group details */
            groupScore = 0;
            insertGroupDetails(db);
        } else
            groupScore = mDbHelper.getScore();
            updateGroupDetails(db);

        Intent i;
        i = new Intent(GroupDetailsActivity.this, MapActivity.class);
        startActivity(i);
    }

    private void insertGroupDetails(SQLiteDatabase db) {
        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(GroupContract.TABLE_NAME, null, getContentValues());

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving details", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGroupDetails(SQLiteDatabase db) {
        String selection = GroupContract._ID + "=?";
        String[] selectionArgs = {"1"};

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.update(GroupContract.TABLE_NAME, getContentValues(), selection, selectionArgs);
    }

    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(GroupContract.GR_NUM, groupNr);
        values.put(GroupContract.GR_NAM, groupName);
        values.put(GroupContract.GR_MEM, groupMembers);
        values.put(GroupContract.SCORE, groupScore);
        values.put(GroupContract.MAX_SCORE, maxQuizScore);

        return values;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}
