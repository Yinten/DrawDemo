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


package com.ryanmattison.drawcolors.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Created by Ryan on 6/17/2015.
 *
 * A Canvas in a surface view incase in the future the drawings will have multiple in the screen and they will be dynamic in size & view heirarchy.
 * This will allow for async load of the canvas redraw in a RecyclerView.
 *
 * First Demo is to free draw with touch.
 */
public class CanvasView extends SurfaceView {
    public static final int DEMO_COLOR = 0x77FFFFFF;
    private static final String TAG = "CanvasView";


    /* The Canvas View will hold its current paint color, pathing, and revision information.
    This will helpful in view reuse if needed for the future of this application. */

    private Path _path;

    //Default Paint
    private Paint _paint;



    private float mX, mY;

    private static final float TOUCH_TOLERANCE = 1;


    public CanvasView(Context context) {
        super(context);
        init();
    }


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        setWillNotDraw(false);
        _path = new Path();
        _paint = new Paint(Paint.DITHER_FLAG);
        _paint.setAntiAlias(true);
        _paint.setDither(true);
        _paint.setColor(DEMO_COLOR);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeJoin(Paint.Join.ROUND);
        _paint.setStrokeCap(Paint.Cap.ROUND);
        _paint.setStrokeWidth(12);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.i(TAG, "Canvas Draw");
        canvas.drawPath(_path, _paint);
    }

    private void touch_start(float x, float y)
    {
        _path.reset();
        _path.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {

            _path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;


    }


    private void touch_up() {
        _path.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "Action Down");
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "Action Move");
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "Action Up");
                touch_up();
                invalidate();
                break;
        }
        return true;
    }




}
