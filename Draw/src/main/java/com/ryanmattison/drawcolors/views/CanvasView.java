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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 6/17/2015.
 *
 * A Canvas in a surface view incase in the future the drawings will have multiple in the screen and they will be dynamic in size & view heirarchy.
 * This will allow for async load of the canvas redraw in a RecyclerView.
 *
 * First Demo is to free draw with touch.
 */
public class CanvasView extends SurfaceView implements IPaint {
    public static final int DEFAULT_COLOR = 0xFF000000;
    private static final String TAG = "CanvasView";
    private static final int DEFAULT_LINESIZE = 12;

    /* The Canvas View will hold its current paint color, pathing, and revision information.
    This will helpful in view reuse if needed for the future of this application. */

    ArrayList<ShapeBO> _shapes = new ArrayList<ShapeBO>();
    ArrayList<ShapeBO> _undoneShapes = new ArrayList<ShapeBO>();

    private int _lineSize = DEFAULT_LINESIZE;
    private int _color = DEFAULT_COLOR;
    ShapeBO _shape = new ShapeBO();
    private Paint _paint;
    private float mX, mY;
    private Bitmap mBitmap;

    private static final float TOUCH_TOLERANCE = 1;
    private boolean _eraseMode = false;
    private CharSequence lineSize;


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
        setDrawingCacheEnabled(true);

        _paint = new Paint(Paint.DITHER_FLAG);
        _paint.setAntiAlias(true);
        _paint.setDither(true);
        _paint.setColor(_color);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeJoin(Paint.Join.ROUND);
        _paint.setStrokeCap(Paint.Cap.ROUND);
        _paint.setStrokeWidth(_lineSize);

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.i(TAG, "Canvas Draw");
        for (ShapeBO shape : _shapes) {
            _paint.setColor(shape.color);
            _paint.setStrokeWidth(shape.lineSize);
            canvas.drawPath(shape.path, _paint);
        }
        _paint.setColor(isEraseMode() ? Color.WHITE : _color);
        _paint.setStrokeWidth(_lineSize);
        canvas.drawPath(_shape.path, _paint);
    }

    private void touch_start(float x, float y)
    {
        _shape.path.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {

        _shape.path.quadTo(mX, mY, (x + mX)/2, (y + mY) / 2);
        mX = x;
        mY = y;


    }


    private void touch_up() {
        _shape.path.lineTo(mX, mY);
        _shape.color = isEraseMode() ? Color.WHITE : _color;
        _shape.lineSize = _lineSize;
        _shapes.add(_shape);
        _shape = new ShapeBO();
        _undoneShapes.clear();

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


    @Override
    public void undo() {
        if(_shapes.size() - 1 >= 0) {
            _undoneShapes.add(_shapes.remove(_shapes.size() - 1));
            invalidate();
        }
    }

    @Override
    public void redo() {
        if(_undoneShapes.size() - 1 >= 0) {
            _shapes.add(_undoneShapes.remove(_undoneShapes.size() - 1));
            invalidate();
        }
    }

    @Override
    public void toggleErase() {
        _eraseMode = !_eraseMode;
    }

    @Override
    public void setColor(int color) {
        _color = color;
        _paint.setColor(color);
    }

    @Override
    public int getColor() {
        return _color;
    }

    @Override
    public Bitmap captureImage() {
        this.buildDrawingCache();
        return this.getDrawingCache();
    }


    public boolean isEraseMode() {
        return _eraseMode;
    }

    public List<ShapeBO> getShapes() {
        return _shapes;
    }

    @Override
    public void setLineSize(int lineSize) {
        this._lineSize = lineSize;
    }
}
