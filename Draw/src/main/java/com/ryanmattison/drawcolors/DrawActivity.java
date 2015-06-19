/*
* Copyright (C) 2015 Ryan Mattison
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.ryanmattison.drawcolors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chiralcode.colorpicker.ColorPickerDialog;
import com.ryanmattison.drawcolors.utils.ImageUtil;
import com.ryanmattison.drawcolors.utils.ShareUtil;
import com.ryanmattison.drawcolors.views.CanvasView;


public class DrawActivity extends AppCompatActivity {


    private static final String TAG = "DrawActivity";

    private CanvasView _canvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        _canvasView = (CanvasView)findViewById(R.id.canvas);
        //Hide ActionBar title
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_color_picker) {
            ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, _canvasView.getColor(), new ColorPickerDialog.OnColorSelectedListener() {

                @Override
                public void onColorSelected(int color) {
                    _canvasView.setColor(color);
                    if (_canvasView.isEraseMode()) {
                        _canvasView.toggleErase();
                    }
                }

            });
            colorPickerDialog.show();

            return true;
        } else if (id == R.id.menu_erase_toggle) {
            if (_canvasView.isEraseMode()) {

                item.setIcon(R.drawable.ic_eraser_black_24dp);

            } else {
                item.setIcon(R.drawable.ic_brush_black_24dp);
            }

            _canvasView.toggleErase();
            return true;
        } else if (id == R.id.menu_redo) {
            _canvasView.redo();
            return true;
        } else if (id == R.id.menu_undo) {
            _canvasView.undo();
            return true;
        } else if (id == R.id.menu_share) {
            Bitmap bitmap = _canvasView.captureImage();
            if (bitmap != null) {
                Uri savedBitmapFileLocation = ImageUtil.cacheBitmapToTempStore(this, bitmap);
                Log.i(TAG, "Saved Bitmap Location: " + savedBitmapFileLocation);
                ShareUtil.shareImage(this, savedBitmapFileLocation);
            } else {
                Toast.makeText(this, "Image issue", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.menu_linesize)
        {
            final CharSequence[] items = {
                    "12", "16", "24"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick a line size");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    _canvasView.setLineSize(Integer.valueOf(items[item].toString()));
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
