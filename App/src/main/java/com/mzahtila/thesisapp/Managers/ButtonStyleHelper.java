package com.mzahtila.thesisapp.Managers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.mzahtila.thesisapp.R;

import static androidx.core.content.ContextCompat.getDrawable;

public class ButtonStyleHelper {
//    private Context context;
//    private TextView doneBttn;

//    public ButtonStyleHelper(Context context) {
//        //this.context = context;
//    }

    public static void styleButtonEnabled(Context context, TextView doneBttn) {
        doneBttn.setTextColor(Color.parseColor("white")); // text color

        int backgroundColor = ContextCompat.getColor(context, R.color.colorAccent);
        doneBttn.setBackgroundColor(backgroundColor); //button color
    }

    public static void styleButtonDisabled(Context context, TextView doneBttn) {
        int textColor = ContextCompat.getColor(context, R.color.colorAccent);
        doneBttn.setTextColor(textColor); // text color

        Drawable bttnColor = ContextCompat.getDrawable(context, R.drawable.square_border_color_gray); //getting button color
        doneBttn.setBackgroundDrawable(bttnColor); //setting button color
    }
}
