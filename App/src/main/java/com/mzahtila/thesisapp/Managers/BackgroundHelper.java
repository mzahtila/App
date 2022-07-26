package com.mzahtila.thesisapp.Managers;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.mzahtila.thesisapp.R;

// This helper enables/disables clicks on the the question answers and Done button while the example is displayed on the screen
public class BackgroundHelper {
    // Define variables
    public static boolean exit;
    public static View Answer;
    private static TextView done;
    public static Drawable example;


    public static void backgroundOnOff(Context context, Activity currentActivity, boolean setClickAbility, int nrQuestions) {

        int i = 1;
        while (i <= nrQuestions) {
            Answer = currentActivity.findViewById(R.id.answer + i);     //Get views by ID
            Answer.setClickable(setClickAbility);    // Enable/disable radio buttons
            i++;
        }

        done = currentActivity.findViewById(R.id.done);
        done.setClickable(setClickAbility); // Enable/disable Done button
    }
}
