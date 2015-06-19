package com.ryanmattison.drawcolors.views;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Ryan on 6/18/2015.
 */
public interface IPaint {

    public void undo();
    public void redo();
    public void toggleErase();
    public void changeColor(int color);
    public Bitmap captureImage();

}
