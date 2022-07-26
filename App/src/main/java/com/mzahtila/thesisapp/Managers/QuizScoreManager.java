package com.mzahtila.thesisapp.Managers;

import android.content.Context;

import com.mzahtila.thesisapp.Data.QuizDbHelper;

import static java.lang.Integer.valueOf;

public class QuizScoreManager {
    private static String noAnswer = "not answered";
    private static int
            myScoreChk1,
            myScoreChk2,
            myScoreChk3,
            myScoreChk4;

    private QuizScoreManager() {
        myScoreChk1 = 0;
        myScoreChk2 = 0;
        myScoreChk3 = 0;
        myScoreChk4 = 0;
    }

    public static void addChkScore(int chkPoint, int addValue) {
        if (chkPoint == 1) {
            myScoreChk1 = myScoreChk1 + addValue;
        }
        if (chkPoint == 2) {
            myScoreChk2 = myScoreChk2 + addValue;
        }
        if (chkPoint == 3) {
            myScoreChk3 = myScoreChk3 + addValue;
        }
        if (chkPoint == 4) {
            myScoreChk4 = myScoreChk4 + addValue;
        }
    }

    public static int getChkScore(int chkPoint) {
        int chkScore = 0;

        if (chkPoint == 1) {
            chkScore = valueOf(myScoreChk1);
        } else if (chkPoint == 2) {
            chkScore = valueOf(myScoreChk2);
        } else if (chkPoint == 3) {
            chkScore = valueOf(myScoreChk3);
        } else if (chkPoint == 4) {
            chkScore = valueOf(myScoreChk4);
        }

        return chkScore;
    }

    public static int getMaxChkScore(int chkPoint) {
        int score = 0;

        if (chkPoint == 1) {
            score = 12;
        }
        if (chkPoint == 2) {
            score = 8;
        }
        if (chkPoint == 3) {
            score = 14;
        }
        if (chkPoint == 4) {
            score = 22;
        }

        return score;
    }

    public static int getMaxScore() {
        int myMaxScore = getMaxChkScore(1) + getMaxChkScore(2) + getMaxChkScore(3) + getMaxChkScore(4);
        return valueOf(myMaxScore);
    }

    public static String setNoAnswer() {
        return noAnswer;
    }

    public static void resetAll() {
        myScoreChk1 = 0;
        myScoreChk2 = 0;
        myScoreChk3 = 0;
        myScoreChk4 = 0;
    }

    public static void resetChkScore(int chkPoint) {
        if (chkPoint == 1) {
            myScoreChk1 = 0;
        }
        if (chkPoint == 2) {
            myScoreChk2 = 0;
        }
        if (chkPoint == 3) {
            myScoreChk3 = 0;
        }
        if (chkPoint == 4) {
            myScoreChk4 = 0;
        }
    }
}