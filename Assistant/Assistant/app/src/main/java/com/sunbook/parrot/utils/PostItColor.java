package com.sunbook.parrot.utils;

import android.content.res.ColorStateList;
import android.widget.TextView;

/**
 * Created by hieuapp on 19/06/2016.
 */
public class PostItColor {

    public static ColorStateList getDisableColor(){
        ColorStateList colorBack = ColorStateList.valueOf(android.graphics.Color.BLACK);
        return colorBack.withAlpha(97);
    }

    public static ColorStateList getSecondaryColor(){
        ColorStateList colorBack = ColorStateList.valueOf(android.graphics.Color.BLACK);
        return colorBack.withAlpha(138);
    }
}
