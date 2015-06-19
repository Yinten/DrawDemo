package com.ryanmattison.drawcolors.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by YO on 6/18/2015.
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";

    public static Uri cacheBitmapToTempStore(Context context, Bitmap b) {
        FileOutputStream fos = null;
        Uri U = null;
        try
        {
            File F = new File(context.getExternalCacheDir().getAbsolutePath(),"temp.jpg");

            F.setReadable(true, false);
            fos = new FileOutputStream(F);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            U = Uri.fromFile(F);

            fos.close();
        } catch (Exception e)
        {
            e.printStackTrace();

        }

        return U;
    }
}
