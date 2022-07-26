package com.mzahtila.thesisapp.Managers;

import android.content.Context;
import android.widget.Toast;

public class CheckpointsHelper {

    private CheckpointsHelper() {
    }

    /* ---------------------------------------------------------------------------------------------
    Notification for scored points on correct answer
    */
    public static void pointsNotification(Context context, int tempScore) {
        if (tempScore == 0) {
            Toast.makeText(context, "+" + String.valueOf(tempScore) + " points\nTry more on the next one!", Toast.LENGTH_LONG).show(); // display message for 0 points
        } else if (tempScore == 1) {
            Toast.makeText(context, "+" + String.valueOf(tempScore) + " point\nOn the right track!", Toast.LENGTH_LONG).show(); // display message for 1 point
        } else if (tempScore == 2)
            Toast.makeText(context, "+" + String.valueOf(tempScore) + " points\nWell done!", Toast.LENGTH_LONG).show(); // display message for 2 points
    }
}
