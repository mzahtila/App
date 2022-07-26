/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mzahtila.thesisapp.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuizDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = QuizDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "app_database.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase myDataBase;

    /**
     * Constructs a new instance of {@link QuizDbHelper}.
     *
     * @param context of the app
     */
    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table

        String SQL_CREATE_RESULTS_TABLE = "CREATE TABLE " + GroupContract.TABLE_NAME + " ("
                + GroupContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GroupContract.GR_NUM + " INTEGER NOT NULL DEFAULT 0, "
                + GroupContract.GR_NAM + " TEXT NOT NULL DEFAULT 'unnamed', "
                + GroupContract.GR_MEM + " TEXT NOT NULL DEFAULT 'unnamed', "
                + GroupContract.SCORE + " INTEGER NOT NULL DEFAULT 0, "
                + GroupContract.MAX_SCORE + " INTEGER NOT NULL DEFAULT 56,"
                + GroupContract.PLD_CHK1 + " INTEGER NOT NULL DEFAULT 0, "
                + GroupContract.PLD_CHK2 + " INTEGER NOT NULL DEFAULT 0, "
                + GroupContract.PLD_CHK3 + " INTEGER NOT NULL DEFAULT 0, "
                + GroupContract.PLD_CHK4 + " INTEGER NOT NULL DEFAULT 0, "
                + GroupContract.ANSW_CHK1 + " TEXT NOT NULL DEFAULT 'not answered', "
                + GroupContract.ANSW_CHK2 + " TEXT NOT NULL DEFAULT 'not answered', "
                + GroupContract.ANSW_CHK3 + " TEXT NOT NULL DEFAULT 'not answered', "
                + GroupContract.ANSW_CHK4 + " TEXT NOT NULL DEFAULT 'not answered' );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_RESULTS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

    public boolean isDatabaseEmpty() {
        boolean databaseEmpty = true;
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(
                GroupContract.TABLE_NAME,   // The table to query
                null,               // The columns to return
                null,              // The columns for the WHERE clause
                null,           // The values for the WHERE clause
                null,               // Don't group the rows
                null,                // Don't filter by row groups
                null);              // The sort order

        if (cursor.getCount() > 0) {
            databaseEmpty = false;
        }

        cursor.close();
        //Log.d("GroupDetailsActivity", "Number of rows in the table = " + cursor.getCount());

        return databaseEmpty;
    }

    public String[] getAnswers() {
        SQLiteDatabase db = getReadableDatabase();

        /* Reading from the database */
        // Which columns to pick from the database: group number, group name, group members
        String[] columns = {
                GroupContract._ID,
                GroupContract.ANSW_CHK1,
                GroupContract.ANSW_CHK2,
                GroupContract.ANSW_CHK3,
                GroupContract.ANSW_CHK4};

        // Define conditions for selection: where column _ID = 1
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
        int answ1ColumnIndex = cursor.getColumnIndex(GroupContract.ANSW_CHK1);
        int answ2ColumnIndex = cursor.getColumnIndex(GroupContract.ANSW_CHK2);
        int answ3ColumnIndex = cursor.getColumnIndex(GroupContract.ANSW_CHK3);
        int answ4ColumnIndex = cursor.getColumnIndex(GroupContract.ANSW_CHK4);

        String answerChk1 = cursor.getString(answ1ColumnIndex);
        //Log.d("AnswersMainActivity", "CHK1 Answer: " + answerChk1);
        String answerChk2 = cursor.getString(answ2ColumnIndex);
        //Log.d("AnswersMainActivity", "CHK2 Answer: " + answerChk2);
        String answerChk3 = cursor.getString(answ3ColumnIndex);
        //Log.d("AnswersMainActivity", "CHK3 Answer: " + answerChk3);
        String answerChk4 = cursor.getString(answ4ColumnIndex);
        //Log.d("AnswersMainActivity", "CHK4 Answer: " + answerChk4);

        cursor.close();
        return new String[]{answerChk1, answerChk2, answerChk3, answerChk4};
    }

    public int getScore() {
        SQLiteDatabase db = getReadableDatabase();

        /* Reading from the database */
        // Which columns to pick from the database: group number, group name, group members
        String[] columns = {
                GroupContract._ID,
                GroupContract.SCORE };

        // Define conditions for selection: where column _ID = 1
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
        int scoreColumnIndex = cursor.getColumnIndex(GroupContract.SCORE);
        int score = Integer.parseInt(cursor.getString(scoreColumnIndex));
        cursor.close();

        return score;
    }

    public String[] getGrNumMem() {
        SQLiteDatabase db = getReadableDatabase();

        /* Reading from the database */
        // Which columns to pick from the database: group number, group name, group members
        String[] columns = {
                GroupContract._ID,
                GroupContract.GR_NUM,
                GroupContract.GR_MEM};

        // Define conditions for selection: where column _ID = 1
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
        int grNum = cursor.getColumnIndex(GroupContract.GR_NUM);
        int grMem = cursor.getColumnIndex(GroupContract.GR_MEM);

        String grNumber = cursor.getString(grNum);
        //Log.d("AnswersMainActivity", "CHK1 Answer: " + answerChk1);
        String grMembers = cursor.getString(grMem);
        //Log.d("AnswersMainActivity", "CHK2 Answer: " + answerChk2);

        cursor.close();
        return new String[]{grNumber, grMembers};
    }
}
