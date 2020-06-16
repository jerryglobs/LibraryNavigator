package com.example.librarynavigator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class MiniMapView extends View {
    private Drawable image;
    private ScaleGestureDetector gestureDetector;
    private float scale = 0.29f;
    private float scaleX = 0.0f;
    private float scaleY = 0.0f;
    private int id = NavigationView.id;

    public MiniMapView(Context context, AttributeSet attrs){
        super(context,attrs);

        image = ContextCompat.getDrawable(context,id);
        setFocusable(true);
        image.setBounds(0,0,image.getIntrinsicWidth(),image.getIntrinsicHeight());
        gestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.save();
        scaleX = canvas.getWidth() / 640f;
        scaleY = canvas.getHeight() / 600f;
        canvas.scale(scaleX*scale,scaleY*scale);
        image.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        invalidate();
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){
            scale *= detector.getScaleFactor();

            if(scale <0.15f)
                    scale = 0.15f;
            if(scale > 10.0f)
                scale = 10.0f;

            invalidate();
            return true;
        }
    }
}
