package com.venturerom.venture.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Utils {
    private static Typeface sRobotoThin;

    public static void setRobotoThin(Context context, View view) {
        if (sRobotoThin == null) {
            sRobotoThin = Typeface.createFromAsset(context.getAssets(),
                    "Roboto-Light.ttf");
        }
        setFont(view, sRobotoThin);
    }

    private static void setFont(View view, Typeface robotoTypeFace) {
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < count; i++) {
                setFont(((ViewGroup) view).getChildAt(i), robotoTypeFace);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTypeface(robotoTypeFace);
        }
    }
}