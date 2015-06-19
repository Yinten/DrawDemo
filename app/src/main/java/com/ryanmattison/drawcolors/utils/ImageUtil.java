package com.ryanmattison.drawcolors.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

/**
 * Created by YO on 6/18/2015.
 */
public class ImageUtil {

    public static String saveImage(Context context, Bitmap bitmap)
    {
        String url = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "title", null);
        return url;
    }
}
