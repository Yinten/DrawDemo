package com.ryanmattison.drawcolors.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by YO on 6/18/2015.
 */
public class ShareUtil {

    public static void shareImage(
            Context context, String uri)
    {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("image/jpeg");
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Made with YoloSwagApp");
        File file = new File(uri);

        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(Intent.createChooser(sendIntent, "Share"));

    }

}
